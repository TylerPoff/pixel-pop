package edu.northeastern.numad23sp_team26.a8_stickers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.stream.IntStream;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.a8_stickers.models.StickerReceived;
import edu.northeastern.numad23sp_team26.a8_stickers.models.User;

public class ReceivedHistoryActivity extends AppCompatActivity {

    private static final String TAG = "a8_stickers.ReceivedHistoryActivity";
    private DatabaseReference mDatabase;
    private ReceivedHistoryAdapter adapter;
    private User currentUser;
    private ArrayList<StickerReceived> receivedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_history);

        FirebaseApp stickers = FirebaseApp.getInstance("stickers");
        mDatabase = FirebaseDatabase.getInstance(stickers).getReference();

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            currentUser = bundle.getParcelable("currentUser");
        }

        RecyclerView recyclerView = findViewById(R.id.receivedRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReceivedHistoryAdapter(receivedList, this);
        recyclerView.setAdapter(adapter);

        loadReceivedHistory();
    }

    private void loadReceivedHistory() {
        mDatabase.child("users").child(currentUser.username).child("stickersReceived")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        StickerReceived stickerReceived = snapshot.getValue(StickerReceived.class);
                        int index = IntStream.range(0, receivedList.size()).filter(i -> receivedList.get(i).equals(stickerReceived))
                                .findFirst().orElse(-1);

                        if (index == -1) {
                            receivedList.add(stickerReceived);
                            adapter.notifyItemInserted(receivedList.size() - 1);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        StickerReceived stickerReceived = snapshot.getValue(StickerReceived.class);
                        int index = IntStream.range(0, receivedList.size()).filter(i -> receivedList.get(i).equals(stickerReceived))
                                .findFirst().orElse(-1);

                        if (index > -1) {
                            receivedList.set(index, stickerReceived);
                            adapter.notifyItemChanged(index);
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "onCancelled:" + error);
                    }
                });
    }
}
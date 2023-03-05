package edu.northeastern.numad23sp_team26.a8_stickers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.a8_stickers.models.User;

public class StickerUserDirectoryActivity extends AppCompatActivity {

    private static final String TAG = "a8_stickers.StickerUserDirectoryActivity";
    private DatabaseReference mDatabase;
    private String currentSticker;
    private ReceiverAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_directory);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (getIntent().getExtras() != null) {
            currentSticker = getIntent().getExtras().getString("currentSticker");
        }
        updateSticker();

        loadUsers();
        updateSendingToTV("");

        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(v -> sendSticker());
    }

    public void updateSendingToTV(String toUser) {
        TextView sendingToTV = findViewById(R.id.sendingToTV);
        sendingToTV.setText(getString(R.string.you_re_sending_to, toUser));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("selectedPosition", adapter.getSelectedPosition());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        adapter.setSelectedPosition(savedInstanceState.getInt("selectedPosition"));
    }

    private void sendSticker() {

    }

    private void updateSticker() {
        ImageView stickerDirectoryIV = findViewById(R.id.stickerDirectoryIV);
        Integer image = null;
        switch (currentSticker.toLowerCase()) {
            case "frog":
                image = R.drawable.sticker_1_frog;
                break;
            case "ribbon":
                image = R.drawable.sticker_2_ribbon;
                break;
            case "backpack":
                image = R.drawable.sticker_3_backpack;
                break;
            case "board":
                image = R.drawable.sticker_4_board;
                break;
            case "cup":
                image = R.drawable.sticker_5_cup;
                break;
            case "bulb":
                image = R.drawable.sticker_6_bulb;
                break;
            case "clock":
                image = R.drawable.sticker_7_clock;
                break;
            case "book":
                image = R.drawable.sticker_8_book;
                break;
            case "bus":
                image = R.drawable.sticker_9_bus;
                break;
            default:
                break;
        }
        if (image != null) {
            stickerDirectoryIV.setImageResource(image);
        }
    }

    private void loadUsers() {
        ArrayList<User> userList = new ArrayList<>();

        RecyclerView userListRV = findViewById(R.id.userListRV);
        userListRV.setHasFixedSize(true);
        userListRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReceiverAdapter(userList, this);
        userListRV.setAdapter(adapter);

        mDatabase.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);

                if (!userList.contains(user)) {
                    userList.add(user);
                    adapter.notifyItemInserted(userList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);

                if (userList.contains(user)) {
                    int index = userList.indexOf(user);
                    userList.set(index, user);
                    adapter.notifyItemChanged(index);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (userList.contains(user)) {
                    int index = userList.indexOf(user);
                    userList.remove(user);
                    adapter.notifyItemRemoved(index);
                }
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
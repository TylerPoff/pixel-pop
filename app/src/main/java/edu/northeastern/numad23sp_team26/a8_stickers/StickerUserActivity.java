package edu.northeastern.numad23sp_team26.a8_stickers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.a8_stickers.models.StickerSent;
import edu.northeastern.numad23sp_team26.a8_stickers.models.User;

public class StickerUserActivity extends AppCompatActivity {

    private static final String TAG = "a8_stickers.StickerUserActivity";
    private DatabaseReference mDatabase;
    private ViewPager viewPager;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_user);

        TextView helloUserTV = findViewById(R.id.helloUserTV);

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> logout());

        Button btnSendStickers = findViewById(R.id.btnSendStickers);
        btnSendStickers.setOnClickListener(v -> openActivitySendSticker());

        Button btnReceivedHistory = findViewById(R.id.btnReceivedHistory);
        btnReceivedHistory.setOnClickListener(v -> openActivityReceivedHistory());

        if (getIntent().getExtras() != null) {
            currentUser = getIntent().getExtras().getParcelable("currentUser");
            helloUserTV.setText(getString(R.string.user_greeting, currentUser.username));
        }

        viewPager = findViewById(R.id.viewPager);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        loadStickers();
    }

    private void logout() {
        currentUser = null;
        Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadStickers() {
        ArrayList<StickerSent> userStickerList = new ArrayList<>();
        StickerUserAdapter adapter = new StickerUserAdapter(this, userStickerList);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(100,100,100,100);

        mDatabase.child("users").child(currentUser.username).child("stickersSent")
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                StickerSent stickerSent = snapshot.getValue(StickerSent.class);

                if (!userStickerList.contains(stickerSent)) {
                    userStickerList.add(stickerSent);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                StickerSent stickerSent = snapshot.getValue(StickerSent.class);

                if (userStickerList.contains(stickerSent)) {
                    int index = userStickerList.indexOf(stickerSent);
                    userStickerList.set(index, stickerSent);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                StickerSent stickerSent = snapshot.getValue(StickerSent.class);

                if (userStickerList.contains(stickerSent)) {
                    userStickerList.remove(stickerSent);
                    adapter.notifyDataSetChanged();
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

    public void openActivitySendSticker() {
        Intent intent = new Intent(this, StickersListActivity.class);

        // Send current user
        Bundle extras = new Bundle();
        extras.putParcelable("currentUser", currentUser);
        intent.putExtras(extras);

        startActivity(intent);
    }

    public void openActivityReceivedHistory() {
        Intent intent = new Intent(this, ReceivedHistoryActivity.class);

        // Send current user
        Bundle extras = new Bundle();
        extras.putParcelable("currentUser", currentUser);
        intent.putExtras(extras);

        startActivity(intent);
    }
}
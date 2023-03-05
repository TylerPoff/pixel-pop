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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.a8_stickers.models.Sticker;
import edu.northeastern.numad23sp_team26.a8_stickers.models.StickerReceived;
import edu.northeastern.numad23sp_team26.a8_stickers.models.StickerSent;
import edu.northeastern.numad23sp_team26.a8_stickers.models.User;

public class StickerUserDirectoryActivity extends AppCompatActivity {

    private static final String TAG = "a8_stickers.StickerUserDirectoryActivity";
    private DatabaseReference mDatabase;
    private Sticker currentSticker;
    private ReceiverAdapter adapter;
    private User currentUser;
    private User sendtoUser;
    private static boolean isSavingSendCount, isSavingReceivedHistory;
    private static boolean isSendCountSaved, isReceivedHistorySaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_directory);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            currentSticker = bundle.getParcelable("sticker");
            currentUser = bundle.getParcelable("currentUser");
        }
        updateSticker();

        loadUsers();
        updateSendingToTV("");

        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(v -> sendSticker());
    }

    public void updateSendingTo(User toUser) {
        sendtoUser = toUser;
        updateSendingToTV(toUser.firstName + " " + toUser.lastName);
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
        // Save send count
        mDatabase.child("users").child(currentUser.username).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                isSavingSendCount = true;
                isSendCountSaved = false;
                updateSendStatus();
                User u = currentData.getValue(User.class);
                if (u == null) {
                    return Transaction.success(currentData);
                }

                if (u.stickersSent == null) {
                    u.stickersSent = new ArrayList<>();
                }

                StickerSent stickerSent = null;
                for (StickerSent sent : u.stickersSent) {
                    if (sent.getSticker().getName().equalsIgnoreCase(currentSticker.getName())) {
                        stickerSent = sent;
                    }
                }

                int totalCount = 1;
                if (stickerSent != null) {
                    totalCount = stickerSent.getTotalCount() + 1;
                    u.stickersSent.set(u.stickersSent.indexOf(stickerSent), new StickerSent(currentSticker, totalCount));
                } else {
                    u.stickersSent.add(new StickerSent(currentSticker, totalCount));
                }

                currentData.setValue(u);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                // Transaction completed
                isSavingSendCount = false;
                isSendCountSaved = true;
                updateSendStatus();
                Log.d(TAG, "postTransaction:onComplete:" + error);
            }
        });

        // Save received history
        mDatabase.child("users").child(sendtoUser.username).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                isSavingReceivedHistory = true;
                isReceivedHistorySaved = false;
                updateSendStatus();
                User u = currentData.getValue(User.class);
                if (u == null) {
                    return Transaction.success(currentData);
                }

                if (u.stickersReceived == null) {
                    u.stickersReceived = new ArrayList<>();
                }
                u.stickersReceived.add(new StickerReceived(currentSticker, currentUser, LocalDateTime.now().toString()));

                currentData.setValue(u);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                // Transaction completed
                isSavingReceivedHistory = false;
                isReceivedHistorySaved = true;
                updateSendStatus();
                Log.d(TAG, "postTransaction:onComplete:" + error);
            }
        });
    }

    private void updateSendStatus() {
        TextView sendStatus = findViewById(R.id.sendStatus);
        if (!isSavingSendCount && !isSavingReceivedHistory) {
            if (isSendCountSaved && isReceivedHistorySaved) {
                sendStatus.setText("Sticker sent successfully");
            } else {
                sendStatus.setText("Failed to send sticker");
            }
        } else {
            sendStatus.setText("");
        }
    }

    private void updateSendingToTV(String toUser) {
        TextView sendingToTV = findViewById(R.id.sendingToTV);
        String textToSet = getString(R.string.you_re_sending_to, toUser);
        if (!sendingToTV.getText().toString().equalsIgnoreCase(textToSet)) {
            sendingToTV.setText(getString(R.string.you_re_sending_to, toUser));
        }
    }

    private void updateSticker() {
        ImageView stickerDirectoryIV = findViewById(R.id.stickerDirectoryIV);
        stickerDirectoryIV.setImageResource(currentSticker.getImageResource());
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
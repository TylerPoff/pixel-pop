package edu.northeastern.numad23sp_team26.a8_stickers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.stream.IntStream;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.a8_stickers.models.StickerReceived;
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
        createNotificationChannel();
        setContentView(R.layout.activity_sticker_user);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        TextView helloUserTV = findViewById(R.id.helloUserTV);

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> logout());

        Button btnSendStickers = findViewById(R.id.btnSendStickers);
        btnSendStickers.setOnClickListener(v -> openActivitySendSticker());

        Button btnReceivedHistory = findViewById(R.id.btnReceivedHistory);
        btnReceivedHistory.setOnClickListener(v -> openActivityReceivedHistory());

        if (getIntent().getExtras() != null) {
            currentUser = getIntent().getExtras().getParcelable("currentUser");
            monitorStickersReceived();
            helloUserTV.setText(getString(R.string.user_greeting, currentUser.username));
        }

        viewPager = findViewById(R.id.viewPager);

        loadStickers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "sticker_received",
                    "Sticker Received",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("You received a sticker");
            channel.enableLights(true);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
                int index = IntStream.range(0, userStickerList.size()).filter(i -> userStickerList.get(i).getSticker().getFileName()
                        .equalsIgnoreCase(stickerSent.getSticker().getFileName())).findFirst().orElse(-1);

                if (index == -1) {
                    userStickerList.add(stickerSent);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                StickerSent stickerSent = snapshot.getValue(StickerSent.class);
                int index = IntStream.range(0, userStickerList.size()).filter(i -> userStickerList.get(i).getSticker().getFileName()
                        .equalsIgnoreCase(stickerSent.getSticker().getFileName())).findFirst().orElse(-1);

                if (index > -1) {
                    userStickerList.set(index, stickerSent);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                StickerSent stickerSent = snapshot.getValue(StickerSent.class);
                int index = IntStream.range(0, userStickerList.size()).filter(i -> userStickerList.get(i).getSticker().getFileName()
                        .equalsIgnoreCase(stickerSent.getSticker().getFileName())).findFirst().orElse(-1);

                if (index > -1) {
                    userStickerList.remove(index);
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

    private void monitorStickersReceived() {
        mDatabase.child("users").child(currentUser.username).child("stickersReceived")
            .addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    StickerReceived stickerReceived = snapshot.getValue(StickerReceived.class);
                    if (stickerReceived != null && stickerReceived.isNotified == 0) {
                        sendNotification(stickerReceived);
                        updateNotified(snapshot.getKey());
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

    private void updateNotified(String strIndex) {
        mDatabase.child("users").child(currentUser.username).child("stickersReceived")
                .child(strIndex).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                StickerReceived stickerReceived = currentData.getValue(StickerReceived.class);
                if (stickerReceived == null) {
                    return Transaction.success(currentData);
                }
                stickerReceived.isNotified = 1;
                currentData.setValue(stickerReceived);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + error);
            }
        });
    }

    public void sendNotification(StickerReceived stickerReceived) {
        User fromUser = stickerReceived.getFrom();
        Intent intent = new Intent(this, ReceiveNotificationActivity.class);

        // Send current sticker
        Bundle extras = new Bundle();
        extras.putString("stickerReceivedFromFullName", fromUser.firstName + " " + fromUser.lastName);
        extras.putString("stickerReceivedFileName", stickerReceived.getSticker().getFileName());
        intent.putExtras(extras);

        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_MUTABLE);

        String channelId = "sticker_received";
        int imageResource = getResources().getIdentifier(stickerReceived.getSticker().getFileName(), "drawable", getPackageName());
        Bitmap stickerIcon = BitmapFactory.decodeResource(getResources(), imageResource);

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("(Team26) Sticker Received")
                .setContentText("You received a sticker from " + fromUser.firstName + " " + fromUser.lastName)
                .setLargeIcon(stickerIcon)
                .setSmallIcon(R.mipmap.ic_launcher_t26)
                .setContentIntent(pIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL ;

        notificationManager.notify(1, notification);
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
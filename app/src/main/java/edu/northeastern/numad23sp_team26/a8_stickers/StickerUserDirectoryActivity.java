package edu.northeastern.numad23sp_team26.a8_stickers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        setContentView(R.layout.activity_sticker_directory);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (getIntent().getExtras() != null) {
            currentSticker = getIntent().getExtras().getString("currentSticker");
        }
        updateSticker();

        loadUsers();
        updateSendingToTV("");

        //TODO Send button functionality
    }

        public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_id), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService (NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendNotification(View view) {
        Intent intent = new Intent(this, ReceiveNotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        String channelId = getString(R.string.channel_id);
        NotificationCompat.Builder notifyBuild = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_t26_foreground)
                .setContentTitle("You got a sticker!")
                .setContentText("Check out the sticker?")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, notifyBuild.build());
    }

    public void updateSendingToTV(String toUser) {
        TextView sendingToTV = findViewById(R.id.sendingToTV);
        sendingToTV.setText(getString(R.string.you_re_sending_to, toUser));
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
        ReceiverAdapter adapter = new ReceiverAdapter(userList, this);
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
package edu.northeastern.numad23sp_team26;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class App extends Application {
    public static final String channelName="notification";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel(){
        NotificationChannel channel = new NotificationChannel(
                channelName,
                "New Sticker",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription("You received a new sticker");
        channel.enableLights(true);
        NotificationManager notificationManager = getSystemService (NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}

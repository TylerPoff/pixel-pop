package edu.northeastern.numad23sp_team26.a8_stickers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import edu.northeastern.numad23sp_team26.R;

public class ReceiveNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_notification);
        TextView gotStickerFromTV = findViewById(R.id.gotStickerFromTV);
        ImageView receivedStickerIV = findViewById(R.id.receivedStickerIV);

    }
}
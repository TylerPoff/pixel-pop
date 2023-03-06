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

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            gotStickerFromTV.setText(bundle.getString("stickerReceivedFromFullName"));
            int imageResource = getResources().getIdentifier(bundle.getString("stickerReceivedFileName"), "drawable", getPackageName());
            receivedStickerIV.setImageResource(imageResource);
        }
    }
}
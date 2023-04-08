package edu.northeastern.numad23sp_team26.a8_stickers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.a8_stickers.models.Sticker;
import edu.northeastern.numad23sp_team26.a8_stickers.models.User;

public class StickersListActivity extends AppCompatActivity {

    private static final String TAG = "a8_stickers.StickersListActivity";
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickers_list);

        if (getIntent().getExtras() != null) {
            currentUser = getIntent().getExtras().getParcelable("currentUser");
        }
    }

    public void sendSticker(View v) {
        Sticker sticker;
        if (v.getId() == R.id.frogImageButton) {
            sticker = new Sticker("Frog", "sticker_1_frog");
        } else if (v.getId() == R.id.ribbonImageButton) {
            sticker = new Sticker("Ribbon", "sticker_2_ribbon");
        } else if (v.getId() == R.id.backpackImageButton) {
            sticker = new Sticker("Backpack", "sticker_3_backpack");
        } else if (v.getId() == R.id.boardImageButton) {
            sticker = new Sticker("Board", "sticker_4_board");
        } else if (v.getId() == R.id.cupImageButton) {
            sticker = new Sticker("Cup", "sticker_5_cup");
        } else if (v.getId() == R.id.bulbImageButton) {
            sticker = new Sticker("Bulb", "sticker_6_bulb");
        } else if (v.getId() == R.id.clockImageButton) {
            sticker = new Sticker("Clock", "sticker_7_clock");
        } else if (v.getId() == R.id.bookImageButton) {
            sticker = new Sticker("Book", "sticker_8_book");
        } else if (v.getId() == R.id.busImageButton) {
            sticker = new Sticker("Bus", "sticker_9_bus");
        } else {
            Log.e(TAG, "Invalid sticker");
            return;
        }

        Intent intent = new Intent (getApplicationContext(), StickerUserDirectoryActivity.class);

        // Send current sticker
        Bundle extras = new Bundle();
        extras.putParcelable("sticker", sticker);
        extras.putParcelable("currentUser", currentUser);
        intent.putExtras(extras);

        startActivity(intent);
    }
}
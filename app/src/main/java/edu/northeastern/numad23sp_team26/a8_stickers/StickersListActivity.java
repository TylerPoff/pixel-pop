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
        String stickerName;
        int stickerImageResource;
        if (v.getId() == R.id.frogImageButton) {
            stickerName = "Frog";
            stickerImageResource = R.drawable.sticker_1_frog;
        } else if (v.getId() == R.id.ribbonImageButton) {
            stickerName = "Ribbon";
            stickerImageResource = R.drawable.sticker_2_ribbon;
        } else if (v.getId() == R.id.backpackImageButton) {
            stickerName = "Backpack";
            stickerImageResource = R.drawable.sticker_3_backpack;
        } else if (v.getId() == R.id.boardImageButton) {
            stickerName = "Board";
            stickerImageResource = R.drawable.sticker_4_board;
        } else if (v.getId() == R.id.cupImageButton) {
            stickerName = "Cup";
            stickerImageResource = R.drawable.sticker_5_cup;
        } else if (v.getId() == R.id.bulbImageButton) {
            stickerName = "Bulb";
            stickerImageResource = R.drawable.sticker_6_bulb;
        } else if (v.getId() == R.id.clockImageButton) {
            stickerName = "Clock";
            stickerImageResource = R.drawable.sticker_7_clock;
        } else if (v.getId() == R.id.bookImageButton) {
            stickerName = "Book";
            stickerImageResource = R.drawable.sticker_8_book;
        } else if (v.getId() == R.id.busImageButton) {
            stickerName = "Bus";
            stickerImageResource = R.drawable.sticker_9_bus;
        } else {
            Log.e(TAG, "Invalid sticker");
            return;
        }

        Intent intent = new Intent (getApplicationContext(), StickerUserDirectoryActivity.class);

        // Send current sticker
        Bundle extras = new Bundle();
        extras.putParcelable("sticker", new Sticker(stickerName, stickerImageResource));
        extras.putParcelable("currentUser", currentUser);
        intent.putExtras(extras);

        startActivity(intent);
    }
}
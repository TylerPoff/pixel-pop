package edu.northeastern.numad23sp_team26.a8_stickers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import edu.northeastern.numad23sp_team26.R;

public class StickersListActivity extends AppCompatActivity {

    private static final String TAG = "a8_stickers.StickersListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickers_list);
    }

    public void sendSticker(View v) {
        String stickerName = "";
        if (v.getId() == R.id.frogImageButton) {
            stickerName = "Frog";
        } else if (v.getId() == R.id.ribbonImageButton) {
            stickerName = "Ribbon";
        } else if (v.getId() == R.id.backpackImageButton) {
            stickerName = "Backpack";
        } else if (v.getId() == R.id.boardImageButton) {
            stickerName = "Board";
        } else if (v.getId() == R.id.cupImageButton) {
            stickerName = "Cup";
        } else if (v.getId() == R.id.bulbImageButton) {
            stickerName = "Bulb";
        } else if (v.getId() == R.id.clockImageButton) {
            stickerName = "Clock";
        } else if (v.getId() == R.id.bookImageButton) {
            stickerName = "Book";
        } else if (v.getId() == R.id.busImageButton) {
            stickerName = "Bus";
        } else {
            Log.e(TAG, "Invalid sticker");
            return;
        }

        Intent intent = new Intent (getApplicationContext(), StickerUserDirectoryActivity.class);

        // Send current sticker
        Bundle extras = new Bundle();
        extras.putString("currentSticker", stickerName);
        intent.putExtras(extras);

        startActivity(intent);
    }
}
package edu.northeastern.numad23sp_team26;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

public class StickersListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickers_list);
        addStickerButtons();

    }

    private void addStickerButtons() {
        GridLayout stickerGrid = findViewById(R.id.stickerGrid);
        Resources res = getResources();
        String packageName = getPackageName();
        TypedArray stickerIds = res.obtainTypedArray(R.array.sticker_drawables);

        // calculate the size of each button based on the screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int buttonWidth = (int) Math.floor(screenWidth / 2.5);

        // iterate over the array of sticker resource IDs
        for (int i = 0; i < stickerIds.length(); i++) {
            int resourceId = stickerIds.getResourceId(i, -1);

            // create a new ImageButton for each sticker resource
            ImageButton button = new ImageButton(this);
            button.setLayoutParams(new GridLayout.LayoutParams(
                    new ViewGroup.LayoutParams(buttonWidth, buttonWidth)));
            button.setPadding(8, 8, 8, 8);
            button.setScaleType(ImageView.ScaleType.CENTER_CROP);
            button.setImageResource(resourceId);
            button.setContentDescription(getString(R.string.Stickers_button));

            // add the new button to the GridLayout
            stickerGrid.addView(button);
        }

        stickerIds.recycle();
    }
}
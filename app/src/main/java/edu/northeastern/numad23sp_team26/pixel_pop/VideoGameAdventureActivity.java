package edu.northeastern.numad23sp_team26.pixel_pop;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad23sp_team26.R;

public class VideoGameAdventureActivity extends AdventureActivity {
    MediaPlayer player;

    private Button button1, button2, button3, button4, button5;
    private final String ADVENTURE_TYPE = "video game";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_game_adventure);

        musicPlay();

        button1 = findViewById(R.id.button1);
        button1.setOnClickListener(v -> createAlertDialog(1, ADVENTURE_TYPE));

        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(v -> createAlertDialog(2, ADVENTURE_TYPE));

        button3 = findViewById(R.id.button3);
        button3.setOnClickListener(v -> createAlertDialog(3, ADVENTURE_TYPE));

        button4 = findViewById(R.id.button4);
        button4.setOnClickListener(v -> createAlertDialog(4, ADVENTURE_TYPE));

        button5 = findViewById(R.id.button5);
        button5.setOnClickListener(v -> createAlertDialog(5, ADVENTURE_TYPE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        musicPlay();
        getUnlockedLevels("video game");
    }

    @Override public void onPause() {
        super.onPause();
        musicStop();
    }

    @Override
    public void openActivityPixelDraw(int levelNum) {
        ArrayList<Integer> colorList = new ArrayList<>();
        colorList.add(getColor(R.color.black));
        colorList.add(getColor(R.color.red));
        colorList.add(getColor(R.color.nature_green));
        colorList.add(getColor(R.color.blue));
        colorList.add(getColor(R.color.yellow));
        colorList.add(getColor(R.color.orange));
        colorList.add(getColor(R.color.maple));
        colorList.add(getColor(R.color.brown));

        Intent intent = new Intent(this, DrawActivity.class);

        Bundle extras = new Bundle();
        extras.putString("adventure", ADVENTURE_TYPE);
        extras.putInt("levelNum", levelNum);
        extras.putInt("maxLevels", 5);
        extras.putIntegerArrayList("colorList", colorList);
        intent.putExtras(extras);

        startActivity(intent);
    }

    @Override
    public void updateUnlockLevels(List<Integer> unlockLevels) {
        for (int l : unlockLevels) {
            switch (l) {
                case 1:
                    button1.setEnabled(true);
                    break;
                case 2:
                    button2.setEnabled(true);
                    break;
                case 3:
                    button3.setEnabled(true);
                    break;
                case 4:
                    button4.setEnabled(true);
                    break;
                case 5:
                    button5.setEnabled(true);
                    break;
            }
        }
    }

    public void musicPlay() {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.supermariobros);
        }
        player.start();
        player.setLooping(true);
    }

    //Could create button for musicStop
    public void musicStop() {
        if (player != null) {
            player.release();
            player = null;
            Toast.makeText(this, "Music off", Toast.LENGTH_SHORT);
        }
    }
}
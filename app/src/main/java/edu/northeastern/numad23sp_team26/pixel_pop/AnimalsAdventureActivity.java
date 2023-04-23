package edu.northeastern.numad23sp_team26.pixel_pop;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad23sp_team26.R;

public class AnimalsAdventureActivity extends AdventureActivity {

    private Button animals_pixel_drawing_1_button, animals_pixel_drawing_2_button, animals_pixel_drawing_3_button, animals_pixel_drawing_4_button, animals_pixel_drawing_5_button;
    private final String ADVENTURE_TYPE = "animals";
    private final int MAX_LEVELS = 5;

    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_adventure);

        musicPlay();

        animals_pixel_drawing_1_button = findViewById(R.id.animals_pixel_drawing_1_button);
        animals_pixel_drawing_1_button.setOnClickListener(v -> {
            if (multiPlayGameID.isEmpty()) {
                createAlertDialog(1, ADVENTURE_TYPE);
            } else {
                createMultiAlertDialog(1, ADVENTURE_TYPE);
            }
        });

        animals_pixel_drawing_2_button = findViewById(R.id.animals_pixel_drawing_2_button);
        animals_pixel_drawing_2_button.setOnClickListener(v -> {
            if (multiPlayGameID.isEmpty()) {
                createAlertDialog(2, ADVENTURE_TYPE);
            } else {
                createMultiAlertDialog(2, ADVENTURE_TYPE);
            }
        });

        animals_pixel_drawing_3_button = findViewById(R.id.animals_pixel_drawing_3_button);
        animals_pixel_drawing_3_button.setOnClickListener(v -> {
            if (multiPlayGameID.isEmpty()) {
                createAlertDialog(3, ADVENTURE_TYPE);
            } else {
                createMultiAlertDialog(3, ADVENTURE_TYPE);
            }
        });

        animals_pixel_drawing_4_button = findViewById(R.id.animals_pixel_drawing_4_button);
        animals_pixel_drawing_4_button.setOnClickListener(v -> {
            if (multiPlayGameID.isEmpty()) {
                createAlertDialog(4, ADVENTURE_TYPE);
            } else {
                createMultiAlertDialog(4, ADVENTURE_TYPE);
            }
        });

        animals_pixel_drawing_5_button = findViewById(R.id.animals_pixel_drawing_5_button);
        animals_pixel_drawing_5_button.setOnClickListener(v -> {
            if (multiPlayGameID.isEmpty()) {
                createAlertDialog(5, ADVENTURE_TYPE);
            } else {
                createMultiAlertDialog(5, ADVENTURE_TYPE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        musicPlay();
        if (multiPlayGameID.isEmpty()) {
            getUnlockedLevels(ADVENTURE_TYPE);
        } else {
            getMultiPlayUnlockedLevels(ADVENTURE_TYPE);
        }
    }

    @Override
    public void onPause() {
        musicStop();
        super.onPause();
    }

    @Override
    public void openActivityPixelDraw(int levelNum, PlayMode playMode) {
        ArrayList<Integer> colorList = new ArrayList<>();
        colorList.add(getColor(R.color.black));
        colorList.add(getColor(R.color.red));
        colorList.add(getColor(R.color.nature_green));
        colorList.add(getColor(R.color.maple));
        colorList.add(getColor(R.color.yellow));
        colorList.add(getColor(R.color.orange));
        colorList.add(getColor(R.color.cheese));
        colorList.add(getColor(R.color.brown));

        Intent intent = new Intent(this, DrawActivity.class);

        Bundle extras = new Bundle();
        extras.putString("adventure", ADVENTURE_TYPE);
        extras.putInt("levelNum", levelNum);
        extras.putInt("maxLevels", MAX_LEVELS);
        extras.putIntegerArrayList("colorList", colorList);
        extras.putString("playMode", playMode.toString());
        extras.putString("playerNum", "p1");
        extras.putString("gameID", multiPlayGameID);
        intent.putExtras(extras);

        startActivity(intent);
    }

    @Override
    public void updateUnlockLevels(List<Integer> unlockLevels) {
        for (int l : unlockLevels) {
            switch (l) {
                case 1:
                    animals_pixel_drawing_1_button.setEnabled(true);
                    break;
                case 2:
                    animals_pixel_drawing_2_button.setEnabled(true);
                    break;
                case 3:
                    animals_pixel_drawing_3_button.setEnabled(true);
                    break;
                case 4:
                    animals_pixel_drawing_4_button.setEnabled(true);
                    break;
                case 5:
                    animals_pixel_drawing_5_button.setEnabled(true);
                    break;
            }
        }
    }

    public void musicPlay() {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.tranquility);
        }
        player.start();
        player.setLooping(true);
    }

    public void musicStop() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
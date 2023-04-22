package edu.northeastern.numad23sp_team26.pixel_pop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad23sp_team26.R;

public class AnimalsAdventureActivity extends AdventureActivity {

    private Button animals_pixel_drawing_1_button, animals_pixel_drawing_2_button, animals_pixel_drawing_3_button, animals_pixel_drawing_4_button, animals_pixel_drawing_5_button;
    private final String ADVENTURE_TYPE = "animals";
    private final int MAX_LEVELS = 5;
    private String multiPlayGameID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_adventure);

        TextView gameIdTV = findViewById(R.id.gameIdTV);

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            multiPlayGameID = extras.getString("gameID");

            if (multiPlayGameID.isEmpty()) {
                gameIdTV.setVisibility(View.INVISIBLE);
            } else {
                gameIdTV.setText(getString(R.string.game_id, multiPlayGameID));
                createGameIdDialog(multiPlayGameID);
            }
        }

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
        getUnlockedLevels(ADVENTURE_TYPE);
    }

    @Override
    public void openActivityPixelDraw(int levelNum) {
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
}
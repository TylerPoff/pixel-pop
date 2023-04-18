package edu.northeastern.numad23sp_team26.pixel_pop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad23sp_team26.R;

public class AnimalsAdventureActivity extends AdventureActivity {

    private Button animals_pixel_drawing_1_button, animals_pixel_drawing_2_button, animals_pixel_drawing_3_button, animals_pixel_drawing_4_button, animals_pixel_drawing_5_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_adventure);

        getUnlockedLevels("animals");

        animals_pixel_drawing_1_button = findViewById(R.id.animals_pixel_drawing_1_button);
        animals_pixel_drawing_1_button.setOnClickListener(v -> createAlertDialog(1));

        animals_pixel_drawing_2_button = findViewById(R.id.animals_pixel_drawing_2_button);
        animals_pixel_drawing_2_button.setOnClickListener(v -> createAlertDialog(2));

        animals_pixel_drawing_3_button = findViewById(R.id.animals_pixel_drawing_3_button);
        animals_pixel_drawing_3_button.setOnClickListener(v -> createAlertDialog(3));

        animals_pixel_drawing_4_button = findViewById(R.id.animals_pixel_drawing_4_button);
        animals_pixel_drawing_4_button.setOnClickListener(v -> createAlertDialog(4));

        animals_pixel_drawing_5_button = findViewById(R.id.animals_pixel_drawing_5_button);
        animals_pixel_drawing_5_button.setOnClickListener(v -> createAlertDialog(5));
    }

    @Override
    public void openActivityPixelDraw(int levelNum) {
        ArrayList<Integer> colorList = new ArrayList<>();
        colorList.add(getColor(R.color.black));
        colorList.add(getColor(R.color.red));
        colorList.add(getColor(R.color.green));
        colorList.add(getColor(R.color.maple));
        colorList.add(getColor(R.color.yellow));
        colorList.add(getColor(R.color.orange));
        colorList.add(getColor(R.color.cheese));
        colorList.add(getColor(R.color.brown));

        Intent intent = new Intent(this, DrawActivity.class);

        Bundle extras = new Bundle();
        extras.putString("adventure", "animals");
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
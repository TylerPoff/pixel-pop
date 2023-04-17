package edu.northeastern.numad23sp_team26.pixel_pop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;

import edu.northeastern.numad23sp_team26.R;

public class AnimalsAdventureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_adventure);

        Button animals_pixel_drawing_1_button = findViewById(R.id.animals_pixel_drawing_1_button);
        animals_pixel_drawing_1_button.setOnClickListener(v -> openActivityPixelDraw(1));

        Button animals_pixel_drawing_2_button = findViewById(R.id.animals_pixel_drawing_2_button);
        animals_pixel_drawing_2_button.setOnClickListener(v -> openActivityPixelDraw(2));

        Button animals_pixel_drawing_3_button = findViewById(R.id.animals_pixel_drawing_3_button);
        animals_pixel_drawing_3_button.setOnClickListener(v -> openActivityPixelDraw(3));

        Button animals_pixel_drawing_4_button = findViewById(R.id.animals_pixel_drawing_4_button);
        animals_pixel_drawing_4_button.setOnClickListener(v -> openActivityPixelDraw(4));

        Button animals_pixel_drawing_5_button = findViewById(R.id.animals_pixel_drawing_5_button);
        animals_pixel_drawing_5_button.setOnClickListener(v -> openActivityPixelDraw(5));
    }

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
        extras.putString("adventure", "animals");
        extras.putInt("levelNum", levelNum);
        extras.putIntegerArrayList("colorList", colorList);
        intent.putExtras(extras);

        startActivity(intent);
    }
}
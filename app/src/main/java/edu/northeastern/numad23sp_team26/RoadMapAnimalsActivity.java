package edu.northeastern.numad23sp_team26;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class RoadMapAnimalsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_map_animals);

        Button animals_pixel_drawing_1_button = findViewById(R.id.animals_pixel_drawing_1_button);
        animals_pixel_drawing_1_button.setOnClickListener(v -> {});

        Button animals_pixel_drawing_2_button = findViewById(R.id.animals_pixel_drawing_2_button);
        animals_pixel_drawing_2_button.setOnClickListener(v -> {});

        Button animals_pixel_drawing_3_button = findViewById(R.id.animals_pixel_drawing_3_button);
        animals_pixel_drawing_3_button.setOnClickListener(v -> {});

        Button animals_pixel_drawing_4_button = findViewById(R.id.animals_pixel_drawing_4_button);
        animals_pixel_drawing_4_button.setOnClickListener(v -> {});

        Button animals_pixel_drawing_5_button = findViewById(R.id.animals_pixel_drawing_5_button);
        animals_pixel_drawing_5_button.setOnClickListener(v -> {});
    }
}
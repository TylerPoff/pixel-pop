package edu.northeastern.numad23sp_team26;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RoadMapAnimalsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_map_animals);

        Button animals_pixel_drawing_1_button = findViewById(R.id.animals_pixel_drawing_1_button);
        animals_pixel_drawing_1_button.setOnClickListener(v -> openAnimalsPixelDrawing1Activity());

        Button animals_pixel_drawing_2_button = findViewById(R.id.animals_pixel_drawing_2_button);
        animals_pixel_drawing_2_button.setOnClickListener(v -> openAnimalsPixelDrawing2Activity());

        Button animals_pixel_drawing_3_button = findViewById(R.id.animals_pixel_drawing_3_button);
        animals_pixel_drawing_3_button.setOnClickListener(v -> openAnimalsPixelDrawing3Activity());

        Button animals_pixel_drawing_4_button = findViewById(R.id.animals_pixel_drawing_4_button);
        animals_pixel_drawing_4_button.setOnClickListener(v -> openAnimalsPixelDrawing4Activity());

        Button animals_pixel_drawing_5_button = findViewById(R.id.animals_pixel_drawing_5_button);
        animals_pixel_drawing_5_button.setOnClickListener(v -> openAnimalsPixelDrawing5Activity());
    }

    public void openAnimalsPixelDrawing1Activity() {
        Intent intent = new Intent(this, AnimalsPixelDrawing1Activity.class);
        startActivity(intent);
    }
    public void openAnimalsPixelDrawing2Activity() {
        Intent intent = new Intent(this, AnimalsPixelDrawing2Activity.class);
        startActivity(intent);
    }

    public void openAnimalsPixelDrawing3Activity() {
        Intent intent = new Intent(this, AnimalsPixelDrawing3Activity.class);
        startActivity(intent);
    }

    public void openAnimalsPixelDrawing4Activity() {
        Intent intent = new Intent(this, AnimalsPixelDrawing4Activity.class);
        startActivity(intent);
    }
    public void openAnimalsPixelDrawing5Activity() {
        Intent intent = new Intent(this, AnimalsPixelDrawing5Activity.class);
        startActivity(intent);
    }
}
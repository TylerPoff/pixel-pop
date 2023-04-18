package edu.northeastern.numad23sp_team26.pixel_pop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import edu.northeastern.numad23sp_team26.R;

public class AnimalsAdventureActivity extends AppCompatActivity {

    // we have to get the shouldShake from the previous activity and pass it
    // forward to the draw activity
    private boolean shouldShake = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_adventure);

        // we passed a boolean value into this intent so grab it out
        Bundle extras = getIntent().getExtras();
        // this should never be null but error check that we actually have values
        if (extras != null){
            shouldShake = extras.getBoolean("shouldShake");
        }

        Button animals_pixel_drawing_1_button = findViewById(R.id.animals_pixel_drawing_1_button);
        animals_pixel_drawing_1_button.setOnClickListener(v -> openActivityPixelDraw());

        Button animals_pixel_drawing_2_button = findViewById(R.id.animals_pixel_drawing_2_button);
        animals_pixel_drawing_2_button.setOnClickListener(v -> openActivityPixelDraw());

        Button animals_pixel_drawing_3_button = findViewById(R.id.animals_pixel_drawing_3_button);
        animals_pixel_drawing_3_button.setOnClickListener(v -> openActivityPixelDraw());

        Button animals_pixel_drawing_4_button = findViewById(R.id.animals_pixel_drawing_4_button);
        animals_pixel_drawing_4_button.setOnClickListener(v -> openActivityPixelDraw());

        Button animals_pixel_drawing_5_button = findViewById(R.id.animals_pixel_drawing_5_button);
        animals_pixel_drawing_5_button.setOnClickListener(v -> openActivityPixelDraw());
    }

    public void openActivityPixelDraw() {
        Intent intent = new Intent(this, DrawActivity.class);
        intent.putExtra("shouldShake", shouldShake);
        startActivity(intent);
    }
}
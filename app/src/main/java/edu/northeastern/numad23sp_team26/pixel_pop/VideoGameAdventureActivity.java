package edu.northeastern.numad23sp_team26.pixel_pop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import edu.northeastern.numad23sp_team26.R;

public class VideoGameAdventureActivity extends AppCompatActivity {
private boolean shouldShake = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_game_adventure);

        //we passed a boolean value into this intent so grab it out
        Bundle extras = getIntent().getExtras();
        //this should never be null but error check that we actually have values
        if (extras != null){
            shouldShake = extras.getBoolean("shouldShake");
        }

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(v -> openActivityPixelDraw());

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(v -> openActivityPixelDraw());

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(v -> openActivityPixelDraw());

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(v -> openActivityPixelDraw());

        Button button5 = findViewById(R.id.button5);
        button5.setOnClickListener(v -> openActivityPixelDraw());
    }

    public void openActivityPixelDraw() {
        Intent intent = new Intent(this, DrawActivity.class);
        intent.putExtra("shouldShake", shouldShake);
        startActivity(intent);
    }
}
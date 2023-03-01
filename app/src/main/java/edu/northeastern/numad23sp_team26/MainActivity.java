package edu.northeastern.numad23sp_team26;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Turn on strict mode only if we're debugging
        if (BuildConfig.DEBUG) {
            // The code below is from Android docs on StrictMode.
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    // .penaltyDeath() uncomment this to crash if policy is violated instead of just logging
                    .build());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnMovie = findViewById(R.id.btnMovie);
//        Button btnSticker = findViewById(R.id.btnSticker);
        btnMovie.setOnClickListener(v -> openActivityMovie());
    }

    public void openActivityMovie() {
        Intent intent = new Intent(this, MovieActivity.class);
        startActivity(intent);
    }
}
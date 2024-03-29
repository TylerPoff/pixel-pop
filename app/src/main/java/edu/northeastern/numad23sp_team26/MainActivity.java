package edu.northeastern.numad23sp_team26;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;

import com.google.firebase.BuildConfig;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import edu.northeastern.numad23sp_team26.a8_stickers.LoginActivity;
import edu.northeastern.numad23sp_team26.pixel_pop.PixelPopLoginActivity;

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

        // Initialize stickers database
        // Uses stickers name to get further instances
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId(getString(R.string.firebaseIdStickers))
                .setApiKey(getString(R.string.firebaseApiKeyStickers))
                .setDatabaseUrl(getString(R.string.firebaseUrlStickers))
                .build();
        FirebaseApp.initializeApp(this, options, "stickers");

        Button btnAbout = findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(v -> openActivityAbout());

        Button btnMovie = findViewById(R.id.btnMovie);
        btnMovie.setOnClickListener(v -> openActivityMovie());

        Button btnSticker = findViewById(R.id.btnSticker);
        btnSticker.setOnClickListener(v -> openActivitySticker());

        Button btnPixelPop = findViewById(R.id.btnPixelPop);
        btnPixelPop.setOnClickListener(v -> openActivityPixelPop());
    }

    public void openActivityAbout() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void openActivityMovie() {
        Intent intent = new Intent(this, MovieActivity.class);
        startActivity(intent);
    }

    public void openActivitySticker() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openActivityPixelPop() {
        Intent intent = new Intent(this, PixelPopLoginActivity.class);
        startActivity(intent);
    }
}
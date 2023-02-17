package edu.northeastern.numad23sp_team26;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MovieActivity extends AppCompatActivity {

    private final String omdbApiUrl = BuildConfig.OMDB_API_URL;
    private final String omdbApiKey = BuildConfig.OMDB_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
    }
}
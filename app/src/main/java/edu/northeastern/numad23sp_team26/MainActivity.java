package edu.northeastern.numad23sp_team26;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnMovie = findViewById(R.id.btnMovie);
        btnMovie.setOnClickListener(v -> openActivityMovie());
    }
    public void openActivityMovie() {
        Intent intent = new Intent(this, MovieActivity.class);
        startActivity(intent);
    }
}
package edu.northeastern.numad23sp_team26.pixel_pop;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.numad23sp_team26.R;

public class DrawActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_draw);
        DrawView drawView = new DrawView(this);
        setContentView(drawView);
    }
}

package edu.northeastern.numad23sp_team26.pixel_pop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.numad23sp_team26.R;
import android.util.Log;


public class DrawActivity extends AppCompatActivity {

    private DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        drawView = findViewById(R.id.drawView);

        Button resetBtn = findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(v -> drawView.resetFills());

        Button logBtn = findViewById(R.id.logBtn);
        logBtn.setOnClickListener(v -> {
            String pixelCellsText = drawView.pixelCellsToString();
            Log.d("PixelCells", pixelCellsText);
        });
    }

    public void onColorBtnClick(View view) {
        int color;
        if (view.getId() == R.id.redColorBtn) {
            color = getColor(R.color.red);
        } else if (view.getId() == R.id.blackColorBtn) {
            color = getColor(R.color.black);
        } else if (view.getId() == R.id.blueColorBtn) {
            color = getColor(R.color.blue);
        } else if (view.getId() == R.id.greenColorBtn) {
            color = getColor(R.color.green);
        } else if (view.getId() == R.id.yellowColorBtn) {
            color = getColor(R.color.yellow);
        } else if (view.getId() == R.id.orangeColorBtn) {
            color = getColor(R.color.orange);
        } else if (view.getId() == R.id.purpleColorBtn) {
            color = getColor(R.color.purple);
        } else if (view.getId() == R.id.brownColorBtn) {
            color = getColor(R.color.brown);
        } else if (view.getId() == R.id.whiteColorBtn) {
            color = getColor(R.color.white);
        } else if (view.getId() == R.id.eraserBtn) {
            color = getColor(R.color.white);
        } else {
            throw new IllegalArgumentException("Unsupported color");
        }
        drawView.changeFillColor(color);
    }
}

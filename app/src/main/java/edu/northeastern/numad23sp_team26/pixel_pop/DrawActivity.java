package edu.northeastern.numad23sp_team26.pixel_pop;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.seismic.ShakeDetector;

import edu.northeastern.numad23sp_team26.R;

public class DrawActivity extends AppCompatActivity implements ShakeDetector.Listener {

    // todo: send toggle argument over
    private ShakeDetector shakeDetector;

    private DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        drawView = findViewById(R.id.drawView);

        Button resetBtn = findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(v -> drawView.resetFills());

        shakeDetector = new ShakeDetector(this);

        /**********************//**********************//**********************/
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Reset")
                .setMessage("Are you sure you want to reset your drawing?")
                .setPositiveButton("Yes", null)
                .setNegativeButton("No", null)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DrawActivity.this, "Not closing", Toast.LENGTH_SHORT).show();
            }
        });

        /**********************//**********************//**********************/

    }

    @Override
    protected void onResume() {
        super.onResume();
        SensorManager sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        // todo: guards will also make sure the toggle is not off
        // todo: guard and check to see how many times it asks not to clear
        shakeDetector.start( sensorManager, SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onPause() {
        super.onPause();
        shakeDetector.stop();
    }

    @Override
    public void hearShake() {
//        Toast.makeText(getContext(), "I've been shaken!", Toast.LENGTH_SHORT).show();
        // todo: show dialog, if they click ok, call reset fills
        // todo: add a member variable to keep track how many times they said not to reset (no 3 times in a row.. if yes rest it to 0)
        drawView.resetFills();
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

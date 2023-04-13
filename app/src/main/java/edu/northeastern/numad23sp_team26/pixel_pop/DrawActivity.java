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
    //TODO make variable to keep track of whether or not we should shake
    private boolean shouldShake = false;
    private ShakeDetector shakeDetector;


    private DrawView drawView;
    private int noCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        drawView = findViewById(R.id.drawView);

        Button resetBtn = findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(v -> drawView.resetFills());

        //TODO get the variable out of the intent to see if we should shake
        //we passed a boolean value into this intent so grab it out
        Bundle extras = getIntent().getExtras();
        //this should never be null but error check that we actually have values
        if (extras != null){
            shouldShake = extras.getBoolean("shouldShake");
            Toast.makeText(this, "shouldShake: " + shouldShake, Toast.LENGTH_SHORT).show();
        }

        if (shouldShake) {
            shakeDetector = new ShakeDetector(this);
        }

        /**********************//**********************//**********************/
//
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle("Shake to Erase")
//                .setMessage("Are you sure you want to reset your drawing?")
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        hearShake();
//                    }
//                })
//
//                .show();
//
//        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//        positiveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(DrawActivity.this, "Not closing", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });

        /**********************//**********************//**********************/

    }

    @Override
    protected void onResume() {
        super.onResume();
        SensorManager sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        // todo: guards will also make sure the toggle is not off
        // todo: guard and check to see how many times it asks not to clear
        if (shouldShake) {
            shakeDetector.start(sensorManager, SensorManager.SENSOR_DELAY_GAME);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (shouldShake) {
            shakeDetector.stop();
        }
    }

    @Override
    public void hearShake() {
        Toast.makeText(this, "I've been shaken!", Toast.LENGTH_SHORT).show();
        // todo: show dialog, if they click ok, call reset fills
        // todo: add a member variable to keep track how many times they said not to reset (no 3 times in a row.. if yes rest it to 0)
        drawView.resetFills();



        /**********************//**********************//**********************/


       AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Shake to Erase")
                .setMessage("Are you sure you want to reset your drawing?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //add to the no count
                        noCount++;
                        if (noCount == 3){
                            shouldShake = false;
                            //if the shake detector is active, stop it
                            if (shakeDetector != null) {
                                shakeDetector.stop();
                            }
                            showSimpleDialog("Shake To Erase","Shake To Erase has been turned off!\nGo to Select Adventure to turn back on.");
                        }
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        drawView.resetFills();

                        //a yes should reset the no count
                        noCount = 0;
                    }
                })
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DrawActivity.this, "Not closing", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        /**********************//**********************//**********************/

    }

    private void  showSimpleDialog(String title, String message){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                       dialog.dismiss();
                    }
                })
                .show();
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

package edu.northeastern.numad23sp_team26.pixel_pop;

import android.content.Context;
import android.hardware.SensorManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.squareup.seismic.ShakeDetector;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelImage;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DrawActivity extends AppCompatActivity implements ShakeDetector.Listener {

    private static final String TAG = "pixel_pop.DrawActivity";
    private DrawView drawView;
    private Gson gson;
    private ArrayList<Integer> colorList;
    private TextView displayTimer;
    private TextView memorizeTV;
    private ConstraintLayout drawPalette;
    private Handler handler = new Handler();
    private String adventure;
    private int levelNum;
    private int maxLevels;
    private SwitchMaterial shakeToEraseSwitch;
    private SensorManager sensorManager;
    private boolean shouldShake = false;
    private ShakeDetector shakeDetector;
    private int noCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        drawView = findViewById(R.id.drawView);
        displayTimer = findViewById(R.id.displayTimer);
        memorizeTV = findViewById(R.id.memorizeTV);
        drawPalette = findViewById(R.id.drawPalette);

        shakeToEraseSwitch = findViewById(R.id.switchShakeErase);
        shakeToEraseSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            shouldShake = isChecked;
            if (shouldShake) {
                Toast.makeText(this, "Shake to reset enabled", Toast.LENGTH_SHORT).show();
                shakeDetector = new ShakeDetector(this);
                sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
                shakeDetector.start(sensorManager, SensorManager.SENSOR_DELAY_GAME);
            } else {
                Toast.makeText(this, "Shake to reset disabled", Toast.LENGTH_SHORT).show();
                if (shakeDetector != null) {
                    shakeDetector.stop();
                }
            }
        });

        Button quitBtn = findViewById(R.id.quitBtn);
        quitBtn.setOnClickListener(v -> createQuitAlertDialog());

        Button whiteColorBtn = findViewById(R.id.whiteColorBtn);
        whiteColorBtn.setOnClickListener(v -> drawView.changeFillColor(getColor(R.color.white)));

        ImageButton eraserBtn = findViewById(R.id.eraserBtn);
        eraserBtn.setOnClickListener(v -> drawView.changeFillColor(getColor(R.color.white)));

        Button resetBtn = findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(v -> createResetAlertDialog());

        Button doneBtn = findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(v -> handleDone());

        gson = new Gson();

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            adventure = extras.getString("adventure");
            levelNum = extras.getInt("levelNum");
            maxLevels = extras.getInt("maxLevels");
            colorList = extras.getIntegerArrayList("colorList");

            setPixelImageProperties();

            /*
            Button logBtn = findViewById(R.id.logBtn);
            logBtn.setOnClickListener(v -> {
                PixelImage currentImage = new PixelImage(adventure, levelNum, 30, 600, drawView.getPixelCellsDisplay());
                String jsonToLog =  gson.toJson(currentImage);
                int chunkCount = jsonToLog.length() / 4000;
                for (int i = 0; i <= chunkCount; i++) {
                    int max = 4000 * (i + 1);
                    if (max >= jsonToLog.length()) {
                        Log.v("", jsonToLog.substring(4000 * i));
                    } else {
                        Log.v("", jsonToLog.substring(4000 * i, max));
                    }
                }
            });
            */
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shouldShake) {
            shakeDetector = new ShakeDetector(this);
            sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
            shakeDetector.start(sensorManager, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (shakeDetector != null) {
            shakeDetector.stop();
        }
    }

    @Override
    public void hearShake() {
        if (shakeDetector != null) {
            shakeDetector.stop();
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this)
                .setOnCancelListener(d -> {
                    shakeDetector = new ShakeDetector(this);
                    sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
                    shakeDetector.start(sensorManager, SensorManager.SENSOR_DELAY_GAME);
                });
        final View ShakeResetPopupView = getLayoutInflater().inflate(R.layout.reset_popup, null);
        Button noBtn = ShakeResetPopupView.findViewById(R.id.noBtn);
        Button yesBtn = ShakeResetPopupView.findViewById(R.id.yesBtn);
        dialogBuilder.setView(ShakeResetPopupView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        noBtn.setOnClickListener(v -> {
            dialog.dismiss();
            // Add to the no count
            noCount++;
            if (noCount == 3) {
                // Reset noCount
                noCount = 0;
                shouldShake = false;
                shakeToEraseSwitch.setChecked(false);
                // If the shake detector is active, stop it
                if (shakeDetector != null) {
                    shakeDetector.stop();
                }

                showSimpleDialog();
            } else {
                shakeDetector = new ShakeDetector(this);
                sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
                shakeDetector.start(sensorManager, SensorManager.SENSOR_DELAY_GAME);
            }
        });
        yesBtn.setOnClickListener(v -> {
            dialog.dismiss();
            drawView.resetFills();
            shakeDetector = new ShakeDetector(this);
            sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
            shakeDetector.start(sensorManager, SensorManager.SENSOR_DELAY_GAME);

            // A yes should reset the no count
            noCount = 0;
        });
    }

    private void  showSimpleDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View ShakeDisabledPopupView = getLayoutInflater().inflate(R.layout.shake_disabled_popup, null);
        Button okBtn = ShakeDisabledPopupView.findViewById(R.id.okBtn);
        dialogBuilder.setView(ShakeDisabledPopupView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        okBtn.setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void onBackPressed() {
        createQuitAlertDialog();
    }

    private void setPixelImageProperties() {
        Button colorBtn1 = findViewById(R.id.colorBtn1);
        Button colorBtn2 = findViewById(R.id.colorBtn2);
        Button colorBtn3 = findViewById(R.id.colorBtn3);
        Button colorBtn4 = findViewById(R.id.colorBtn4);
        Button colorBtn5 = findViewById(R.id.colorBtn5);
        Button colorBtn6 = findViewById(R.id.colorBtn6);
        Button colorBtn7 = findViewById(R.id.colorBtn7);
        Button colorBtn8 = findViewById(R.id.colorBtn8);

        colorBtn1.getBackground().setTint(colorList.get(0));
        colorBtn2.getBackground().setTint(colorList.get(1));
        colorBtn3.getBackground().setTint(colorList.get(2));
        colorBtn4.getBackground().setTint(colorList.get(3));
        colorBtn5.getBackground().setTint(colorList.get(4));
        colorBtn6.getBackground().setTint(colorList.get(5));
        colorBtn7.getBackground().setTint(colorList.get(6));
        colorBtn8.getBackground().setTint(colorList.get(7));

        colorBtn1.setOnClickListener(v -> drawView.changeFillColor(colorList.get(0)));
        colorBtn2.setOnClickListener(v -> drawView.changeFillColor(colorList.get(1)));
        colorBtn3.setOnClickListener(v -> drawView.changeFillColor(colorList.get(2)));
        colorBtn4.setOnClickListener(v -> drawView.changeFillColor(colorList.get(3)));
        colorBtn5.setOnClickListener(v -> drawView.changeFillColor(colorList.get(4)));
        colorBtn6.setOnClickListener(v -> drawView.changeFillColor(colorList.get(5)));
        colorBtn7.setOnClickListener(v -> drawView.changeFillColor(colorList.get(6)));
        colorBtn8.setOnClickListener(v -> drawView.changeFillColor(colorList.get(7)));

        List<PixelImage> pixelImages = loadPixelImagesFromFile("pixelImages.json");
        PixelImage imageToDisplay = pixelImages.stream().filter(pixelImage -> pixelImage.getAdventure().equalsIgnoreCase(adventure) && pixelImage.getLevelNum() == levelNum).findFirst().orElse(null);
        if (imageToDisplay != null) {
            runOnUiThread(() -> shakeToEraseSwitch.setVisibility(View.INVISIBLE));
            memorizeTV.setVisibility(View.VISIBLE);
            drawPalette.setVisibility(View.INVISIBLE);
            drawView.setIsEditable(false);
            drawView.setPixelCellsDisplay(imageToDisplay.getPixelCellsDisplay());
            displayTimer.setText("" + imageToDisplay.getDisplaySecondsTimer());
            DisplayTimerThread displayTimerThread = new DisplayTimerThread(imageToDisplay.getDisplaySecondsTimer());
            displayTimerThread.start();
        }
    }

    private void createResetAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View ResetPopupView = getLayoutInflater().inflate(R.layout.reset_popup, null);
        Button noBtn = ResetPopupView.findViewById(R.id.noBtn);
        Button yesBtn = ResetPopupView.findViewById(R.id.yesBtn);
        dialogBuilder.setView(ResetPopupView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        noBtn.setOnClickListener(v -> dialog.dismiss());
        yesBtn.setOnClickListener(v -> {
            dialog.dismiss();
            drawView.resetFills();
        });
    }

    private void handleDone() {
        Intent intent = new Intent (getApplicationContext(), ResultsActivity.class);

        Bundle extras = new Bundle();
        extras.putString("adventure", adventure);
        extras.putInt("levelNum", levelNum);
        extras.putInt("maxLevels", maxLevels);
        extras.putIntegerArrayList("colorList", colorList);
        extras.putParcelableArrayList("originalPixels", new ArrayList<>(drawView.getPixelCellsDisplay()));
        extras.putParcelableArrayList("drawnPixels", new ArrayList<>(drawView.getPixelCellsState()));
        intent.putExtras(extras);

        startActivity(intent);
        finish();
    }

    private List<PixelImage> loadPixelImagesFromFile(String fileName) {
        try {
            InputStreamReader isr = new InputStreamReader(getAssets().open(fileName));
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();
            List<PixelImage> pixelImages = gson.fromJson(json, new TypeToken<List<PixelImage>>(){}.getType());

            br.close();
            isr.close();

            return pixelImages;
        } catch (IOException e) {
            String msg = "Error loading pixel images from file";
            Log.e("DrawActivity", msg, e);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        }
    }

    private void createQuitAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View QuitPopupView = getLayoutInflater().inflate(R.layout.quit_level_popup, null);
        Button cancelBtn = QuitPopupView.findViewById(R.id.cancelBtn);
        Button quitBtn = QuitPopupView.findViewById(R.id.quitBtn);
        dialogBuilder.setView(QuitPopupView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        cancelBtn.setOnClickListener(v -> dialog.dismiss());
        quitBtn.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });
    }

    private class DisplayTimerThread extends Thread {
        private int displaySecondsTimer;

        public DisplayTimerThread(int displaySecondsTimer) {
            this.displaySecondsTimer = displaySecondsTimer;
        }

        @Override
        public void run() {
            for (int i = 0; i < displaySecondsTimer; i++) {
                try {
                    Thread.sleep(1000);
                    int currentTime = displaySecondsTimer - i - 1;
                    handler.post(() -> {
                        if (currentTime <= 10) {
                            displayTimer.setTextColor(Color.RED);
                        }
                        displayTimer.setText("" + currentTime);
                    });

                    // Change to draw mode
                    if (currentTime == 0) {
                        drawView.resetFills();
                        drawView.setIsEditable(true);
                        handler.post(() -> {
                            displayTimer.setTextColor(Color.BLACK);
                            displayTimer.setText("Draw");
                            memorizeTV.setVisibility(View.INVISIBLE);
                            drawPalette.setVisibility(View.VISIBLE);
                            shakeToEraseSwitch.setVisibility(View.VISIBLE);
                        });
                    }
                } catch (InterruptedException e) {
                    Log.e(TAG, "InterruptedException");
                }
            }
        }
    }
}

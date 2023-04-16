package edu.northeastern.numad23sp_team26.pixel_pop;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelImage;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DrawActivity extends AppCompatActivity {

    private static final String TAG = "pixel_pop.DrawActivity";
    private DrawView drawView;
    private Gson gson;
    private ArrayList<Integer> colorList;
    private TextView displayTimer;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        drawView = findViewById(R.id.drawView);

        Button resetBtn = findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(v -> drawView.resetFills());

        Button whiteColorBtn = findViewById(R.id.whiteColorBtn);
        whiteColorBtn.setOnClickListener(v -> drawView.changeFillColor(getColor(R.color.white)));

        ImageButton eraserBtn = findViewById(R.id.eraserBtn);
        eraserBtn.setOnClickListener(v -> drawView.changeFillColor(getColor(R.color.white)));

        displayTimer = findViewById(R.id.displayTimer);

        gson = new Gson();

        if (getIntent().getExtras() != null) {
            setPixelImageProperties();

            /*
            Bundle extras = getIntent().getExtras();
            String adventure = extras.getString("adventure");
            int levelNum = extras.getInt("levelNum");

            Button logBtn = findViewById(R.id.logBtn);
            logBtn.setOnClickListener(v -> {
                PixelImage currentImage = new PixelImage(adventure, levelNum, 600, 600, drawView.getPixelCellsDisplay());
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

    private void setPixelImageProperties() {
        Bundle extras = getIntent().getExtras();
        String adventure = extras.getString("adventure");
        int levelNum = extras.getInt("levelNum");
        colorList = extras.getIntegerArrayList("colorList");

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
            drawView.setPixelCellsDisplay(imageToDisplay.getPixelCellsDisplay());
            displayTimer.setText("" + imageToDisplay.getDisplaySecondsTimer());
            DisplayTimerThread displayTimerThread = new DisplayTimerThread(imageToDisplay.getDisplaySecondsTimer());
            displayTimerThread.start();
        }
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
                    if (currentTime == 0) {
                        // TODO: empty the draw canvas
                    }
                } catch (InterruptedException e) {
                    Log.e(TAG, "InterruptedException");
                }
            }
        }
    }
}

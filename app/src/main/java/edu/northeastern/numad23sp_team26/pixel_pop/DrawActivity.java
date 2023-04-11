package edu.northeastern.numad23sp_team26.pixel_pop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelImage;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DrawActivity extends AppCompatActivity {

    private DrawView drawView;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        drawView = findViewById(R.id.drawView);

        Button resetBtn = findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(v -> drawView.resetFills());

        gson = new Gson();

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            String adventure = extras.getString("adventure");
            int levelNum = extras.getInt("levelNum");

            Button logBtn = findViewById(R.id.logBtn);
            logBtn.setOnClickListener(v -> {
                PixelImage currentImage = new PixelImage(adventure, levelNum, 600, 600, drawView.getPixelCellsState());
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

            List<PixelImage> pixelImages = loadPixelImagesFromFile("pixelImages.json");
            PixelImage imageToDisplay = pixelImages.stream().filter(pixelImage -> pixelImage.getAdventure().equalsIgnoreCase(adventure) && pixelImage.getLevelNum() == levelNum).findFirst().orElse(null);
            if (imageToDisplay != null) {
                drawView.autoDrawPixelCells(imageToDisplay.getPixelCells());
            }
        }
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
}

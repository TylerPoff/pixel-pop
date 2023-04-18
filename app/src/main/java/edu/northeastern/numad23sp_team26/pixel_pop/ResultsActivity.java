package edu.northeastern.numad23sp_team26.pixel_pop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelCellDisplay;

public class ResultsActivity extends AppCompatActivity {

    private TextView levelName;
    private DrawView originalDrawView;
    private DrawView userDrawView;
    private String adventure;
    private int levelNum;
    private int maxLevels;
    private ArrayList<Integer> colorList;
    private List<PixelCellDisplay> originalPixels;
    private List<PixelCellDisplay> drawnPixels;
    private TextView score;
    private Button retryBtn;
    private Button nextLevelBtn;
    private Button levelMenuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        levelName = findViewById(R.id.levelName);
        originalDrawView = findViewById(R.id.originalDrawingImg);
        userDrawView = findViewById(R.id.yourDrawingImg);
        score = findViewById(R.id.score);
        retryBtn = findViewById(R.id.retryBtn);
        nextLevelBtn = findViewById(R.id.nextLevelBtn);
        levelMenuBtn = findViewById(R.id.menuBtn);

        levelMenuBtn.setOnClickListener(v -> finish());

        originalDrawView.setIsEditable(false);
        userDrawView.setIsEditable(false);

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            adventure = extras.getString("adventure");
            levelNum = extras.getInt("levelNum");
            maxLevels = extras.getInt("maxLevels");
            colorList = extras.getIntegerArrayList("colorList");
            originalPixels = extras.getParcelableArrayList("originalPixels");
            drawnPixels = extras.getParcelableArrayList("drawnPixels");

            levelName.setText(getString(R.string.level_num, levelNum));
            originalDrawView.setPixelCellsDisplay(originalPixels);
            userDrawView.setPixelCellsDisplay(drawnPixels);

            if (levelNum < maxLevels) {
                nextLevelBtn.setOnClickListener(v -> handleNextLevel());
            } else {
                nextLevelBtn.setEnabled(false);
            }

            // TODO: save score to database
            if (originalPixels.size() == drawnPixels.size()) {
                int whole = originalPixels.size();
                int part = 0;
                for (PixelCellDisplay c : originalPixels) {
                    PixelCellDisplay drawnPixel = drawnPixels.stream().filter(item -> item.getRowNum() == c.getRowNum() && item.getColNum() == c.getColNum()).findFirst().orElse(null);
                    if (c.equals(drawnPixel) && c.getColor() != getColor(R.color.white)) {
                        part++;
                    }
                }
                float percent = ((float)part / whole) * 100;
                int percent_rounded = Math.round(percent);
                score.setText(getString(R.string.accuracy, String.valueOf(percent_rounded) + "%"));
            } else {
                score.setText(getString(R.string.accuracy, ""));
            }

            retryBtn.setOnClickListener(v -> handleRetry());
        }
    }

    private void handleRetry() {
        Intent intent = new Intent(this, DrawActivity.class);

        Bundle extras = new Bundle();
        extras.putString("adventure", adventure);
        extras.putInt("levelNum", levelNum);
        extras.putInt("maxLevels", maxLevels);
        extras.putIntegerArrayList("colorList", colorList);
        intent.putExtras(extras);

        startActivity(intent);
        finish();
    }

    private void handleNextLevel() {
        if (levelNum < maxLevels) {
            Intent intent = new Intent(this, DrawActivity.class);

            Bundle extras = new Bundle();
            extras.putString("adventure", adventure);
            extras.putInt("levelNum", levelNum + 1);
            extras.putInt("maxLevels", maxLevels);
            extras.putIntegerArrayList("colorList", colorList);
            intent.putExtras(extras);

            startActivity(intent);
            finish();
        }
    }
}
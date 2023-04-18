package edu.northeastern.numad23sp_team26;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.northeastern.numad23sp_team26.pixel_pop.DrawActivity;
import edu.northeastern.numad23sp_team26.pixel_pop.DrawView;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelCellDisplay;

public class ResultsActivity extends AppCompatActivity {

    private TextView levelName;
    private DrawView originalDrawView;
    private DrawView userDrawView;
    private String adventure;
    private int levelNum;
    private ArrayList<Integer> colorList;
    private List<PixelCellDisplay> originalPixels;
    private List<PixelCellDisplay> drawnPixels;
    private TextView score;
    private Button retryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        levelName = findViewById(R.id.levelName);
        originalDrawView = findViewById(R.id.originalDrawingImg);
        userDrawView = findViewById(R.id.yourDrawingImg);
        score = findViewById(R.id.score);
        retryBtn = findViewById(R.id.retryBtn);

        originalDrawView.setIsEditable(false);
        userDrawView.setIsEditable(false);

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            adventure = extras.getString("adventure");
            levelNum = extras.getInt("levelNum");
            colorList = extras.getIntegerArrayList("colorList");
            originalPixels = extras.getParcelableArrayList("originalPixels");
            drawnPixels = extras.getParcelableArrayList("drawnPixels");

            levelName.setText(getString(R.string.level_num, levelNum));
            originalDrawView.setPixelCellsDisplay(originalPixels);
            userDrawView.setPixelCellsDisplay(drawnPixels);

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
                double percent = ((double)part / whole) * 100;
                score.setText(getString(R.string.accuracy, String.format(Locale.ENGLISH, "%.2f%%", percent)));
            } else {
                score.setText(getString(R.string.accuracy, ""));
            }

            retryBtn.setOnClickListener(v -> handleRetry());
        }
    }

    private void handleRetry() {
        Intent intent = new Intent(this, DrawActivity.class);

        Bundle extras = new Bundle();
        extras.putString("adventure", "animals");
        extras.putInt("levelNum", levelNum);
        extras.putIntegerArrayList("colorList", colorList);
        intent.putExtras(extras);

        startActivity(intent);
        finish();
    }
}
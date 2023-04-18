package edu.northeastern.numad23sp_team26.pixel_pop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelCellDisplay;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelPopUser;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelScore;

public class ResultsActivity extends AppCompatActivity {

    private static final String TAG = "pixel_pop.ResultsActivity";
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
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;

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

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();

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
                score.setText(getString(R.string.accuracy, percent_rounded + "%"));
                saveScore(percent_rounded);
            } else {
                score.setText(getString(R.string.accuracy, ""));
            }

            retryBtn.setOnClickListener(v -> handleRetry());
        }
    }

    private void saveScore(int accuracyPercent) {
        String uid = mAuth.getCurrentUser().getUid();
        databaseRef.child("Users").child(uid).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                PixelPopUser u = currentData.getValue(PixelPopUser.class);
                if (u == null) {
                    return Transaction.success(currentData);
                }

                if (u.pixelScoreList == null) {
                    u.pixelScoreList = new ArrayList<>();
                }

                u.pixelScoreList.add(new PixelScore(accuracyPercent, LocalDateTime.now().toString()));
                currentData.setValue(u);

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                Log.d(TAG, "postTransaction:onComplete:" + error);
            }
        });
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
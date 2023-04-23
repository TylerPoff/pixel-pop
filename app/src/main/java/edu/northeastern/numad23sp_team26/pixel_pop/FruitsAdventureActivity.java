package edu.northeastern.numad23sp_team26.pixel_pop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad23sp_team26.R;

public class FruitsAdventureActivity extends AdventureActivity {

    private Button fruit_button1, fruit_button2, fruit_button3, fruit_button4, fruit_button5;
    private final String ADVENTURE_TYPE = "fruits";
    private final int MAX_LEVELS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruits_adventure);

        fruit_button1 = findViewById(R.id.fruit_button1);
        fruit_button1.setOnClickListener(v -> {
            if (multiPlayGameID.isEmpty()) {
                createAlertDialog(1, ADVENTURE_TYPE);
            } else {
                createMultiAlertDialog(1, ADVENTURE_TYPE);
            }
        });

        fruit_button2 = findViewById(R.id.fruit_button2);
        fruit_button2.setOnClickListener(v -> {
            if (multiPlayGameID.isEmpty()) {
                createAlertDialog(2, ADVENTURE_TYPE);
            } else {
                createMultiAlertDialog(2, ADVENTURE_TYPE);
            }
        });

        fruit_button3 = findViewById(R.id.fruit_button3);
        fruit_button3.setOnClickListener(v -> {
            if (multiPlayGameID.isEmpty()) {
                createAlertDialog(3, ADVENTURE_TYPE);
            } else {
                createMultiAlertDialog(3, ADVENTURE_TYPE);
            }
        });

        fruit_button4 = findViewById(R.id.fruit_button4);
        fruit_button4.setOnClickListener(v -> {
            if (multiPlayGameID.isEmpty()) {
                createAlertDialog(4, ADVENTURE_TYPE);
            } else {
                createMultiAlertDialog(4, ADVENTURE_TYPE);
            }
        });

        fruit_button5 = findViewById(R.id.fruit_button5);
        fruit_button5.setOnClickListener(v -> {
            if (multiPlayGameID.isEmpty()) {
                createAlertDialog(5, ADVENTURE_TYPE);
            } else {
                createMultiAlertDialog(5, ADVENTURE_TYPE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (multiPlayGameID.isEmpty()) {
            getUnlockedLevels(ADVENTURE_TYPE);
        } else {
            getMultiPlayUnlockedLevels(ADVENTURE_TYPE);
        }
    }

    @Override
    public void openActivityPixelDraw(int levelNum, PlayMode playMode) {
        ArrayList<Integer> colorList = new ArrayList<>();
        colorList.add(getColor(R.color.black));
        colorList.add(getColor(R.color.red));
        colorList.add(getColor(R.color.nature_green));
        colorList.add(getColor(R.color.blue));
        colorList.add(getColor(R.color.yellow));
        colorList.add(getColor(R.color.fruit_orange));
        colorList.add(getColor(R.color.purple_500));
        colorList.add(getColor(R.color.brown));

        Intent intent = new Intent(this, DrawActivity.class);

        Bundle extras = new Bundle();
        extras.putString("adventure", ADVENTURE_TYPE);
        extras.putInt("levelNum", levelNum);
        extras.putInt("maxLevels", MAX_LEVELS);
        extras.putIntegerArrayList("colorList", colorList);
        extras.putString("playMode", playMode.toString());
        extras.putString("playerNum", "p1");
        extras.putString("gameID", multiPlayGameID);
        intent.putExtras(extras);

        startActivity(intent);
    }

    @Override
    public void updateUnlockLevels(List<Integer> unlockLevels) {
        for (int l : unlockLevels) {
            switch (l) {
                case 1:
                    fruit_button1.setEnabled(true);
                    break;
                case 2:
                    fruit_button2.setEnabled(true);
                    break;
                case 3:
                    fruit_button3.setEnabled(true);
                    break;
                case 4:
                    fruit_button4.setEnabled(true);
                    break;
                case 5:
                    fruit_button5.setEnabled(true);
                    break;
            }
        }
    }
}
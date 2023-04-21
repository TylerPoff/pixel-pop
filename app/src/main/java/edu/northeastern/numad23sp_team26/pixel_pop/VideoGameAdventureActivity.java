package edu.northeastern.numad23sp_team26.pixel_pop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad23sp_team26.R;

public class VideoGameAdventureActivity extends AdventureActivity {

    private Button button1, button2, button3, button4, button5;
    private final String ADVENTURE_TYPE = "video game";
    private final int MAX_LEVELS = 5;
    private String multiPlayGameID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_game_adventure);

        TextView gameIdTV = findViewById(R.id.gameIdTV);

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            multiPlayGameID = extras.getString("gameID");

            if (multiPlayGameID.isEmpty()) {
                gameIdTV.setVisibility(View.INVISIBLE);
            } else {
                gameIdTV.setText(getString(R.string.game_id, multiPlayGameID));
                createGameIdDialog(multiPlayGameID);
            }
        }

        button1 = findViewById(R.id.button1);
        button1.setOnClickListener(v -> createAlertDialog(1, ADVENTURE_TYPE));

        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(v -> createAlertDialog(2, ADVENTURE_TYPE));

        button3 = findViewById(R.id.button3);
        button3.setOnClickListener(v -> createAlertDialog(3, ADVENTURE_TYPE));

        button4 = findViewById(R.id.button4);
        button4.setOnClickListener(v -> createAlertDialog(4, ADVENTURE_TYPE));

        button5 = findViewById(R.id.button5);
        button5.setOnClickListener(v -> createAlertDialog(5, ADVENTURE_TYPE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUnlockedLevels("video game");
    }

    @Override
    public void openActivityPixelDraw(int levelNum) {
        ArrayList<Integer> colorList = new ArrayList<>();
        colorList.add(getColor(R.color.black));
        colorList.add(getColor(R.color.red));
        colorList.add(getColor(R.color.green));
        colorList.add(getColor(R.color.blue));
        colorList.add(getColor(R.color.yellow));
        colorList.add(getColor(R.color.orange));
        colorList.add(getColor(R.color.maple));
        colorList.add(getColor(R.color.brown));

        Intent intent = new Intent(this, DrawActivity.class);

        Bundle extras = new Bundle();
        extras.putString("adventure", ADVENTURE_TYPE);
        extras.putInt("levelNum", levelNum);
        extras.putInt("maxLevels", MAX_LEVELS);
        extras.putIntegerArrayList("colorList", colorList);
        intent.putExtras(extras);

        startActivity(intent);
    }

    @Override
    public void updateUnlockLevels(List<Integer> unlockLevels) {
        for (int l : unlockLevels) {
            switch (l) {
                case 1:
                    button1.setEnabled(true);
                    break;
                case 2:
                    button2.setEnabled(true);
                    break;
                case 3:
                    button3.setEnabled(true);
                    break;
                case 4:
                    button4.setEnabled(true);
                    break;
                case 5:
                    button5.setEnabled(true);
                    break;
            }
        }
    }
}
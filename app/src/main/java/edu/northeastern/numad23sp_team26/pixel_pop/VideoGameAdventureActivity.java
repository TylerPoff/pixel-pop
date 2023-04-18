package edu.northeastern.numad23sp_team26.pixel_pop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu.northeastern.numad23sp_team26.R;

public class VideoGameAdventureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_game_adventure);

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(v -> createAlertDialog(1));

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(v -> createAlertDialog(2));

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(v -> createAlertDialog(3));

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(v -> createAlertDialog(4));

        Button button5 = findViewById(R.id.button5);
        button5.setOnClickListener(v -> createAlertDialog(5));
    }

    public void createAlertDialog(int levelNum){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View AdventurePopupView = getLayoutInflater().inflate(R.layout.adventure_popup, null);
        TextView adventure_level_txt = (TextView) AdventurePopupView.findViewById(R.id.adventure_popup_level_txt);
        TextView three_top_scores_txt = (TextView) AdventurePopupView.findViewById(R.id.adventure_popup_top_scores_txt2);
        //TODO: populate the top scores with real user data once it is available
        Button back_btn = (Button) AdventurePopupView.findViewById(R.id.adventure_popup_back_btn);
        Button start_btn = (Button) AdventurePopupView.findViewById(R.id.adventure_popup_start_btn);
        dialogBuilder.setView(AdventurePopupView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        adventure_level_txt.setText("LEVEL "+levelNum);
        back_btn.setOnClickListener(v -> dialog.dismiss());
        start_btn.setOnClickListener(v -> openActivityPixelDraw(levelNum));
    }

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
        extras.putString("adventure", "video game");
        extras.putInt("levelNum", levelNum);
        extras.putIntegerArrayList("colorList", colorList);
        intent.putExtras(extras);

        startActivity(intent);
    }
}
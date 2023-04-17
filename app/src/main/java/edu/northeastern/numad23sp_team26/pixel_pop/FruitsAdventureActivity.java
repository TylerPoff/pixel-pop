package edu.northeastern.numad23sp_team26.pixel_pop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import edu.northeastern.numad23sp_team26.R;

public class FruitsAdventureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruits_adventure);

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(v -> openActivityPixelDraw(1));

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(v -> openActivityPixelDraw(2));

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(v -> openActivityPixelDraw(3));

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(v -> openActivityPixelDraw(4));

        Button button5 = findViewById(R.id.button5);
        button5.setOnClickListener(v -> openActivityPixelDraw(5));
    }

    public void openActivityPixelDraw(int levelNum) {
        ArrayList<Integer> colorList = new ArrayList<>();
        colorList.add(getColor(R.color.black));
        colorList.add(getColor(R.color.red));
        colorList.add(getColor(R.color.nature_green));
        colorList.add(getColor(R.color.blue));
        colorList.add(getColor(R.color.yellow));
        colorList.add(getColor(R.color.orange));
        colorList.add(getColor(R.color.purple_500));
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
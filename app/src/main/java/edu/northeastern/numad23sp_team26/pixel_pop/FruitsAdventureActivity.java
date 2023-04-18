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

        Button fruit_button1 = findViewById(R.id.fruit_button1);
        fruit_button1.setOnClickListener(v -> openActivityPixelDraw(1));

        Button fruit_button2 = findViewById(R.id.fruit_button2);
        fruit_button2.setOnClickListener(v -> openActivityPixelDraw(2));

        Button fruit_button3 = findViewById(R.id.fruit_button3);
        fruit_button3.setOnClickListener(v -> openActivityPixelDraw(3));

        Button fruit_button4 = findViewById(R.id.fruit_button4);
        fruit_button4.setOnClickListener(v -> openActivityPixelDraw(4));

        Button fruit_button5 = findViewById(R.id.fruit_button5);
        fruit_button5.setOnClickListener(v -> openActivityPixelDraw(5));
    }

    public void openActivityPixelDraw(int levelNum) {
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
        extras.putString("adventure", "fruits");
        extras.putInt("levelNum", levelNum);
        extras.putIntegerArrayList("colorList", colorList);
        intent.putExtras(extras);

        startActivity(intent);
    }
}
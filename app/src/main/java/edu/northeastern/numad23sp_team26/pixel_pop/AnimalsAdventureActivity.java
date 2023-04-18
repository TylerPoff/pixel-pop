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

public class AnimalsAdventureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_adventure);

        Button animals_pixel_drawing_1_button = findViewById(R.id.animals_pixel_drawing_1_button);
        animals_pixel_drawing_1_button.setOnClickListener(v -> createAlertDialog(1));

        Button animals_pixel_drawing_2_button = findViewById(R.id.animals_pixel_drawing_2_button);
        animals_pixel_drawing_2_button.setOnClickListener(v -> createAlertDialog(2));

        Button animals_pixel_drawing_3_button = findViewById(R.id.animals_pixel_drawing_3_button);
        animals_pixel_drawing_3_button.setOnClickListener(v -> createAlertDialog(3));

        Button animals_pixel_drawing_4_button = findViewById(R.id.animals_pixel_drawing_4_button);
        animals_pixel_drawing_4_button.setOnClickListener(v -> createAlertDialog(4));

        Button animals_pixel_drawing_5_button = findViewById(R.id.animals_pixel_drawing_5_button);
        animals_pixel_drawing_5_button.setOnClickListener(v -> createAlertDialog(5));
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
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        adventure_level_txt.setText("LEVEL "+levelNum);
        back_btn.setOnClickListener(v -> dialog.dismiss());
        start_btn.setOnClickListener(v -> {
            dialog.dismiss();
            openActivityPixelDraw(levelNum);
        });
    }

    public void openActivityPixelDraw(int levelNum) {
        ArrayList<Integer> colorList = new ArrayList<>();
        colorList.add(getColor(R.color.black));
        colorList.add(getColor(R.color.red));
        colorList.add(getColor(R.color.green));
        colorList.add(getColor(R.color.maple));
        colorList.add(getColor(R.color.yellow));
        colorList.add(getColor(R.color.orange));
        colorList.add(getColor(R.color.cheese));
        colorList.add(getColor(R.color.brown));

        Intent intent = new Intent(this, DrawActivity.class);

        Bundle extras = new Bundle();
        extras.putString("adventure", "animals");
        extras.putInt("levelNum", levelNum);
        extras.putInt("maxLevels", 5);
        extras.putIntegerArrayList("colorList", colorList);
        intent.putExtras(extras);

        startActivity(intent);
    }
}
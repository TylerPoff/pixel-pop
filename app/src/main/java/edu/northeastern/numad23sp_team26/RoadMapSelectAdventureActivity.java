package edu.northeastern.numad23sp_team26;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class RoadMapSelectAdventureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_map_select_adventure);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select Adventure");
        arrayList.add("Animals");
        arrayList.add("Beach");
        arrayList.add("Flowers");
        arrayList.add("Plants");
        arrayList.add("Sea");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String adventure = parent.getItemAtPosition(position).toString();
                switch (adventure) {
                    case "Animals":
                        openActivityAnimals();
                        break;
                    case "Beach":
                        openActivityBeach();
                        break;
                    case "Flowers":
                        openActivityFlowers();
                        break;
                    case "Plants":
                        openActivityPlants();
                        break;
                    case "Sea":
                        openActivitySea();
                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
    }

    public void openActivityAnimals() {
        Intent intent = new Intent(this, RoadMapAnimalsActivity.class);
        startActivity(intent);
    }
    public void openActivityBeach() {
        Intent intent = new Intent(this, RoadMapBeachActivity.class);
        startActivity(intent);
    }
    public void openActivityFlowers() {
        Intent intent = new Intent(this, RoadMapFlowersActivity.class);
        startActivity(intent);
    }
    public void openActivityPlants() {
        Intent intent = new Intent(this, RoadMapPlantsActivity.class);
        startActivity(intent);
    }
    public void openActivitySea() {
        Intent intent = new Intent(this, RoadMapSeaActivity.class);
        startActivity(intent);
    }


}
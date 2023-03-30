package edu.northeastern.numad23sp_team26;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;

public class MapLevelsActivity extends AppCompatActivity {

    private ArrayList<Integer> levels = new ArrayList<>(Arrays.asList(1,2,3,4,5));

    private RecyclerView.LayoutManager layoutManager;
    private LevelButtonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_levels);
        RecyclerView recyclerView = findViewById(R.id.levelGrid);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LevelButtonAdapter(levels, this);
        recyclerView.setAdapter(adapter);
    }

}
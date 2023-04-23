package edu.northeastern.numad23sp_team26.pixel_pop;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelMultiGame;

public class WaitJoinMultiplayerActivity extends MultiPlayCommonActivity {

    private final int MAX_LEVELS = 5;
    private ChildEventListener multiPlayGamesStartChildListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            PixelMultiGame multiGame = snapshot.getValue(PixelMultiGame.class);
            if (multiGame != null && multiGame.gameID.equalsIgnoreCase(multiPlayGameID)) {
                if (!multiGame.adventure.isEmpty() && multiGame.levelNum != 0 && multiGame.pixelCellsState == null) {
                    isPlaying = true;
                    openDrawActivity(multiGame.adventure, multiGame.levelNum);
                }
            }
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            PixelMultiGame multiGame = snapshot.getValue(PixelMultiGame.class);
            if (multiGame != null && multiGame.gameID.equalsIgnoreCase(multiPlayGameID)) {
                createDisconnectedAlertDialog();
            }
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_join_multiplayer);
    }

    @Override
    protected void onResume() {
        super.onResume();

        isPlaying = false;

        if (multiPlayGameID.isEmpty()) {
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseRef.child("MultiplayerGames").removeEventListener(multiPlayGamesStartChildListener);
    }

    @Override
    protected void multiPlayGamesListener() {
        databaseRef.child("MultiplayerGames").addChildEventListener(multiPlayGamesStartChildListener);
    }

    private void openDrawActivity(String adventure, int levelNum) {
        ArrayList<Integer> colorList = new ArrayList<>();
        switch (adventure) {
            case "animals":
                colorList.add(getColor(R.color.black));
                colorList.add(getColor(R.color.red));
                colorList.add(getColor(R.color.nature_green));
                colorList.add(getColor(R.color.maple));
                colorList.add(getColor(R.color.yellow));
                colorList.add(getColor(R.color.orange));
                colorList.add(getColor(R.color.cheese));
                colorList.add(getColor(R.color.brown));
                break;
            case "fruits":
                colorList.add(getColor(R.color.black));
                colorList.add(getColor(R.color.red));
                colorList.add(getColor(R.color.nature_green));
                colorList.add(getColor(R.color.blue));
                colorList.add(getColor(R.color.yellow));
                colorList.add(getColor(R.color.fruit_orange));
                colorList.add(getColor(R.color.purple_500));
                colorList.add(getColor(R.color.brown));
                break;
            case "video game":
                colorList.add(getColor(R.color.black));
                colorList.add(getColor(R.color.red));
                colorList.add(getColor(R.color.green));
                colorList.add(getColor(R.color.blue));
                colorList.add(getColor(R.color.yellow));
                colorList.add(getColor(R.color.orange));
                colorList.add(getColor(R.color.maple));
                colorList.add(getColor(R.color.brown));
                break;
        }

        Intent intent = new Intent(this, DrawActivity.class);

        Bundle extras = new Bundle();
        extras.putString("adventure", adventure);
        extras.putInt("levelNum", levelNum);
        extras.putInt("maxLevels", MAX_LEVELS);
        extras.putIntegerArrayList("colorList", colorList);
        extras.putString("playMode", AdventureActivity.PlayMode.MULTI.toString());
        extras.putString("playerNum", "p2");
        extras.putString("gameID", multiPlayGameID);
        intent.putExtras(extras);

        startActivity(intent);
        finish();
    }
}

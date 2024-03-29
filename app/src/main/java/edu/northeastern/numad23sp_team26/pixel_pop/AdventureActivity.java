package edu.northeastern.numad23sp_team26.pixel_pop;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelMultiGame;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelMultiScore;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelPopUser;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelScore;

public abstract class AdventureActivity extends MultiPlayCommonActivity {

    private static final String TAG = "pixel_pop.AdventureActivity";
    private String playerTwoEmail;
    private View MultiAdventurePopupView;
    private TextView playTwoTxt;
    private ProgressBar progressBar;
    private Button multiPlayStartBtn;
    private FirebaseAuth mAuth;
    private AlertDialog gameIdDialog, adventureDialog, multiAdventureDialog;
    protected enum PlayMode {
        SINGLE,
        MULTI
    }
    private ChildEventListener playerTwoJoinChildListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            if (snapshot.getKey() != null && !snapshot.getKey().equalsIgnoreCase("playerTwoID")) {
                return;
            }
            String playerTwoID = snapshot.getValue(String.class);
            if (playerTwoID != null && !playerTwoID.isEmpty()) {
                databaseRef.child("Users").child(playerTwoID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        PixelPopUser u = snapshot.getValue(PixelPopUser.class);
                        if (u != null) {
                            playerTwoEmail = u.email;
                            Toast.makeText(AdventureActivity.this, "Player two (" + playerTwoEmail.split("@")[0] + ") joined", Toast.LENGTH_SHORT).show();

                            if (MultiAdventurePopupView != null) {
                                playTwoTxt.setText("Player Two: " + playerTwoEmail.split("@")[0]);
                                progressBar.setVisibility(View.INVISIBLE);
                                multiPlayStartBtn.setEnabled(true);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // onCancelled
                    }
                });
            }
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

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

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        isPlaying = false;

        TextView gameIdTV = findViewById(R.id.gameIdTV);
        if (multiPlayGameID.isEmpty()) {
            gameIdTV.setVisibility(View.INVISIBLE);
        } else {
            databaseRef.child("MultiplayerGames").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean foundGame = false;
                    if (!snapshot.hasChildren()) {
                        dismissAllDialogs();
                        finish();
                    }
                    for (DataSnapshot statusSnapshot: snapshot.getChildren()) {
                        PixelMultiGame status = statusSnapshot.getValue(PixelMultiGame.class);
                        if (status != null && status.gameID.equalsIgnoreCase(multiPlayGameID)) {
                            foundGame = true;
                            break;
                        }
                    }
                    if (!foundGame) {
                        dismissAllDialogs();
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            gameIdTV.setText(getString(R.string.game_id, multiPlayGameID));
            createGameIdDialog(multiPlayGameID);
            databaseRef.child("MultiplayerGames").child(multiPlayGameID).addChildEventListener(playerTwoJoinChildListener);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (!multiPlayGameID.isEmpty()) {
            databaseRef.child("MultiplayerGames").child(multiPlayGameID).removeEventListener(playerTwoJoinChildListener);
            dismissAllDialogs();
        }
        super.onPause();
    }

    public abstract void openActivityPixelDraw(int levelNum, PlayMode playMode);

    public abstract void updateUnlockLevels(List<Integer> unlockLevels);

    public void getUnlockedLevels(String adventure) {
        String uid = mAuth.getCurrentUser().getUid();

        databaseRef.child("Users").child(uid).get().addOnCompleteListener(task -> {
            List<Integer> unlockedLevels = new ArrayList<>();
            unlockedLevels.add(1);
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting firebase data", task.getException());
            }
            else {
                if (task.getResult().getValue() != null) {
                    PixelPopUser currentUser = task.getResult().getValue(PixelPopUser.class);

                    if (currentUser != null) {
                        List<PixelScore> pixelScoreList = currentUser.pixelScoreList;
                        if (pixelScoreList != null) {
                            List<PixelScore> pixelScoreListFiltered = pixelScoreList.stream().filter(p -> p.adventure.equalsIgnoreCase(adventure)).collect(Collectors.toList());
                            for (PixelScore ps : pixelScoreListFiltered) {
                                int nextLevel = ps.levelNum + 1;
                                if (!unlockedLevels.contains(nextLevel)) {
                                    unlockedLevels.add(nextLevel);
                                }
                            }
                        }
                    }
                }
            }
            updateUnlockLevels(unlockedLevels);
        });
    }

    protected void getMultiPlayUnlockedLevels(String adventure) {
        List<Integer> unlockedLevels = new ArrayList<>();
        unlockedLevels.add(1);
        unlockedLevels.add(2);
        unlockedLevels.add(3);
        unlockedLevels.add(4);
        unlockedLevels.add(5);
        updateUnlockLevels(unlockedLevels);
    }

    public void createGameIdDialog(String gameID) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View GameIdPopupView = getLayoutInflater().inflate(R.layout.host_multiplayer_popup, null);
        TextView gameIdTV = GameIdPopupView.findViewById(R.id.gameIdTV);
        gameIdTV.setText(getString(R.string.game_id, gameID));
        Button okBtn = GameIdPopupView.findViewById(R.id.okBtn);
        dialogBuilder.setView(GameIdPopupView);
        gameIdDialog = dialogBuilder.create();
        gameIdDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        gameIdDialog.show();
        okBtn.setOnClickListener(v -> gameIdDialog.dismiss());
    }

    public void createAlertDialog(int levelNum, String adventure) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View AdventurePopupView = getLayoutInflater().inflate(R.layout.adventure_popup, null);
        TextView adventure_level_txt = (TextView) AdventurePopupView.findViewById(R.id.adventure_popup_level_txt);
        TextView three_top_scores_txt = (TextView) AdventurePopupView.findViewById(R.id.adventure_popup_top_scores_txt2);

        String uid = mAuth.getCurrentUser().getUid();
        databaseRef.child("Users").child(uid).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting firebase data", task.getException());
            }
            else {
                if (task.getResult().getValue() != null) {
                    PixelPopUser currentUser = task.getResult().getValue(PixelPopUser.class);
                    if (currentUser != null) {
                        List<PixelScore> pixelScoreList = currentUser.pixelScoreList;
                        if (pixelScoreList != null) {
                            List<Integer> pixelScoreListFiltered = pixelScoreList
                                                                    .stream()
                                                                    .filter(p -> (p.adventure.equalsIgnoreCase(adventure) && p.levelNum==levelNum))
                                                                    .map(p -> p.accuracyPercent)
                                                                    .collect(Collectors.toList());
                            if (!pixelScoreListFiltered.isEmpty()) {
                                pixelScoreListFiltered.sort(Collections.reverseOrder());
                                pixelScoreListFiltered = pixelScoreListFiltered.subList(0, Math.min(pixelScoreListFiltered.size(), 3));
                                List<String> outputList = new ArrayList<>();
                                for (int i = 1; i <= pixelScoreListFiltered.size(); i++) {
                                    outputList.add(i + ")  " + pixelScoreListFiltered.get(i - 1) + "%");
                                }
                                three_top_scores_txt.setText(String.join("\n\n", outputList));
                            }
                        }
                    }
                }
            }
        });
        Button back_btn = (Button) AdventurePopupView.findViewById(R.id.adventure_popup_back_btn);
        Button start_btn = (Button) AdventurePopupView.findViewById(R.id.adventure_popup_start_btn);
        dialogBuilder.setView(AdventurePopupView);
        adventureDialog = dialogBuilder.create();
        adventureDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        adventureDialog.show();
        adventure_level_txt.setText(getString(R.string.level_num, levelNum));
        back_btn.setOnClickListener(v -> adventureDialog.dismiss());
        start_btn.setOnClickListener(v -> {
            adventureDialog.dismiss();
            openActivityPixelDraw(levelNum, PlayMode.SINGLE);
        });
    }

    public void createMultiAlertDialog(int levelNum, String adventure) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        MultiAdventurePopupView = getLayoutInflater().inflate(R.layout.multi_adventure_popup, null);
        playTwoTxt = (TextView) MultiAdventurePopupView.findViewById(R.id.adventure_popup_player_two_txt);
        progressBar = (ProgressBar) MultiAdventurePopupView.findViewById(R.id.progressBar);
        multiPlayStartBtn = (Button) MultiAdventurePopupView.findViewById(R.id.adventure_popup_start_btn);
        TextView adventure_level_txt = (TextView) MultiAdventurePopupView.findViewById(R.id.adventure_popup_level_txt);
        TextView three_top_scores_txt = (TextView) MultiAdventurePopupView.findViewById(R.id.adventure_popup_top_scores_txt2);
        Button back_btn = (Button) MultiAdventurePopupView.findViewById(R.id.adventure_popup_back_btn);

        String uid = mAuth.getCurrentUser().getUid();
        databaseRef.child("Users").child(uid).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting firebase data", task.getException());
            }
            else {
                if (task.getResult().getValue() != null) {
                    PixelPopUser currentUser = task.getResult().getValue(PixelPopUser.class);
                    if (currentUser != null) {
                        List<PixelMultiScore> pixelScoreList = currentUser.pixelMultiScoreList;
                        if (pixelScoreList != null) {
                            List<PixelMultiScore> pixelScoreListFiltered = pixelScoreList.stream()
                                    .filter(p -> (p.adventure.equalsIgnoreCase(adventure) && p.levelNum==levelNum))
                                    .collect(Collectors.toList());
                            if (!pixelScoreListFiltered.isEmpty()) {
                                pixelScoreListFiltered.stream().map(p -> p.accuracyPercent).collect(Collectors.toList()).sort(Collections.reverseOrder());
                                pixelScoreListFiltered = pixelScoreListFiltered.subList(0, Math.min(pixelScoreListFiltered.size(), 3));
                                List<String> outputList = new ArrayList<>();
                                for (int i = 1; i <= pixelScoreListFiltered.size(); i++) {
                                    outputList.add(i + ")  " + pixelScoreListFiltered.get(i - 1).accuracyPercent + "%"
                                            + " (Completed with: " + pixelScoreListFiltered.get(i - 1).otherPlayerEmail.split("@")[0] + ")");
                                }
                                three_top_scores_txt.setText(String.join("\n\n", outputList));
                            }
                        }
                    }
                }
            }
        });

        dialogBuilder.setView(MultiAdventurePopupView);
        multiAdventureDialog = dialogBuilder.create();
        multiAdventureDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        multiAdventureDialog.show();

        adventure_level_txt.setText(getString(R.string.level_num, levelNum));

        if (playerTwoEmail != null && !playerTwoEmail.isEmpty()) {
            playTwoTxt.setText("Player Two: " + playerTwoEmail.split("@")[0]);
            progressBar.setVisibility(View.INVISIBLE);
            multiPlayStartBtn.setEnabled(true);
        } else {
            multiPlayStartBtn.setEnabled(false);
        }

        back_btn.setOnClickListener(v -> multiAdventureDialog.dismiss());
        multiPlayStartBtn.setOnClickListener(v -> {
            multiAdventureDialog.dismiss();
            startMultiplayerGame(adventure, levelNum);
            openActivityPixelDraw(levelNum, PlayMode.MULTI);
        });
    }

    private void dismissAllDialogs() {
        if (gameIdDialog != null) {
            gameIdDialog.dismiss();
        }

        if (adventureDialog != null) {
            adventureDialog.dismiss();
        }

        if (multiAdventureDialog != null) {
            multiAdventureDialog.dismiss();
        }
    }
}

package edu.northeastern.numad23sp_team26.pixel_pop;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelPopUser;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelScore;

public abstract class AdventureActivity extends AppCompatActivity {

    private static final String TAG = "pixel_pop.AdventureActivity";

    public abstract void openActivityPixelDraw(int levelNum);

    public abstract void updateUnlockLevels(List<Integer> unlockLevels);

    public void getUnlockedLevels(String adventure) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

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

    public void createAlertDialog(int levelNum, String adventure) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View AdventurePopupView = getLayoutInflater().inflate(R.layout.adventure_popup, null);
        TextView adventure_level_txt = (TextView) AdventurePopupView.findViewById(R.id.adventure_popup_level_txt);
        TextView three_top_scores_txt = (TextView) AdventurePopupView.findViewById(R.id.adventure_popup_top_scores_txt2);
        //TODO: populate the top scores with real user data once it is available
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
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
                                                                    .filter(p -> (p.adventure.equalsIgnoreCase(adventure)&&p.levelNum==levelNum))
                                                                    .map(p -> p.accuracyPercent)
                                                                    .collect(Collectors.toList());
                            if(!pixelScoreListFiltered.isEmpty()) {
                                Collections.sort(pixelScoreListFiltered, Collections.reverseOrder());
                                pixelScoreListFiltered = pixelScoreListFiltered.subList(0,Math.min(pixelScoreListFiltered.size(),3));
                                three_top_scores_txt.setText("");
                                for (int i : pixelScoreListFiltered){
                                    three_top_scores_txt.append(String.valueOf(i)+" %\n");
                                }
                            }
                        }
                    }
                }
            }
        });
        Button back_btn = (Button) AdventurePopupView.findViewById(R.id.adventure_popup_back_btn);
        Button start_btn = (Button) AdventurePopupView.findViewById(R.id.adventure_popup_start_btn);
        dialogBuilder.setView(AdventurePopupView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        adventure_level_txt.setText("LEVEL " + levelNum);
        back_btn.setOnClickListener(v -> dialog.dismiss());
        start_btn.setOnClickListener(v -> {
            dialog.dismiss();
            openActivityPixelDraw(levelNum);
        });
    }
}

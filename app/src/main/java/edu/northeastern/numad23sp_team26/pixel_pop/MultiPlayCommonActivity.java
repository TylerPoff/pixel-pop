package edu.northeastern.numad23sp_team26.pixel_pop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelMultiGame;

public class MultiPlayCommonActivity extends AppCompatActivity {

    protected DatabaseReference databaseRef;
    protected String multiPlayGameID;
    protected static boolean isPlaying;
    private ChildEventListener multiPlayGamesChildListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

        databaseRef = FirebaseDatabase.getInstance().getReference();

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            multiPlayGameID = extras.getString("gameID");

            multiPlayGamesListener();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseRef.child("MultiplayerGames").removeEventListener(multiPlayGamesChildListener);

        if (!multiPlayGameID.isEmpty() && !isPlaying) {
            databaseRef.child("MultiplayerGames").child(multiPlayGameID).removeValue();
            multiPlayGameID = "";
            finish();
        }
    }

    protected void multiPlayGamesListener() {
        databaseRef.child("MultiplayerGames").addChildEventListener(multiPlayGamesChildListener);
    }

    protected void createDisconnectedAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View DisconnectedPopupView = getLayoutInflater().inflate(R.layout.multi_disconnected_popup, null);
        Button okBtn = DisconnectedPopupView.findViewById(R.id.okBtn);
        dialogBuilder.setView(DisconnectedPopupView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        okBtn.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });
    }

    protected void startMultiplayerGame(String adventure, int levelNum) {
        databaseRef.child("MultiplayerGames").child(multiPlayGameID).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                PixelMultiGame multiGame = currentData.getValue(PixelMultiGame.class);
                if (multiGame == null) {
                    return Transaction.success(currentData);
                }

                multiGame.adventure = adventure;
                multiGame.levelNum = levelNum;
                currentData.setValue(multiGame);

                isPlaying = true;

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

            }
        });
    }
}

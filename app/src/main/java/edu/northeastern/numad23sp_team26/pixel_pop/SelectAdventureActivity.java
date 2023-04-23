package edu.northeastern.numad23sp_team26.pixel_pop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelMultiGame;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelPopUser;

public class SelectAdventureActivity extends AppCompatActivity {

    private static final String TAG = "pixel_pop.SelectAdventureActivity";
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private PixelPopUser currentUser;
    private TextView helloUsername;
    private ImageView profileImage;
    private Spinner spinner;
    private ProgressBar progressBar;
    private Button singlePlayerBtn;
    private Button hostMultiplayerBtn;
    private Button joinMultiplayerBtn;
    private List<String> multiPlayGameIDList = new ArrayList<>();

    enum PlayMode {
        SINGLE,
        MULTI
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_adventure);

        helloUsername = findViewById(R.id.hello_username);
        profileImage = findViewById(R.id.profileImage);
        spinner = findViewById(R.id.spinner);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        singlePlayerBtn = findViewById(R.id.singlePlayBtn);
        hostMultiplayerBtn = findViewById(R.id.multiHostBtn);
        joinMultiplayerBtn = findViewById(R.id.multiJoinBtn);

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        getCurrentUser();
        getGameIds();

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select Adventure");
        arrayList.add("Nature (Easy)");
        arrayList.add("Fruit Stand (Normal)");
        arrayList.add("Video Game (Hard)");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    singlePlayerBtn.setEnabled(true);
                    hostMultiplayerBtn.setEnabled(true);
                } else {
                    singlePlayerBtn.setEnabled(false);
                    hostMultiplayerBtn.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        singlePlayerBtn.setOnClickListener(v -> {
            String adventure = spinner.getSelectedItem().toString();
            switch (adventure) {
                case "Nature (Easy)":
                    openActivityAnimals(PlayMode.SINGLE);
                    break;
                case "Fruit Stand (Normal)":
                    openActivityFruits(PlayMode.SINGLE);
                    break;
                case "Video Game (Hard)":
                    openActivityVideoGame(PlayMode.SINGLE);
                    break;
            }
        });

        hostMultiplayerBtn.setOnClickListener(v -> {
            String adventure = spinner.getSelectedItem().toString();
            switch (adventure) {
                case "Nature (Easy)":
                    openActivityAnimals(PlayMode.MULTI);
                    break;
                case "Fruit Stand (Normal)":
                    openActivityFruits(PlayMode.MULTI);
                    break;
                case "Video Game (Hard)":
                    openActivityVideoGame(PlayMode.MULTI);
                    break;
            }
        });

        joinMultiplayerBtn.setOnClickListener(v -> openJoinMultiplayerActivity());
    }

    @Override
    protected void onResume() {
        super.onResume();
        spinner.setSelection(0);
        singlePlayerBtn.setEnabled(false);
        hostMultiplayerBtn.setEnabled(false);
    }

    public void openActivityAnimals(PlayMode mode) {
        Intent intent = new Intent(this, AnimalsAdventureActivity.class);

        Bundle extras = new Bundle();
        if (mode == PlayMode.MULTI) {
            extras.putString("gameID", getGameID());
        } else {
            extras.putString("gameID", "");
        }
        intent.putExtras(extras);

        startActivity(intent);
    }

    public void openActivityFruits(PlayMode mode) {
        Intent intent = new Intent(this, FruitsAdventureActivity.class);

        Bundle extras = new Bundle();
        if (mode == PlayMode.MULTI) {
            extras.putString("gameID", getGameID());
        } else {
            extras.putString("gameID", "");
        }
        intent.putExtras(extras);

        startActivity(intent);
    }

    public void openActivityVideoGame(PlayMode mode) {
        Intent intent = new Intent(this, VideoGameAdventureActivity.class);

        Bundle extras = new Bundle();
        if (mode == PlayMode.MULTI) {
            extras.putString("gameID", getGameID());
        } else {
            extras.putString("gameID", "");
        }
        intent.putExtras(extras);

        startActivity(intent);
    }

    public void openJoinMultiplayerActivity() {
        Intent intent = new Intent(this, JoinMultiplayerActivity.class);
        startActivity(intent);
    }

    private void getGameIds() {
        databaseRef.child("MultiplayerGames").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                PixelMultiGame multiGame = snapshot.getValue(PixelMultiGame.class);
                if (multiGame != null && !multiPlayGameIDList.contains(multiGame.gameID)) {
                    multiPlayGameIDList.add(multiGame.gameID);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                PixelMultiGame multiGame = snapshot.getValue(PixelMultiGame.class);
                if (multiGame != null) {
                    multiPlayGameIDList.remove(multiGame.gameID);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getGameID() {
        String multiPlayGameID = generatesGameId();
        while (multiPlayGameIDList.contains(multiPlayGameID)) {
            multiPlayGameID = generatesGameId();
        }
        saveMultiPlayGame(multiPlayGameID);
        return multiPlayGameID;
    }

    private String generatesGameId() {
        int strLen = 8;
        String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder gameIdSb = new StringBuilder(strLen);

        for (int i = 0; i < strLen; i++) {
            int index = (int) (alphaNumericString.length() * Math.random());
            gameIdSb.append(alphaNumericString.charAt(index));
        }

        return gameIdSb.toString();
    }

    private void saveMultiPlayGame(String gameID) {
        DatabaseReference multiplayerGamesRef = databaseRef.child("MultiplayerGames");
        multiplayerGamesRef.child(gameID).setValue(new PixelMultiGame(gameID, mAuth.getCurrentUser().getUid()));
    }

    private void getCurrentUser() {
        progressBar.setVisibility(View.VISIBLE);
        spinner.setEnabled(false);
        joinMultiplayerBtn.setEnabled(false);
        String uid = mAuth.getCurrentUser().getUid();
        databaseRef.child("Users").child(uid).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting firebase data", task.getException());
            }
            else {
                if (task.getResult().getValue() != null) {
                    currentUser = task.getResult().getValue(PixelPopUser.class);

                    if (currentUser != null) {
                        helloUsername.setText("Hello " + currentUser.email.split("@")[0]);
                        int imageResource = getResources().getIdentifier(currentUser.profilePicture, "drawable", getPackageName());
                        profileImage.setImageResource(imageResource);
                    }
                }
            }
            progressBar.setVisibility(View.INVISIBLE);
            spinner.setEnabled(true);
            joinMultiplayerBtn.setEnabled(true);
        });
    }
}
package edu.northeastern.numad23sp_team26.pixel_pop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.northeastern.numad23sp_team26.R;
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

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select Adventure");
        arrayList.add("Nature (Easy)");
        arrayList.add("Video Game (Hard)");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        singlePlayerBtn.setOnClickListener(v -> {
            String adventure = spinner.getSelectedItem().toString();
            switch (adventure) {
                case "Nature (Easy)":
                    openActivityAnimals();
                    break;
                case "Video Game (Hard)":
                    openActivityVideoGame();
                    break;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        spinner.setSelection(0);
    }

    public void openActivityAnimals() {
        Intent intent = new Intent(this, AnimalsAdventureActivity.class);
        startActivity(intent);
    }

    public void openActivityVideoGame() {
        Intent intent = new Intent(this, VideoGameAdventureActivity.class);
        startActivity(intent);
    }

    private void getCurrentUser() {
        progressBar.setVisibility(View.VISIBLE);
        spinner.setEnabled(false);
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
        });
    }
}
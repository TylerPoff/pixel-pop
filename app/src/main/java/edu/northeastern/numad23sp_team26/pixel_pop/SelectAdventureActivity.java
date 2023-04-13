package edu.northeastern.numad23sp_team26.pixel_pop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
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
    private Switch switchShakeErase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_adventure);

        helloUsername = findViewById(R.id.hello_username);
        profileImage = findViewById(R.id.profileImage);
        switchShakeErase = findViewById(R.id.switchShakeErase);

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        getCurrentUser();


        spinner = findViewById(R.id.spinner);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select Adventure");
        arrayList.add("Animals (Easy)");
        arrayList.add("Video Game (Hard)");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String adventure = parent.getItemAtPosition(position).toString();
                switch (adventure) {
                    case "Animals (Easy)":
                        openActivityAnimals();
                        break;
                    case "Video Game (Hard)":
                        openActivityVideoGame();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
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
        //TODO put the boolean value for shake to erase
        //get the value from the switch
        boolean shouldShake = switchShakeErase.isChecked();
        //we can pass data into an activity by setting up a key-value pair
        //we need a keyword to represent what this value is, we will need to use this
        //same keyword to get the value out in the other activity
        //We will use "shouldShake" as the keyword, and put the boolean variable in as the value
        intent.putExtra("shouldShake", shouldShake);
        startActivity(intent);
    }

    public void openActivityVideoGame() {
        Intent intent = new Intent(this, VideoGameAdventureActivity.class);
        boolean shouldShake = switchShakeErase.isChecked();
        intent.putExtra("shouldShake", shouldShake);
        startActivity(intent);
    }

    private void getCurrentUser() {
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
        });
    }
}
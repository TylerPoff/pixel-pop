package edu.northeastern.numad23sp_team26.a8_stickers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.a8_stickers.models.StickerReceived;
import edu.northeastern.numad23sp_team26.a8_stickers.models.StickerSent;
import edu.northeastern.numad23sp_team26.a8_stickers.models.User;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "a8_stickers.RegisterActivity";
    private DatabaseReference mDatabase;
    private TextInputEditText editTextUsername, editTextFirstName, editTextLastName;
    private String registerUsername, firstName, lastName;
    private Button buttonReg;
    private TextView registerErrorTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = findViewById(R.id.username);
        editTextFirstName = findViewById(R.id.firstname);
        editTextLastName = findViewById(R.id.lastname);
        buttonReg = findViewById(R.id.btnRegister);
        registerErrorTV = findViewById(R.id.registerErrorTV);

        TextView loginNavBtnTV = findViewById(R.id.loginNavBtnTV);
        loginNavBtnTV.setOnClickListener(v -> openActivityLogin());

        mDatabase = FirebaseDatabase.getInstance().getReference();

        buttonReg.setOnClickListener(v -> {
            registerErrorTV.setText("");
            registerUsername = editTextUsername.getText().toString().trim();
            firstName = editTextFirstName.getText().toString().trim();
            lastName = editTextLastName.getText().toString().trim();
            if (registerUsername.isEmpty()) {
                registerErrorTV.setText("Please enter username");
            } else if (registerUsername.length() > 20) {
                registerErrorTV.setText("Username cannot be longer than 20 characters");
            } else if (firstName.isEmpty()) {
                registerErrorTV.setText("Please enter first name");
            } else if (lastName.isEmpty()) {
                registerErrorTV.setText("Please enter last name");
            } else {
                onRegister(registerUsername, firstName, lastName);
            }
        });
    }

    // test
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        registerUsername = editTextUsername.getText().toString().trim();
        firstName = editTextFirstName.getText().toString().trim();
        lastName = editTextLastName.getText().toString().trim();
        outState.putString("registerUsername", registerUsername);
        outState.putString("firstName", firstName);
        outState.putString("lastName", lastName);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        registerUsername = savedInstanceState.getString("registerUsername");
        firstName = savedInstanceState.getString("firstName");
        lastName = savedInstanceState.getString("lastName");
        editTextUsername.setText(registerUsername);
        editTextFirstName.setText(firstName);
        editTextLastName.setText(lastName);
    }

    public void openActivityLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // Reference: https://firebase.google.com/docs/database/android/read-and-write#read_once_using_get
    // We use read once here to prevent duplicate usernames
    private void onRegister(String username, String firstName, String lastName) {
        mDatabase.child("users").child(username).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting firebase data", task.getException());
            }
            else {
                if (task.getResult().getValue() == null) {
                    User user = new User(username.toLowerCase(), firstName, lastName, new ArrayList<>(), new ArrayList<>());
                    mDatabase.child("users").child(username.toLowerCase()).setValue(user);
                    Intent intent = new Intent (getApplicationContext(), StickerUserActivity.class);

                    // Send current user
                    Bundle extras = new Bundle();
                    extras.putParcelable("currentUser", user);
                    intent.putExtras(extras);

                    startActivity(intent);
                    finish();
                } else {
                    registerErrorTV.setText("Username already exists");
                }
            }
        });
    }
}
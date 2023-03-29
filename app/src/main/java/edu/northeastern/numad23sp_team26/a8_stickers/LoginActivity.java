package edu.northeastern.numad23sp_team26.a8_stickers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.a8_stickers.models.User;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "a8_stickers.LoginActivity";
    private DatabaseReference mDatabase;
    private TextInputEditText editTextUsername;
    private String loginUsername;
    private Button buttonLogin;
    private TextView loginErrorTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.username);
        buttonLogin = findViewById(R.id.btnLogin);
        loginErrorTV = findViewById(R.id.loginErrorTV);

        TextView registerNavBtnTV = findViewById(R.id.registerNavBtnTV);
        registerNavBtnTV.setOnClickListener(v -> openActivityRegister());

        FirebaseApp stickers = FirebaseApp.getInstance("stickers");
        mDatabase = FirebaseDatabase.getInstance(stickers).getReference();

        buttonLogin.setOnClickListener(v -> {
            loginErrorTV.setText("");
            loginUsername = editTextUsername.getText().toString().trim();
            if (loginUsername.isEmpty()) {
                loginErrorTV.setText("Please enter username");
            } else {
                onLogin(loginUsername);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        loginUsername = editTextUsername.getText().toString().trim();
        outState.putString("loginUsername", loginUsername);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        loginUsername = savedInstanceState.getString("loginUsername");
        editTextUsername.setText(loginUsername);
    }

    public void openActivityRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    // Reference: https://firebase.google.com/docs/database/android/read-and-write#read_once_using_get
    // We use read once here to check if user exists
    private void onLogin(String username) {
        mDatabase.child("users").child(username).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting firebase data", task.getException());
            }
            else {
                if (task.getResult().getValue() != null) {
                    User currentUser = task.getResult().getValue(User.class);
                    Intent intent = new Intent (getApplicationContext(), StickerUserActivity.class);

                    // Send current user
                    Bundle extras = new Bundle();
                    extras.putParcelable("currentUser", currentUser);
                    intent.putExtras(extras);

                    startActivity(intent);
                    finish();
                } else {
                    loginErrorTV.setText("User does not exist");
                }
            }
        });
    }
}
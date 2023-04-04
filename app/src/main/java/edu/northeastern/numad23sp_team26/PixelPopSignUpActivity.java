package edu.northeastern.numad23sp_team26;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.numad23sp_team26.a8_stickers.LoginActivity;

public class PixelPopSignUpActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signUpButton;
    private TextView loginRedirect;
    private TextView signUpErrorTV;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixel_pop_sign_up);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        emailEditText = findViewById(R.id.signup_email);
        passwordEditText = findViewById(R.id.signup_password);
        signUpButton = findViewById(R.id.signup_button);
        loginRedirect = findViewById(R.id.signup_login_redirect);
        signUpErrorTV = findViewById(R.id.signup_error_tv);

        // Redirect to LoginActivity
        loginRedirect.setOnClickListener(v -> openActivityPixelPopLogin());

        // Sign up and case handling
        signUpButton.setOnClickListener(v -> {
            signUpErrorTV.setText("");
            email = emailEditText.getText().toString().trim();
            password = passwordEditText.getText().toString().trim();
            if (email.isEmpty()) {
                signUpErrorTV.setText("Please enter your email.");
            } else if (password.isEmpty()) {
                signUpErrorTV.setText("Please enter your password.");
            } else {
                signUp();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        outState.putString("email", email);
        outState.putString("password", password);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        email = savedInstanceState.getString("email");
        password = savedInstanceState.getString("password");
        emailEditText.setText(email);
        passwordEditText.setText(password);
    }

    private void signUp() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign up success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        addUserToDatabase(user);
                        Toast.makeText(PixelPopSignUpActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                        // Redirect to main activity or another relevant activity
                        redirectToLevelSelectActivity();
                    } else {
                        // If sign up fails, display a message to the user.
                        Exception exception = task.getException();
                        String errorMessage = exception != null ? exception.getMessage() : "Registration failed.";
                        Toast.makeText(PixelPopSignUpActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(this, e -> {
                    Log.e("PixelPopSignUp", "Error: " + e.getMessage());
                });
    }

    private void addUserToDatabase(FirebaseUser user) {
        if (user != null) {
            DatabaseReference usersRef = database.getReference("Users");
            usersRef.child(user.getUid()).setValue(user.getEmail());
        }
    }

    private void redirectToLevelSelectActivity() {
        Intent PixelPopLevelSelectionActivityIntent = new Intent(this, PixelPopLevelSelectionActivity.class);
        startActivity(PixelPopLevelSelectionActivityIntent);
        finish(); // Optional: to prevent going back to the login/sign-up activity using the back button
    }

    public void openActivityPixelPopLogin() {
        Intent loginIntent = new Intent(PixelPopSignUpActivity.this, PixelPopLoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

}


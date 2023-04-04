package edu.northeastern.numad23sp_team26;

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

public class PixelPopSignUpActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signUpButton;
    private TextView loginRedirect;
    private FirebaseAuth mAuth;

    private FirebaseDatabase database;

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

        signUpButton.setOnClickListener(v -> signUp());

        loginRedirect.setOnClickListener(v -> {
            // Redirect to LoginActivity
            Intent loginIntent = new Intent(PixelPopSignUpActivity.this, PixelPopLoginActivity.class);
            startActivity(loginIntent);
        });
    }

    private void signUp() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign up success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        addUserToDatabase(user);
                        Toast.makeText(PixelPopSignUpActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                        // Redirect to main activity or another relevant activity
                        redirectToMainActivity();
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

    private void redirectToMainActivity() {
        Intent PixelPopLevelSelectionActivityIntent = new Intent(this, PixelPopLevelSelectionActivity.class);
        startActivity(PixelPopLevelSelectionActivityIntent);
        finish(); // Optional: to prevent going back to the login/sign-up activity using the back button
    }

}


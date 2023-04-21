package edu.northeastern.numad23sp_team26.pixel_pop;

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

import edu.northeastern.numad23sp_team26.R;

public class PixelPopLoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signUpRedirect;
    private TextView loginErrorTV;
    private FirebaseAuth mAuth;

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixel_pop_login);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signUpRedirect = findViewById(R.id.login_signup_redirect);
        loginErrorTV = findViewById(R.id.login_error_tv);

        // Redirect to Sign Up Activity
        signUpRedirect.setOnClickListener(v -> openActivityPixelPopSignUp());

        loginButton.setOnClickListener(v -> logIn());

        // Log in and case handling
        loginButton.setOnClickListener(v -> {
            loginErrorTV.setText("");
            loginErrorTV.setVisibility(View.INVISIBLE);
            email = emailEditText.getText().toString().trim();
            password = passwordEditText.getText().toString().trim();
            if (email.isEmpty()) {
                loginErrorTV.setText("Please enter your email.");
                loginErrorTV.setVisibility(View.VISIBLE);
            } else if (password.isEmpty()) {
                loginErrorTV.setText("Please enter your password.");
                loginErrorTV.setVisibility(View.VISIBLE);
            } else {
                logIn();
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

    private void logIn() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(PixelPopLoginActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
                        // Redirect to main activity or another relevant activity
                        redirectToSelectAdventureActivity();
                    } else {
                        // If sign in fails, display a message to the user.
                        Exception exception = task.getException();
                        String errorMessage = exception != null ? exception.getMessage() : "Registration failed.";
                        Toast.makeText(PixelPopLoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(this, e -> {
                    Log.e("PixelPopLogin", "Error: " + e.getMessage());
                });
    }

    private void redirectToSelectAdventureActivity() {
        Intent PixelPopLevelSelectionActivityIntent = new Intent(this, SelectAdventureActivity.class);
        startActivity(PixelPopLevelSelectionActivityIntent);
        finish(); // Optional: to prevent going back to the login/sign-up activity using the back button
    }

    public void openActivityPixelPopSignUp() {
        Intent loginIntent = new Intent(this, PixelPopSignUpActivity.class);
        startActivity(loginIntent);
        finish();
    }

}

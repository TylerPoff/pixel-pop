package edu.northeastern.numad23sp_team26;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PixelPopLoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signUpRedirect;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixel_pop_login);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signUpRedirect = findViewById(R.id.login_signup_redirect);

        loginButton.setOnClickListener(v -> logIn());

        signUpRedirect.setOnClickListener(v -> {
            // Redirect to SignupActivity
            Intent signUpIntent = new Intent(PixelPopLoginActivity.this, PixelPopSignUpActivity.class);
            startActivity(signUpIntent);
        });
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
                        redirectToMainActivity();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(PixelPopLoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void redirectToMainActivity() {
        Intent PixelPopLevelSelectionActivityIntent = new Intent(this, PixelPopLevelSelectionActivity.class);
        startActivity(PixelPopLevelSelectionActivityIntent);
        finish(); // Optional: to prevent going back to the login/sign-up activity using the back button
    }

}

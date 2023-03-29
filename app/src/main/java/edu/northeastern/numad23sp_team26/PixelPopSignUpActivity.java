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

public class PixelPopSignUpActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signUpButton;
    private TextView loginRedirect;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixel_pop_sign_up);

        mAuth = FirebaseAuth.getInstance();

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
                        Toast.makeText(PixelPopSignUpActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                        // Redirect to your main activity or another relevant activity
                    } else {
                        // If sign up fails, display a message to the user.
                        Toast.makeText(PixelPopSignUpActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


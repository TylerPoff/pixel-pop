package edu.northeastern.numad23sp_team26.a8_stickers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.numad23sp_team26.R;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText editTextUsername, editTextFirstName, editTextLastName;
    Button buttonReg;
    FirebaseAuth mAuth;


    private static final String TAG = "a8_stickers.RegisterActivity";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        editTextUsername = findViewById(R.id.username);
        buttonReg = findViewById(R.id.btnRegister);

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, firstName, lastName;
                username = String.valueOf(editTextUsername.getText());
                firstName = String.valueOf(editTextFirstName.getText());
                lastName = String.valueOf(editTextLastName.getText());

                if (TextUtils.isEmpty(username)){
                    Toast.makeText(RegisterActivity.this, "Please, enter username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(firstName)){
                    Toast.makeText(RegisterActivity.this, "Please, enter first name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(firstName)){
                    Toast.makeText(RegisterActivity.this, "Please, enter last name", Toast.LENGTH_SHORT).show();
                    return;
                }

                /** implemented the code for creating the user
                 * create a new account by passing the new user's email address and password to createUserWithEmailAndPassword:
                 * working on creating the user with username, firstName, lastName instead of email and password */
                mAuth.createUserWithEmailAndPassword(username, firstName, lastName)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    // Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(RegisterActivity.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    // Log.w(TAG, "createUserWithUserName:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });



        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
}
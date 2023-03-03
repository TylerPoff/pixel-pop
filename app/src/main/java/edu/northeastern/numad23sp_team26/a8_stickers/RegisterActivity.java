package edu.northeastern.numad23sp_team26.a8_stickers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.numad23sp_team26.R;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText editTextUsername, editTextFirstName, editTextLastName;
    Button buttonReg;


    private static final String TAG = "a8_stickers.RegisterActivity";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
                }

                if (TextUtils.isEmpty(firstName)){
                    Toast.makeText(RegisterActivity.this, "Please, enter first name", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(firstName)){
                    Toast.makeText(RegisterActivity.this, "Please, enter last name", Toast.LENGTH_SHORT).show();
                }

            }
        });



        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
}
package edu.northeastern.numad23sp_team26.a8_stickers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.numad23sp_team26.R;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "a8_stickers.RegisterActivity";

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDatabase = FirebaseDatabase.getInstance().getReference();


    }
}



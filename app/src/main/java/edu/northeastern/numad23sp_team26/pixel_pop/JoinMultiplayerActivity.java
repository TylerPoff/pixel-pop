package edu.northeastern.numad23sp_team26.pixel_pop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelMultiGame;

public class JoinMultiplayerActivity extends AppCompatActivity {

    private static final String TAG = "pixel_pop.JoinMultiplayerActivity";
    private EditText gameIdEditText;
    private TextView errorTV;
    private Button joinBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_multiplayer);

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        gameIdEditText = findViewById(R.id.gameIdET);
        errorTV = findViewById(R.id.errorTV);
        joinBtn = findViewById(R.id.joinBtn);

        joinBtn.setOnClickListener(v -> joinMultiplayerGame());
    }

    private void joinMultiplayerGame() {
        String gameID = gameIdEditText.getText().toString().trim();

        if (gameID.isEmpty()) {
            errorTV.setText("Please enter a game ID");
            errorTV.setVisibility(View.VISIBLE);

            return;
        }

        errorTV.setText("");
        errorTV.setVisibility(View.INVISIBLE);
        databaseRef.child("MultiplayerGames").child(gameID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PixelMultiGame multiGame = dataSnapshot.getValue(PixelMultiGame.class);
                if (multiGame == null) {
                    errorTV.setText("Could not find game");
                    errorTV.setVisibility(View.VISIBLE);
                } else {
                    multiGame.playerTwoID = mAuth.getCurrentUser().getUid();
                    databaseRef.child("MultiplayerGames").child(multiGame.gameID).getRef().setValue(multiGame);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // onCancelled
            }
        });
    }
}

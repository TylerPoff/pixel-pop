package edu.northeastern.numad23sp_team26.pixel_pop;

import android.os.Bundle;

import androidx.annotation.Nullable;

import edu.northeastern.numad23sp_team26.R;

public class WaitJoinMultiplayerActivity extends MultiPlayCommonActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_join_multiplayer);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (multiPlayGameID.isEmpty()) {
            finish();
        }
    }
}

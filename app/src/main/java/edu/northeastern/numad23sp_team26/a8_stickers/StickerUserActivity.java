package edu.northeastern.numad23sp_team26.a8_stickers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.a8_stickers.models.StickerUser;
import edu.northeastern.numad23sp_team26.a8_stickers.models.User;

public class StickerUserActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_user);

        TextView helloUserTV = findViewById(R.id.helloUserTV);

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> logout());

        Button btnSendStickers = findViewById(R.id.btnSendStickers);
        Button btnReceivedHistory = findViewById(R.id.btnReceivedHistory);

        if (getIntent().getExtras() != null) {
            currentUser = getIntent().getExtras().getParcelable("currentUser");
            helloUserTV.setText(getString(R.string.user_greeting, currentUser.firstName, currentUser.lastName));
        }

        //TODO Open Send Stickers activity
        //TODO Open History activity

        viewPager = findViewById(R.id.viewPager);
        loadStickers();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                 //TODO Link with project to get username
            }

                @Override
                public void onPageSelected(int position) {
                    //Only one page to be included
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    //Only one way to control scroll
                }
        });
    }

    private void logout() {
        currentUser = null;
        Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadStickers() {
        ArrayList<StickerUser> userStickerList = new ArrayList<>();
        StickerUserAdapter adapter = new StickerUserAdapter(this, userStickerList);

        //dummy add
        userStickerList.add(new StickerUser("Frog","Stickers Sent: 1",R.drawable.sticker_1_frog));
        userStickerList.add(new StickerUser("Ribbon","Stickers Sent: 3",R.drawable.sticker_2_ribbon));
        userStickerList.add(new StickerUser("Backpack","Stickers Sent: 100",R.drawable.sticker_3_backpack));

        viewPager.setAdapter(adapter);
        viewPager.setPadding(100,100,100,100);
    }
}
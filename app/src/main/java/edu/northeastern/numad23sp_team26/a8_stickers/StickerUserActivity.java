package edu.northeastern.numad23sp_team26.a8_stickers;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.a8_stickers.models.StickerUser;

public class StickerUserActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_user);
        Button btnSendStickers = findViewById(R.id.btnSendStickers);
        Button btnReceivedHistory = findViewById(R.id.btnReceivedHistory);

        //TODO Open Send Stickers activity
        //TODO Open History activity

        actionBar = getSupportActionBar();
        viewPager = findViewById(R.id.viewPager);
        loadStickers();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                 actionBar.setTitle("Hello, USERNAME");
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
        }
        );
    }

    private void loadStickers() {
        ArrayList<StickerUser> userStickerList = new ArrayList<>();
        StickerUserAdapter adapter = new StickerUserAdapter(this, userStickerList);

        //dummy add
        userStickerList.add(new StickerUser("Cool Sticker","Stickers Sent: 1",R.drawable.fantasy_genre));
        userStickerList.add(new StickerUser("Awesome Sticker","Stickers Sent: 3",R.drawable.action_genre));
        userStickerList.add(new StickerUser("Scary Sticker","Stickers Sent: 100",R.drawable.horror_genre));

        viewPager.setAdapter(adapter);
        viewPager.setPadding(100,100,100,100);
    }
}
package edu.northeastern.numad23sp_team26.a8_stickers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.northeastern.numad23sp_team26.R;

public class StickerUserDirectoryActivity extends AppCompatActivity {
    private String userSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userSelected = "";
        setContentView(R.layout.activity_sticker_directory);
        ListView userListLV = findViewById(R.id.userListLV);
        ImageView stickerDirectoryIV = findViewById(R.id.stickerDirectoryIV);
        loadUsers();
        //TODO Send button functionality
    }

    private void loadUsers() {
        ArrayList<String> userList = new ArrayList<>();
        ListView userListLV = findViewById(R.id.userListLV);
        TextView sendingToTV = findViewById(R.id.sendingToTV);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.sticker_directory_list, R.id.userTV, userList);
        userListLV.setAdapter(adapter);
        userListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userSelected = userList.get(position);
                sendingToTV.setText(getResources().getString(R.string.you_re_sending_to) + userSelected);
            }
        });
    }
}
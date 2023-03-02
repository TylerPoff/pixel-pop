package edu.northeastern.numad23sp_team26;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class StickerUserAdapter extends PagerAdapter {

    private final Context context;
    private final ArrayList<StickerUserModel> StickerList;

    public StickerUserAdapter(Context context, ArrayList<StickerUserModel> StickerList) {
        this.context = context;
        this.StickerList = StickerList;
    }

    @Override
    public int getCount() {
        return StickerList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.sticker_user_card_item, container, false);
        ImageView stickerIV = view.findViewById(R.id.stickerIV);
        TextView nameTV = view.findViewById(R.id.stickerNameTV);
        TextView sentTV = view.findViewById(R.id.stickerSentTV);

        StickerUserModel model = StickerList.get(position);
        String name = model.getName();
        String sent = model.getSent();
        int image = model.getImage();

        stickerIV.setImageResource(image);
        nameTV.setText(name);
        sentTV.setText(sent);

        container.addView(view, position);

        return view;
    }
}

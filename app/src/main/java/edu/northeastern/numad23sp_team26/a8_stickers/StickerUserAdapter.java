package edu.northeastern.numad23sp_team26.a8_stickers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.a8_stickers.models.Sticker;
import edu.northeastern.numad23sp_team26.a8_stickers.models.StickerSent;

public class StickerUserAdapter extends PagerAdapter {

    private final Context context;
    private final ArrayList<StickerSent> StickerList;

    public StickerUserAdapter(Context context, ArrayList<StickerSent> StickerList) {
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

        StickerSent model = StickerList.get(position);
        Sticker sticker = model.getSticker();
        int totalCount = model.getTotalCount();

        stickerIV.setImageResource(sticker.getImageResource());
        nameTV.setText(sticker.getName());
        sentTV.setText(context.getString(R.string.num_sent, totalCount));

        container.addView(view, position);

        return view;
    }
}

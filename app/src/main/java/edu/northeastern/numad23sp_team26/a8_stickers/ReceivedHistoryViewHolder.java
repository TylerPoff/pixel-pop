package edu.northeastern.numad23sp_team26.a8_stickers;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.a8_stickers.models.StickerReceived;
import edu.northeastern.numad23sp_team26.a8_stickers.models.User;

public class ReceivedHistoryViewHolder  extends RecyclerView.ViewHolder {

    Context context;
    private static final String TAG = "RecievedHistoryViewHolder";
    public TextView stickerNameTV, stickerFromTV, stickerTimestampTV;
    public ImageView stickerIV;

    public ReceivedHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        stickerNameTV = itemView.findViewById(R.id.stickerNameTV);
        stickerFromTV = itemView.findViewById(R.id.fromTV);
        stickerTimestampTV = itemView.findViewById(R.id.timestampTV);
        stickerIV = itemView.findViewById(R.id.stickerIV);
    }

    public void bindThisData(StickerReceived received) {
        int imageResource = context.getResources().getIdentifier(received.getSticker().getFileName(), "drawable", context.getPackageName());
        stickerIV.setImageResource(imageResource);
        stickerNameTV.setText(received.getSticker().getName());
        User fromUser = received.getFrom();
        stickerFromTV.setText(context.getString(R.string.sticker_from, fromUser.firstName + " " + fromUser.lastName));
        LocalDateTime dateTime = LocalDateTime.parse(received.getTimeStamp());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm:ss a");
        String formatDateTime = dateTime.format(formatter);
        stickerTimestampTV.setText(context.getString(R.string.sticker_timestamp, formatDateTime));
    }
}

package edu.northeastern.numad23sp_team26;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReceivedHistoryViewHolder  extends RecyclerView.ViewHolder {

    Context context;
    private static final String TAG = "RecievedHistoryViewHolder";
    public TextView stickerNameTV, stickerFromTV, stickerTimestampTV;
    public ImageView stickerIV;
    private Handler handler = new Handler();

    public ReceivedHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        stickerNameTV = itemView.findViewById(R.id.stickerNameTV);
        stickerFromTV = itemView.findViewById(R.id.fromTV);
        stickerTimestampTV = itemView.findViewById(R.id.timestampTV);
        stickerIV = itemView.findViewById(R.id.stickerIV);

    }

}

package edu.northeastern.numad23sp_team26.a8_stickers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.a8_stickers.models.StickerReceived;

public class ReceivedHistoryAdapter extends RecyclerView.Adapter<ReceivedHistoryViewHolder> {

    private final List<StickerReceived> receivedHistories;
    private final Context context;

    public ReceivedHistoryAdapter(List<StickerReceived> receivedHistories, Context context) {
        this.receivedHistories = receivedHistories;
        this.context = context;
    }

    @NonNull
    @Override
    public ReceivedHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReceivedHistoryViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.sticker_card_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReceivedHistoryViewHolder holder, int position) {
        holder.bindThisData(receivedHistories.get(position));
    }

    @Override
    public int getItemCount() {
        return receivedHistories.size();
    }
}

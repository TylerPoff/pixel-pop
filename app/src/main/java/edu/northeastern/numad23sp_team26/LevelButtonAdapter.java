package edu.northeastern.numad23sp_team26;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LevelButtonAdapter extends RecyclerView.Adapter<LevelButtonViewHolder>{
    private final List<Integer> levels;
    private final Context context;

    public LevelButtonAdapter(List<Integer> levels, Context context) {
        this.levels = levels;
        this.context = context;
    }

    @NonNull
    @Override
    public LevelButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LevelButtonViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.level_button_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LevelButtonViewHolder holder, int position) {
        holder.bindThisData(position);
    }

    @Override
    public int getItemCount() {
        return levels.size();
    }
}

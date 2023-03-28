package edu.northeastern.numad23sp_team26;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LevelButtonViewHolder extends RecyclerView.ViewHolder{

    public TextView level;

    public LevelButtonViewHolder(@NonNull View itemView) {
        super(itemView);
        level = itemView.findViewById(R.id.level_button_text);
    }

    public void bindThisData(int level) {
        this.level.setText(level);
    }
}


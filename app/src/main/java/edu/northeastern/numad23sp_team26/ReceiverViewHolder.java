package edu.northeastern.numad23sp_team26;

import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReceiverViewHolder extends RecyclerView.ViewHolder {

    public RadioButton userRB;

    public ReceiverViewHolder(@NonNull View itemView) {
        super(itemView);
        userRB = itemView.findViewById(R.id.userRB);
    }
}

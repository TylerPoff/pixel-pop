package edu.northeastern.numad23sp_team26.a8_stickers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.ReceiverViewHolder;
import edu.northeastern.numad23sp_team26.a8_stickers.models.User;

public class ReceiverAdapter extends RecyclerView.Adapter<ReceiverViewHolder> {

    private final List<User> users;
    private final Context context;
    private int selectedPosition = -1;

    public ReceiverAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ReceiverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReceiverViewHolder(LayoutInflater.from(context).inflate(R.layout.sticker_directory_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiverViewHolder holder, int position) {
        User user = users.get(position);
        String fullName = user.firstName + " " + user.lastName;
        holder.userRB.setText(fullName);
        holder.userRB.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
            if (context instanceof StickerUserDirectoryActivity) {
                ((StickerUserDirectoryActivity) context).updateSendingToTV(fullName);
            }
        });
        holder.userRB.setChecked(position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}

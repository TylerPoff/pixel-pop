package edu.northeastern.numad23sp_team26;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MovieViewHolder extends RecyclerView.ViewHolder {

    Context context;
    private static final String TAG = "MovieViewHolder";
    public TextView nameTV, yearTV, typeTV, genreTV;
    public ImageView posterIV, genreIV;
    private Handler handler = new Handler();

    public MovieViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        nameTV = itemView.findViewById(R.id.nameTV);
        yearTV = itemView.findViewById(R.id.yearTV);
        typeTV = itemView.findViewById(R.id.typeTV);
        posterIV = itemView.findViewById(R.id.posterIV);
        genreTV = itemView.findViewById(R.id.genreTV);
        genreIV = itemView.findViewById(R.id.genreIV);
    }

    public void bindThisData(Movie movie) {
        nameTV.setText(movie.getName());
        yearTV.setText(context.getString(R.string.release, movie.getYear()));
        typeTV.setText(context.getString(R.string.type, movie.getType()));
        genreTV.setText(context.getString(R.string.genre, movie.getGenre()));
        if (!movie.getPosterURL().isEmpty()) {
            new Thread(() -> {
                try {
                    URL url = new URL(movie.getPosterURL());
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    handler.post(() -> posterIV.setImageBitmap(bmp));
                } catch (MalformedURLException e) {
                    Log.e(TAG,"MalformedURLException");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e(TAG,"IOException");
                    e.printStackTrace();
                }
            }).start();
        }
        Integer genreImg = getGenreIMG(movie.getGenre());
        if (genreImg != null) {
            genreIV.setImageResource(getGenreIMG(movie.getGenre()));
        }
    }

    public Integer getGenreIMG(String genre){
        switch(genre.toLowerCase()) {
            case "action":
                return R.drawable.action_genre;
            case "comedy":
                return R.drawable.comedy_genre;
            //TODO: complete this
            default:
                return null;
        }
    }
}

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
                    // Sometimes, poster URL returned from the API call could be malformed
                } catch (IOException e) {
                    Log.e(TAG,"IOException");
                    e.printStackTrace();
                }
            }).start();
        }
        String[] movieGenre = movie.getGenre().split(",\n");
        Integer genreImg = null;
        if (movieGenre.length > 0) {
            int current = 0;
            while (genreImg == null && current < movieGenre.length) {
                String currentGenre = movieGenre[current].trim();
                genreImg = getGenreIMG(currentGenre);
                current += 1;
            }
        }
        if (genreImg != null) {
            genreIV.setVisibility(View.VISIBLE);
            genreIV.setImageResource(genreImg);
        } else {
            genreIV.setVisibility(View.INVISIBLE);
        }
    }

    public Integer getGenreIMG(String genre){
        switch(genre.toLowerCase()) {
            case "action":
                return R.drawable.action_genre;
            case "comedy":
                return R.drawable.comedy_genre;
            case "drama":
                return R.drawable.drama_genre;
            case "fantasy":
                return R.drawable.fantasy_genre;
            case "horror":
                return R.drawable.horror_genre;
            case "romance":
                return R.drawable.romance_genre;
            case "science fiction":
                return R.drawable.sciencefiction_genre;
            default:
                return null;
        }
    }

}

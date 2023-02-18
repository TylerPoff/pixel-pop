package edu.northeastern.numad23sp_team26;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    public TextView nameTV, yearTV, imdbIDTV,typeTV;
    public ImageView posterIV, genreIV;

    public MovieViewHolder(@NonNull View itemView) {
        super(itemView);
        nameTV = itemView.findViewById(R.id.nameTV);
        yearTV = itemView.findViewById(R.id.yearTV);
        imdbIDTV = itemView.findViewById(R.id.imdbIDTV);
        typeTV = itemView.findViewById(R.id.typeTV);
        posterIV = itemView.findViewById(R.id.posterIV);
        genreIV = itemView.findViewById(R.id.genreIV);
    }

    public void bindThisData(Movie movie) {
        nameTV.setText(movie.getName());
        yearTV.setText("Year: " + movie.getYear());
        imdbIDTV.setText("IMDB ID: " + movie.getImdbID());
        typeTV.setText("Type: " + movie.getType());
        //posterIV.setImageResource(movie.getPoster());
        genreIV.setImageResource(getGenreIMG(movie.getGenre()));
    }

    public int getGenreIMG(String genre){
        switch(genre){
            default: return R.drawable.comedy_genre;
        }
    }
}

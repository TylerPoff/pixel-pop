package edu.northeastern.numad23sp_team26;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity {

    private static final String TAG = "MovieActivity";

    private final String omdbApiUrl = "https://www.omdbapi.com/?apikey=";
    private final String omdbApiKey = "8e8bb9a6";

    private Handler resHandler = new Handler();

    private RecyclerView.LayoutManager layoutManager;
    private MovieAdapter adapter;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView progressText;
    private EditText searchEditText;
    private TextView msgTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progressText = findViewById(R.id.progressText);
        progressText.setVisibility(View.INVISIBLE);

        searchEditText = findViewById(R.id.searchEditText);

        msgTextView = findViewById(R.id.msgTextView);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MovieAdapter(movieList, this);
        recyclerView.setAdapter(adapter);
    }

    public void searchMovies(View view) {
        String searchWords = searchEditText.getText().toString();
        if (!searchWords.isEmpty()) {
            Thread omdbThread = new OMDBWebServiceThread(searchWords);
            omdbThread.start();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("searchText", searchEditText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String searchText = savedInstanceState.getString("searchText");
        searchEditText.setText(searchText);

        Thread omdbThread = new OMDBWebServiceThread(searchText);
        omdbThread.start();
    }

    private class OMDBWebServiceThread extends Thread {

        private String searchWords;

        public OMDBWebServiceThread(String searchWords) {
            this.searchWords = searchWords;
        }

        @Override
        public void run() {
            try {
                resHandler.post(() -> {
                    progressBar.setIndeterminate(true);
                    progressBar.setVisibility(View.VISIBLE);
                    progressText.setVisibility(View.VISIBLE);
                });

                movieList.clear();
                resHandler.post(() -> adapter.notifyDataSetChanged());
                URL url = new URL(omdbApiUrl + omdbApiKey + "&s=" + searchWords);
                String res = NetworkUtil.httpResponse(url);
                JSONObject jObject = new JSONObject(res);

                try {
                    if (jObject.has("Search")) {
                        JSONArray arr = jObject.getJSONArray("Search");
                        int minNumMovies = Math.min(10, arr.length());
                        resHandler.post(() -> msgTextView.setText("Top " + 10 + " results"));
                        int progress = 0;
                        for (int i = 0; i < minNumMovies; i++) {
                            progress += 1;
                            int finalProgress = progress;
                            resHandler.post(() -> progressText.setText("Loading " + finalProgress + " of " + minNumMovies + " movies..."));

                            JSONObject obj = arr.getJSONObject(i);
                            String ImdbId = obj.getString("imdbID"); // get unique ID of movie

                            String genre = "";
                            try {
                                URL itemUrl = new URL(omdbApiUrl + omdbApiKey + "&i=" + ImdbId);
                                String itemRes = NetworkUtil.httpResponse(itemUrl);
                                JSONObject resJObject = new JSONObject(itemRes);
                                genre = resJObject.getString("Genre");
                            } catch (MalformedURLException e) {
                                Log.e(TAG, "MalformedURLException");
                                e.printStackTrace();
                            } catch (IOException e) {
                                Log.e(TAG, "IOException");
                                e.printStackTrace();
                            }

                            String name = obj.getString("Title");
                            String year = obj.getString("Year");
                            String type = obj.getString("Type");
                            String poster = obj.getString("Poster");
                            movieList.add(new Movie(name, year, type, poster, genre));
                        }
                        resHandler.post(() -> adapter.notifyDataSetChanged());
                    } else {
                        resHandler.post(() -> {
                            try {
                                msgTextView.setText("Error: " + jObject.getString("Error"));
                            } catch (JSONException e) {
                                Log.e(TAG,"JSONException");
                                e.printStackTrace();
                            }
                        });
                    }
                } catch (JSONException e) {
                    Log.e(TAG,"JSONException");
                    e.printStackTrace();
                }

                resHandler.post(() -> {
                    progressBar.setIndeterminate(false);
                    progressBar.setVisibility(View.INVISIBLE);
                    progressText.setVisibility(View.INVISIBLE);
                });
            } catch (MalformedURLException e) {
                Log.e(TAG,"MalformedURLException");
                e.printStackTrace();
            } catch (ProtocolException e) {
                Log.e(TAG,"ProtocolException");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG,"IOException");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e(TAG,"JSONException");
                e.printStackTrace();
            }
        }
    }
}

package edu.northeastern.numad23sp_team26;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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

    private final String omdbApiUrl = BuildConfig.OMDB_API_URL;
    private final String omdbApiKey = BuildConfig.OMDB_API_KEY;

    private Handler resHandler = new Handler();

    private RecyclerView.LayoutManager layoutManager;
    private MovieAdapter adapter;
    private ArrayList<Movie> movieList = new ArrayList<>();

    private ProgressBar progressBar;
    private TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie);
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);

        Thread omdbThread = new OMDBWebServiceThread("Top Gun");
        omdbThread.start();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MovieAdapter(movieList, this);
        recyclerView.setAdapter(adapter);
        progressBar.setMax(100);
        progressText.setText("Loading...");
    }

    private class OMDBWebServiceThread extends Thread {

        private String searchWord;

        public OMDBWebServiceThread(String searchWord) {
            this.searchWord = searchWord;
        }

        @Override
        public void run() {
            try {
                //URL url = new URL(omdbApiUrl + omdbApiKey + "&s=" + searchWord);
                URL url = new URL("https://www.example.com");
                String res = NetworkUtil.httpResponse(url);

                JSONObject jObject = new JSONObject(res);

                resHandler.post(() -> {
                    progressBar.setIndeterminate(true);
                    progressBar.setVisibility(View.VISIBLE);
                    progressText.setText("Loading...");
                    try {
                        if (jObject.has("Search")) {
                            JSONArray arr = jObject.getJSONArray("Search");
                            //int numMovies = arr.length();
                            int numMovies = 5;
                            progressBar.setMax(numMovies);
                            for (int i = 0; i < numMovies; i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                String name = obj.getString("Title");
                                int year = Integer.parseInt(obj.getString("Year"));
                                String type = obj.getString("Type");
                                String poster = obj.getString("Poster");
                                movieList.add(new Movie(name, year, type, poster));

                                int progress = i + 1;
                                progressBar.setProgress(progress);
                                progressText.setText("Loading " + progress + " of " + numMovies + " movies...");
                                Log.d(TAG, "Progress text updated to: Loading " + progress + " of " + numMovies + " movies...");
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            TextView errorTextView = findViewById(R.id.errorTextView);
                            errorTextView.setText("Error: " + jObject.getString("Error"));
                        }
                    } catch (JSONException e) {
                        Log.e(TAG,"JSONException");
                        e.printStackTrace();
                    }
                    progressBar.setIndeterminate(false);
                    progressBar.setVisibility(View.GONE);
                    progressText.setText("");
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
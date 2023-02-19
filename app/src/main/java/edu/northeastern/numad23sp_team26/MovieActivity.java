package edu.northeastern.numad23sp_team26;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
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

    /** ---------------------------------------------------------------------- */
    private SearchView searchView; // todo : new
    private String input;
    ListView myListView;  // todo : new

    // todo: making the search view functional
    /** ---------------------------------------------------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie);

        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);




        // TODO: put search query here
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // "    Top Gun    "
                input = query.trim();
                Log.v(TAG, input);
                OMDBWebServiceThread omdbThread = new OMDBWebServiceThread(input);
                omdbThread.start();
                movieList = omdbThread.getMovies();
                recycler();
                try {
                    omdbThread.join();
                    Log.v(TAG, "Thread Alive: " + omdbThread.isAlive());
                    Log.v(TAG, "Thread Id: " + omdbThread.getId());

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


                //  progressBar.setMax(100);
                //  progressText.setText("Loading...");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.v(TAG, "newText " + newText);
                //movieList = new ArrayList<>();
                return false;
            }
        });
    }

    private void recycler() {
        layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MovieAdapter(movieList, this);
        recyclerView.setAdapter(adapter);
    }

    private class OMDBWebServiceThread extends Thread {

        private String searchWord;
        private ArrayList<Movie> movieList = new ArrayList<>();


        public OMDBWebServiceThread(String searchWord) {
            this.searchWord = searchWord;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(omdbApiUrl + omdbApiKey + "&s=" + searchWord);
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
                            int numMovies = 100;
                            progressBar.setMax(numMovies);
                            for (int i = 0; i < numMovies; i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                String ImdbId = obj.getString("imdbID"); // get unique ID of movie

                                String genre ="";
                                try {
                                    URL itemUrl = new URL(omdbApiUrl + omdbApiKey + "&i=" + ImdbId);
                                    String itemres = NetworkUtil.httpResponse(itemUrl);
                                    JSONObject resJObject = new JSONObject(itemres);
                                    genre = resJObject.getString("Genre");
                                } catch (MalformedURLException e) {
                                    Log.e(TAG, "MalformedURLException");
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    Log.e(TAG, "IOException");
                                    e.printStackTrace();
                                }


                                String name = obj.getString("Title");
                                int year = Integer.parseInt(obj.getString("Year"));
                                String type = obj.getString("Type");
                                String poster = obj.getString("Poster");
                                movieList.add(new Movie(name, year, type, poster, genre));

                                int progress = i + 1;
                                progressBar.setProgress(progress);
                                progressText.setText("Loading " + progress + " of " + numMovies + " movies...");
                                Log.d(TAG, "Progress text updated to: Loading " + progress + " of " + numMovies + " movies...");
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

        public ArrayList<Movie> getMovies() {return this.movieList;}
    }


}

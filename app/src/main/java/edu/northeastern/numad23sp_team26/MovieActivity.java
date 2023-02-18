package edu.northeastern.numad23sp_team26;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import edu.northeastern.numad23sp_team26.databinding.ActivityMovieBinding;

public class MovieActivity extends AppCompatActivity {

    private ActivityMovieBinding binding;
    private static final String TAG = "MovieActivity";

    private final String omdbApiUrl = BuildConfig.OMDB_API_URL;
    private final String omdbApiKey = BuildConfig.OMDB_API_KEY;

    private Handler resHandler = new Handler();

    private RecyclerView.LayoutManager layoutManager;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMovieBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Thread omdbThread = new OMDBWebServiceThread("Top Gun");
        omdbThread.start();

       binding.recyclerView.setHasFixedSize(true);
       layoutManager = new LinearLayoutManager(this);
       binding.recyclerView.setLayoutManager(layoutManager);
       adapter = new MovieAdapter(new ArrayList<>(), this);
       binding.recyclerView.setAdapter(adapter);

    }

    private class OMDBWebServiceThread extends Thread {

        private String searchWord;

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
                    try {
                        if (jObject.has("Search")) {
                            binding.webTextView.setText(jObject.getJSONArray("Search").toString());
                        } else {
                            binding.webTextView.setText(jObject.getString("Error"));
                        }
                    } catch (JSONException e) {
                        Log.e(TAG,"JSONException");
                        e.printStackTrace();
                    }
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
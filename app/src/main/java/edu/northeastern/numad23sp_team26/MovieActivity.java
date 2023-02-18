package edu.northeastern.numad23sp_team26;

import androidx.appcompat.app.AppCompatActivity;

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

import edu.northeastern.numad23sp_team26.databinding.ActivityMovieBinding;

public class MovieActivity extends AppCompatActivity {

    private ActivityMovieBinding binding;
    private static final String TAG = "MovieActivity";

    private final String omdbApiUrl = BuildConfig.OMDB_API_URL;
    private final String omdbApiKey = BuildConfig.OMDB_API_KEY;

    private Handler resHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMovieBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Thread omdbThread = new OMDBWebServiceThread("Top Gun");
        omdbThread.start();
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
                    binding.webTextView.setText(jObject.toString());
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
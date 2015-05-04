package com.mertkahyaoglu.randommovie;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.mertkahyaoglu.randommovie.volley.VolleyController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {

    JSONArray ids = null;
    JSONObject fetchedData = null;

    NetworkImageView nivPoster;
    Button btnRefresh;

    private static final String base_url = "http://www.omdbapi.com/?i=";
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setClickListeners();

        try {
            JSONObject json = new JSONObject(getIntent().getStringExtra("initialdata"));
            fetchedData = json;
            updateScreen(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fetchAndShowMovie(String id) {
        String url = base_url + id;
        JsonObjectRequest movieReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            fetchedData = response;
                            updateScreen(fetchedData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fetchAndShowMovie(Utils.getRandomID(ids));
            }
        });

        VolleyController.getInstance(getApplicationContext()).addToRequestQueue(movieReq);
    }

    private void updateScreen(JSONObject json) throws JSONException {
        String posterUrl = json.getString("Poster");
        String title = json.getString("Title");
        getSupportActionBar().setTitle(title);
        nivPoster.setImageUrl(posterUrl, imageLoader);
    }

    private void init() {
        btnRefresh = (Button)findViewById(R.id.btn_refresh);
        nivPoster = (NetworkImageView)findViewById(R.id.movie_poster);
        imageLoader = VolleyController.getInstance(getApplicationContext()).getImageLoader();
        ids = Utils.loadJSONFromAsset(getApplicationContext());
    }

    private void setClickListeners() {
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = Utils.getRandomID(ids);
                if(Utils.isNetworkStatusAvialable(getApplicationContext())) {
                    fetchAndShowMovie(id);
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();

                }
            }
        });
        nivPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fetchedData != null) {
                    Intent i = new Intent(getBaseContext(), MovieDetailActivity.class);
                    i.putExtra("json", fetchedData.toString());
                    i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);
                }
            }
        });
    }

}

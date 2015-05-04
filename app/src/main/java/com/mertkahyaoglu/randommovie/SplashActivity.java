package com.mertkahyaoglu.randommovie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mertkahyaoglu.randommovie.volley.VolleyController;

import org.json.JSONArray;
import org.json.JSONObject;

public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 1000;
    private static final String base_url = "http://www.omdbapi.com/?i=";
    JSONArray ids = null;
    JSONObject fetchedData = null;

    ImageView loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loading = (ImageView) findViewById(R.id.loading);
        loading.setBackgroundResource(R.drawable.loading_animation);
        ids = Utils.loadJSONFromAsset(getApplicationContext());
        loading.post(new Runnable() {
            @Override
            public void run() {
                AnimationDrawable frameAnimation = (AnimationDrawable) loading.getBackground();
                frameAnimation.start();
            }
        });

        if(Utils.isNetworkStatusAvialable(getApplicationContext())) {
            fetchAndShowMovie(Utils.getRandomID(ids));
        } else {
            Toast.makeText(getApplicationContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
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
                            new CountDownTimer(SPLASH_TIME_OUT, 1000) {
                                public void onTick(long millisUntilFinished) {}

                                public void onFinish() {
                                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                                    i.putExtra("initialdata", fetchedData.toString());
                                    i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    startActivity(i);
                                    finish();
                                }
                            }.start();
                        } catch (Exception e) {
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


}

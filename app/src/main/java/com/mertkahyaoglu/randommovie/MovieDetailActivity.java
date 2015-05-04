package com.mertkahyaoglu.randommovie;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mertkahyaoglu.randommovie.volley.VolleyController;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailActivity extends ActionBarActivity {

    TextView movieTitle;
    TextView movieRating;
    TextView movieGenres;
    TextView movieCast;
    TextView movieDirector;
    TextView moviePlot;
    TextView movieRuntime;
    NetworkImageView movieThumbnail;

    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        init();
        setMovieContentView();
    }


    private void setMovieContentView() {
        JSONObject json = null;
        try {
            json = new JSONObject(getIntent().getStringExtra("json"));

            String url = json.getString("Poster");
            String title = json.getString("Title");

            String year = json.getString("Year");
            year = "<font color='#5F9BCB'> ("+year+")</font>";

            String plotStart = "<font color='#F57C00'>Plot: </font>";
            String plot = json.getString("Plot");

            String genres = json.getString("Genre");
            String runtime = json.getString("Runtime");
            String rating = json.getString("imdbRating");

            String directorStart = "<font color='#F57C00'>Director: </font>";
            String director = json.getString("Director");

            String castStart = "<font color='#F57C00'>Cast: </font>";
            String cast = json.getString("Actors");

            getSupportActionBar().setTitle(title);
            movieGenres.setText(genres);
            movieRuntime.setText("Runtime: "+runtime);
            movieRating.setText("Rating: "+rating);
            movieTitle.setText(Html.fromHtml(title + year));
            movieThumbnail.setImageUrl(url, imageLoader);
            moviePlot.setText(Html.fromHtml(plotStart + plot));
            movieDirector.setText(Html.fromHtml(directorStart + director));
            movieCast.setText(Html.fromHtml(castStart + cast));

        } catch (JSONException e) {
            Log.d("ERROR:", e.toString());
        }
    }

    public void init() {
        imageLoader = VolleyController.getInstance(getApplicationContext()).getImageLoader();

        moviePlot = (TextView)findViewById(R.id.tvPlot);
        movieDirector = (TextView)findViewById(R.id.tvDirector);
        movieCast = (TextView)findViewById(R.id.tvCast);
        movieThumbnail = (NetworkImageView) findViewById(R.id.nivThumbnail);
        movieTitle = (TextView) findViewById(R.id.tvTitle);
        movieGenres = (TextView)findViewById(R.id.tvGenres);
        movieRuntime = (TextView)findViewById(R.id.tvRuntime);
        movieRating = (TextView)findViewById(R.id.tvRating);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}

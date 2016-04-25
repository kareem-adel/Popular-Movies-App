package com.kareem_adel.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MovieDetail extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);
        Bundle bundle = new Bundle();
        bundle.putString("MovieID", getIntent().getExtras().getString("MovieID"));
        MovieDetailFragment movieDetail = new MovieDetailFragment();
        movieDetail.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_activity_container, movieDetail).commit();
    }
}

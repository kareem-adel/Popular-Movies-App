package com.kareem_adel.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements NewsFeedFragment.Callback {

    boolean twoPane;
    FrameLayout main_feed_detail_fragment_container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(20, TimeUnit.SECONDS);
            client.setReadTimeout(20, TimeUnit.SECONDS);

            File cachePath = getCacheDir();
            client.setCache(new com.squareup.okhttp.Cache(cachePath, 30000000));

            Picasso sPicasso = new Picasso.Builder(this)
                    .downloader(new OkHttpDownloader(client))
                    .build();
            Picasso.setSingletonInstance(sPicasso);
        } catch (IllegalStateException e) {
        }

        setContentView(R.layout.activity_main);

        main_feed_detail_fragment_container = (FrameLayout) findViewById(R.id.main_feed_detail_fragment_container);
        twoPane = main_feed_detail_fragment_container != null;

        //startActivity(new Intent(this, NewsFeedFragment.class));
        //NewsFeedFragment newsFeedFragment = new NewsFeedFragment();
        //getSupportFragmentManager().beginTransaction().add(R.id.MainActivity_Container, newsFeedFragment).show(newsFeedFragment).commit();
    }

    @Override
    public void launchDetailFragment(String MovieID) {
        if (twoPane) {
            Bundle bundle = new Bundle();
            bundle.putString("MovieID", MovieID);
            MovieDetailFragment movieDetail = new MovieDetailFragment();
            movieDetail.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_feed_detail_fragment_container, movieDetail).commit();
        } else {
            Intent intent = new Intent(this, MovieDetail.class);
            intent.putExtra("MovieID", MovieID);
            startActivity(intent);
        }
    }
}

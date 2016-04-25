package com.kareem_adel.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kareem_adel.popularmovies.DataSource.Database.MoviesContract;
import com.kareem_adel.popularmovies.DataSource.MovieAPIs;
import com.kareem_adel.popularmovies.Favourites.SetFavouritesTask;
import com.kareem_adel.popularmovies.FetchTasks.ReviewsFetchTask;
import com.kareem_adel.popularmovies.FetchTasks.VideosFetchTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MovieDetailFragment extends Fragment {

    TextView MovieDetail_MovieName;
    TextView MovieDetail_Description;
    TextView MovieDetail_Rating;
    TextView MovieDetail_Year;
    Button MovieDetail_MarkAsFavourite;
    ImageView MovieDetail_Poster;
    LinearLayout MovieDetail_Trailers;
    LinearLayout MovieDetail_Reviews;
    Bitmap exclamation;

    String MovieID;

    //TrailersAdapter trailersAdapter;
    //ReviewsAdapter reviewsAdapter;

    public final static int GeneralDetailFragmentLoaderCONST = 0;
    public final static int DetailFragmentLoaderVideosCONST = 1;
    public final static int DetailFragmentLoaderReviewsCONST = 2;
    public final static int DetailFragmentLoaderFavouriteCONST = 3;

    GeneralDetailFragmentLoader generalDetailFragmentLoader;
    DetailFragmentLoaderVideos detailFragmentLoaderVideos;
    DetailFragmentLoaderReviews detailFragmentLoaderReviews;
    DetailFragmentLoaderFavourite detailFragmentLoaderFavourite;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share_action, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ShareAction: {
                if (MovieDetail_Trailers.getChildCount() > 0) {
                    View view = MovieDetail_Trailers.getChildAt(0);
                    TextView TrailerItem_TrailerName = (TextView) view.findViewById(R.id.TrailerItem_TrailerName);
                    String VideoKey = (String) TrailerItem_TrailerName.getTag();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + VideoKey);
                    intent.setType("text/plain");
                    startActivity(intent);
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        MovieDetail_MovieName = (TextView) view.findViewById(R.id.MovieDetail_MovieName);
        MovieDetail_Description = (TextView) view.findViewById(R.id.MovieDetail_Description);
        MovieDetail_MarkAsFavourite = (Button) view.findViewById(R.id.MovieDetail_MarkAsFavourite);
        MovieDetail_Rating = (TextView) view.findViewById(R.id.MovieDetail_Rating);
        MovieDetail_Year = (TextView) view.findViewById(R.id.MovieDetail_Year);
        MovieDetail_Poster = (ImageView) view.findViewById(R.id.MovieDetail_Poster);
        MovieDetail_Trailers = (LinearLayout) view.findViewById(R.id.MovieDetail_Trailers);
        MovieDetail_Reviews = (LinearLayout) view.findViewById(R.id.MovieDetail_Reviews);
        exclamation = BitmapFactory.decodeResource(getResources(), R.drawable.exclamation);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MovieID = getArguments().getString("MovieID");

        //trailersAdapter = new TrailersAdapter(getContext(), null, false);
        //MovieDetail_Trailers.setAdapter(trailersAdapter);
        //reviewsAdapter = new ReviewsAdapter(getContext(), null, false);
        //MovieDetail_Reviews.setAdapter(reviewsAdapter);

        MovieDetail_MarkAsFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch ((String) v.getTag()) {
                    case "set": {
                        SetFavouritesTask setFavouritesTask = new SetFavouritesTask(getContext());
                        setFavouritesTask.execute("unset", MovieID);
                        break;
                    }
                    case "unset": {
                        SetFavouritesTask setFavouritesTask = new SetFavouritesTask(getContext());
                        setFavouritesTask.execute("set", MovieID);
                        break;
                    }
                }
            }
        });

        generalDetailFragmentLoader = new GeneralDetailFragmentLoader();
        getLoaderManager().initLoader(GeneralDetailFragmentLoaderCONST, null, generalDetailFragmentLoader);
        detailFragmentLoaderVideos = new DetailFragmentLoaderVideos();
        getLoaderManager().initLoader(DetailFragmentLoaderVideosCONST, null, detailFragmentLoaderVideos);
        detailFragmentLoaderReviews = new DetailFragmentLoaderReviews();
        getLoaderManager().initLoader(DetailFragmentLoaderReviewsCONST, null, detailFragmentLoaderReviews);
        detailFragmentLoaderFavourite = new DetailFragmentLoaderFavourite();
        getLoaderManager().initLoader(DetailFragmentLoaderFavouriteCONST, null, detailFragmentLoaderFavourite);

        refreshData();
    }

    ReviewsFetchTask reviewsFetchTask;
    VideosFetchTask videosFetchTask;

    public void refreshData() {
        if (reviewsFetchTask != null) {
            reviewsFetchTask.cancel(true);
        }
        if (videosFetchTask != null) {
            videosFetchTask.cancel(true);
        }

        reviewsFetchTask = new ReviewsFetchTask(getContext());
        reviewsFetchTask.execute(MovieID);

        videosFetchTask = new VideosFetchTask(getContext());
        videosFetchTask.execute(MovieID);

    }

    public static final int COLUMN_MovieName = 0;
    public static final int COLUMN_Description = 1;
    public static final int COLUMN_Rating = 2;
    public static final int COLUMN_Year = 3;
    public static final int COLUMN_Poster = 4;

    public class GeneralDetailFragmentLoader implements LoaderManager.LoaderCallbacks<Cursor> {


        public final String[] DetailProjection = new String[]{
                MoviesContract.Movies.COLUMN_MovieName,
                MoviesContract.Movies.COLUMN_Description,
                MoviesContract.Movies.COLUMN_Rating,
                MoviesContract.Movies.COLUMN_Year,
                MoviesContract.Movies.COLUMN_Poster};


        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getActivity(), MoviesContract.Movies.MovieUri(), DetailProjection, MoviesContract.Movies.COLUMN_MovieID + " = ?", new String[]{MovieID}, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data.moveToFirst()) {
                MovieDetail_MovieName.setText(data.getString(COLUMN_MovieName));
                MovieDetail_Description.setText(data.getString(COLUMN_Description));
                //MovieDetail_Duration.setText(movieDetail.);
                //MovieDetail_MarkAsFavourite = (Button) view.findViewById(R.id.MovieDetail_MarkAsFavourite);
                MovieDetail_Rating.setText(data.getString(COLUMN_Rating));
                MovieDetail_Year.setText(data.getString(COLUMN_Year).split("-")[0]);

                String ImageUrl = MovieAPIs.API_BASE_URL_IMAGES + data.getString(COLUMN_Poster);
                Picasso.with(getActivity()).load(ImageUrl).placeholder(R.drawable.placeholder).error(R.drawable.exclamation).into(MovieDetail_Poster);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    public static final int COLUMN_VideosID = 0;
    public static final int COLUMN_Key = 1;
    public static final int COLUMN_Name = 2;

    public class DetailFragmentLoaderVideos implements LoaderManager.LoaderCallbacks<Cursor> {

        public final String[] VideoProjection = new String[]{
                MoviesContract.Videos._ID,
                MoviesContract.Videos.COLUMN_Key,
                MoviesContract.Videos.COLUMN_Name};


        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getActivity(), MoviesContract.Videos.VideosUri(), VideoProjection, MoviesContract.Videos.COLUMN_MovieID + " = ?", new String[]{MovieID}, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            //trailersAdapter.swapCursor(data);
            MovieDetail_Trailers.removeAllViews();
            while (data.moveToNext()) {
                LayoutInflater layoutInflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = layoutInflator.inflate(R.layout.trailer_item, MovieDetail_Trailers, false);
                TextView TrailerItem_TrailerName = (TextView) view.findViewById(R.id.TrailerItem_TrailerName);
                ImageView TrailerItem_Play = (ImageView) view.findViewById(R.id.TrailerItem_Play);
                TrailerItem_TrailerName.setText(data.getString(COLUMN_Name));

                TrailerItem_TrailerName.setTag(data.getString(COLUMN_Key));
                TrailerItem_Play.setTag(data.getString(COLUMN_Key));
                TrailerItem_TrailerName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String VideoKey = (String) v.getTag();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + VideoKey));
                        startActivity(intent);
                    }
                });
                TrailerItem_Play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String VideoKey = (String) v.getTag();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + VideoKey));
                        startActivity(intent);
                    }
                });

                MovieDetail_Trailers.addView(view);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            //trailersAdapter.swapCursor(null);
        }
    }

    public static final int COLUMN_ReviewsID = 0;
    public static final int COLUMN_Author = 1;
    public static final int COLUMN_Content = 2;

    public class DetailFragmentLoaderReviews implements LoaderManager.LoaderCallbacks<Cursor> {

        public final String[] ReviewProjection = new String[]{
                MoviesContract.Reviews._ID,
                MoviesContract.Reviews.COLUMN_Author,
                MoviesContract.Reviews.COLUMN_Content};


        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getActivity(), MoviesContract.Reviews.ReviewsUri(), ReviewProjection, MoviesContract.Reviews.COLUMN_MovieID + " = ?", new String[]{MovieID}, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            //reviewsAdapter.swapCursor(data);
            MovieDetail_Reviews.removeAllViews();
            while (data.moveToNext()) {
                LayoutInflater layoutInflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = layoutInflator.inflate(R.layout.review_item, MovieDetail_Reviews, false);
                TextView ReviewItem_AuthorName = (TextView) view.findViewById(R.id.ReviewItem_AuthorName);
                TextView ReviewItem_Content = (TextView) view.findViewById(R.id.ReviewItem_Content);
                ReviewItem_AuthorName.setText(data.getString(COLUMN_Author));
                ReviewItem_Content.setText(data.getString(COLUMN_Content));
                MovieDetail_Reviews.addView(view);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            //reviewsAdapter.swapCursor(null);
        }
    }


    public static final int COLUMN_FavouriteID = 0;
    public static final int COLUMN_MovieID = 1;

    public class DetailFragmentLoaderFavourite implements LoaderManager.LoaderCallbacks<Cursor> {

        public final String[] FavouriteProjection = new String[]{
                MoviesContract.Favourites._ID,
                MoviesContract.Favourites.COLUMN_MovieID};


        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getActivity(), MoviesContract.Favourites.FavouritesUri(), FavouriteProjection, MoviesContract.Favourites.COLUMN_MovieID + " = ?", new String[]{MovieID}, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data.moveToFirst()) {
                MovieDetail_MarkAsFavourite.setBackgroundColor(Color.YELLOW);
                MovieDetail_MarkAsFavourite.setTag("set");
            } else {
                MovieDetail_MarkAsFavourite.setBackgroundColor(Color.GRAY);
                MovieDetail_MarkAsFavourite.setTag("unset");
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            //reviewsAdapter.swapCursor(null);
        }
    }
/*
    public class TrailersAdapter extends CursorAdapter {

        public TrailersAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater layoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return layoutInflator.inflate(R.layout.trailer_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView textView = (TextView) view.findViewById(R.id.TrailerItem_TrailerName);
            textView.setText(cursor.getString(COLUMN_Name));
        }
    }
    */

    /*
    public class ReviewsAdapter extends CursorAdapter {

        public ReviewsAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater layoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return layoutInflator.inflate(R.layout.trailer_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView textView = (TextView) view.findViewById(R.id.TrailerItem_TrailerName);
            textView.setText(cursor.getString(COLUMN_Author));
        }
    }
    */
}

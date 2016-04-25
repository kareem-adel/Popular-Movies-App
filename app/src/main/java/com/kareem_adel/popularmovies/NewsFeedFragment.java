package com.kareem_adel.popularmovies;

import android.app.Service;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.kareem_adel.popularmovies.DataSource.Database.MoviesContract;
import com.kareem_adel.popularmovies.DataSource.MovieAPIs;
import com.kareem_adel.popularmovies.FetchTasks.MoviesFetchTask;
import com.squareup.picasso.Picasso;

public class NewsFeedFragment extends Fragment {
    GridView NewsFeed;
    NewsFeedAdapter newsFeedAdapter;

    MoviesLoader moviesLoader;
    public final static int MoviesLoaderCONST = 3;
    int CurrentSortingType = 0;
    int currentFeedClickPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        NewsFeed = (GridView) view.findViewById(R.id.NewsFeed);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        NewsFeed.setNumColumns(2);
        newsFeedAdapter = new NewsFeedAdapter(getContext(), null, 0);
        NewsFeed.setAdapter(newsFeedAdapter);
        NewsFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentFeedClickPosition = position;
                Cursor cursor = (Cursor) NewsFeed.getItemAtPosition(position);
                String MovieID = cursor.getString(COLUMN_MovieID);

                ((Callback) getActivity()).launchDetailFragment(MovieID);

                //Bundle bundle = new Bundle();
                //bundle.putString("MovieID", cursor.getString(COLUMN_MovieID));
                //MovieDetailFragment movieDetail = new MovieDetailFragment();
                //movieDetail.setArguments(bundle);
                //getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivity_Container, movieDetail).show(movieDetail).hide(NewsFeedFragment.this).addToBackStack(null).commit();
            }
        });


        CurrentSortingType = R.id.Most_popular;
        refreshMovies(CurrentSortingType);

        moviesLoader = new MoviesLoader();
        getLoaderManager().initLoader(MoviesLoaderCONST, null, moviesLoader);

        if (savedInstanceState != null)
            currentFeedClickPosition = savedInstanceState.getInt("FeedPos", 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("FeedPos", currentFeedClickPosition);
        super.onSaveInstanceState(outState);
    }

    public interface Callback {
        void launchDetailFragment(String MovieID);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(MoviesLoaderCONST, null, moviesLoader);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort_by_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Most_popular: {
                refreshMovies(R.id.Most_popular);
                CurrentSortingType = R.id.Most_popular;
                break;
            }
            case R.id.Top_rated: {
                refreshMovies(R.id.Top_rated);
                CurrentSortingType = R.id.Top_rated;
                break;
            }
            case R.id.Favourites: {
                CurrentSortingType = R.id.Favourites;
                break;
            }
        }
        getLoaderManager().restartLoader(MoviesLoaderCONST, null, moviesLoader);
        return super.onOptionsItemSelected(item);
    }

    public static final int COLUMN_MovieID = 1;
    public static final int COLUMN_Poster = 2;

    public class MoviesLoader implements LoaderManager.LoaderCallbacks<Cursor> {

        public final String[] DetailProjection = new String[]{
                MoviesContract.Movies._ID,
                MoviesContract.Movies.COLUMN_MovieID,
                MoviesContract.Movies.COLUMN_Poster};


        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            String SortingType = "";
            switch (CurrentSortingType) {
                case R.id.Most_popular: {
                    SortingType = MovieAPIs.FEED_SORTING_POPULAR;
                    break;
                }
                case R.id.Top_rated: {
                    SortingType = MovieAPIs.FEED_SORTING_TOP_RATED;
                    break;
                }
                case R.id.Favourites: {
                    return new CursorLoader(getActivity(), MoviesContract.Favourites.FavouritesUri(), null, null, null, null);
                }
            }
            return new CursorLoader(getActivity(), MoviesContract.MoviesSorting.MoviesSortingUri(), null, null, new String[]{SortingType}, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            newsFeedAdapter.swapCursor(data);
            NewsFeed.smoothScrollToPosition(currentFeedClickPosition);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            newsFeedAdapter.swapCursor(null);
        }
    }

    public class NewsFeedAdapter extends CursorAdapter {


        public NewsFeedAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.movie_poster_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final ViewHolder viewHolder = (ViewHolder) view.getTag();
            String ImageUrl = MovieAPIs.API_BASE_URL_IMAGES + cursor.getString(COLUMN_Poster);
            Picasso.with(getActivity()).load(ImageUrl).placeholder(R.drawable.placeholder).error(R.drawable.exclamation).into(viewHolder.movie_poster_image);
        }

        public class ViewHolder {
            ImageView movie_poster_image;

            public ViewHolder(View itemView) {
                movie_poster_image = (ImageView) itemView.findViewById(R.id.movie_poster_image);
            }
        }
    }


    MoviesFetchTask moviesFetchTask;

    public void refreshMovies(int sortType) {
        if (moviesFetchTask != null) {
            moviesFetchTask.cancel(true);
        }
        switch (sortType) {
            case R.id.Most_popular: {
                moviesFetchTask = new MoviesFetchTask(getContext());
                moviesFetchTask.execute(MovieAPIs.FEED_SORTING_POPULAR);
                break;
            }
            case R.id.Top_rated: {
                moviesFetchTask = new MoviesFetchTask(getContext());
                moviesFetchTask.execute(MovieAPIs.FEED_SORTING_TOP_RATED);
                break;
            }
        }
    }


}

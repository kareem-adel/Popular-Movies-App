package com.kareem_adel.popularmovies.DataSource;

import android.net.Uri;

import com.kareem_adel.popularmovies.DataSource.Database.MoviesContract;

import java.net.MalformedURLException;
import java.net.URL;

public class MovieAPIs {
    final public static String API_BASE_URL = "http://api.themoviedb.org/3/movie/";
    final public static String API_BASE_URL_IMAGES = "http://image.tmdb.org/t/p/w185//";
    final public static String API_KEY_NAME = "api_key";
    final public static String api_key = "7068d94dc6285b83bfc97bb548d3d554";

    final public static String FEED_SORTING_POPULAR = "popular";
    final public static String FEED_SORTING_TOP_RATED = "top_rated";

    public static URL BuildVideosAPI(String id) throws MalformedURLException {
        return new URL(Uri.parse(API_BASE_URL).buildUpon().appendPath(id).appendPath(MoviesContract.VIDEOS_PATH).appendQueryParameter(API_KEY_NAME, api_key).build().toString());
    }

    public static URL BuildReviewsAPI(String id) throws MalformedURLException {
        return new URL(Uri.parse(API_BASE_URL).buildUpon().appendPath(id).appendPath(MoviesContract.REVIEWS_PATH).appendQueryParameter(API_KEY_NAME, api_key).build().toString());
    }

    public static URL BuildMoviesAPI(String Sorting) throws MalformedURLException {
        return new URL(Uri.parse(API_BASE_URL).buildUpon().appendPath(Sorting).appendQueryParameter("api_key", MovieAPIs.api_key).build().toString());
    }
}

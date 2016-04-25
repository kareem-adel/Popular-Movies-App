package com.kareem_adel.popularmovies.DataSource.Database;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {
    public static final String DatabaseName = "PopularMoviesDatabase";
    public static final String authority = "com.kareem_adel.popularmovies";
    final public static String MAIN_FEED_PATH = "main";
    final public static String MOVIE_PATH = "movie";
    final public static String VIDEOS_PATH = "videos";
    final public static String REVIEWS_PATH = "reviews";
    final public static String FAVOURITES_PATH = "favourites";
    final public static String MoviesSorting_PATH = "moviessorting";

    public static Uri BaseUri = Uri.parse("content://" + authority);

    public static class MoviesSorting implements BaseColumns {
        public static final String TABLE_NAME = "moviessorting";
        public static final String COLUMN_MovieID = "movieid";
        public static final String COLUMN_MovieFeedRanking = "ranking";
        public static final String COLUMN_Type = "myfeedtype";

        public static Uri MoviesSortingUri() {
            return BaseUri.buildUpon().appendPath(MoviesSorting_PATH).build();
        }
    }

    public static class Movies implements BaseColumns {
        public static final String TABLE_NAME = "moviesfeed";
        public static final String COLUMN_MovieID = "movieid";
        public static final String COLUMN_MovieName = "moviename";
        public static final String COLUMN_Description = "description";
        public static final String COLUMN_Duration = "duration";
        public static final String COLUMN_Rating = "rating";
        public static final String COLUMN_Year = "year";
        public static final String COLUMN_Poster = "poster";


        public static final int Sorting_MostPopular = 0;
        public static final int Sorting_TopRated = 1;

        public static Uri MoviesFeedUri() {
            return BaseUri.buildUpon().appendPath(MAIN_FEED_PATH).build();
        }

        public static Uri MovieUri() {
            return BaseUri.buildUpon().appendPath(MOVIE_PATH).build();
        }
    }

    public static class Favourites implements BaseColumns {
        public static final String TABLE_NAME = "Favourites";
        public static final String COLUMN_MovieID = "movieid";

        public static Uri FavouritesUri() {
            return BaseUri.buildUpon().appendPath(FAVOURITES_PATH).build();
        }
    }

    public static class Videos implements BaseColumns {
        public static final String TABLE_NAME = "videos";
        public static final String COLUMN_MovieID = "movieid";
        public static final String COLUMN_Key = "videokey";
        public static final String COLUMN_Name = "videoname";

        public static Uri VideosUri() {
            return BaseUri.buildUpon().appendPath(VIDEOS_PATH).build();
        }
    }

    public static class Reviews implements BaseColumns {
        public static final String TABLE_NAME = "reviews";
        public static final String COLUMN_MovieID = "movieid";
        public static final String COLUMN_Author = "reviewauthor";
        public static final String COLUMN_Content = "reviewcontent";

        public static Uri ReviewsUri() {
            return BaseUri.buildUpon().appendPath(REVIEWS_PATH).build();
        }
    }
}

package com.kareem_adel.popularmovies.DataSource;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.kareem_adel.popularmovies.DataSource.Database.MoviesContract;
import com.kareem_adel.popularmovies.DataSource.Database.PopularSQLiteDatabase;

public class MyGlobalContentProvider extends ContentProvider {
    UriMatcher uriMatcher;
    static final int VIDEOS_URI = 0;
    static final int REVIEWS_URI = 1;
    static final int MAIN_FEED_URI = 2;
    static final int FAVOURITES_URI = 3;
    static final int MOVIES_SORTING_URI = 4;
    static final int MOVIE_URI = 5;

    PopularSQLiteDatabase popularSQLiteDatabase;

    public MyGlobalContentProvider() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MoviesContract.authority, MoviesContract.VIDEOS_PATH, VIDEOS_URI);
        uriMatcher.addURI(MoviesContract.authority, MoviesContract.REVIEWS_PATH, REVIEWS_URI);
        uriMatcher.addURI(MoviesContract.authority, MoviesContract.MAIN_FEED_PATH, MAIN_FEED_URI);
        uriMatcher.addURI(MoviesContract.authority, MoviesContract.FAVOURITES_PATH, FAVOURITES_URI);
        uriMatcher.addURI(MoviesContract.authority, MoviesContract.MoviesSorting_PATH, MOVIES_SORTING_URI);
        uriMatcher.addURI(MoviesContract.authority, MoviesContract.MOVIE_PATH, MOVIE_URI);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = popularSQLiteDatabase.getWritableDatabase();
        int type = uriMatcher.match(uri);
        int ret;
        switch (type) {
            case VIDEOS_URI: {
                ret = sqLiteDatabase.delete(MoviesContract.Videos.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case REVIEWS_URI: {
                ret = sqLiteDatabase.delete(MoviesContract.Reviews.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case MAIN_FEED_URI: {
                ret = sqLiteDatabase.delete(MoviesContract.Movies.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case FAVOURITES_URI: {
                ret = sqLiteDatabase.delete(MoviesContract.Favourites.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case MOVIES_SORTING_URI: {
                ret = sqLiteDatabase.delete(MoviesContract.MoviesSorting.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case MOVIE_URI: {
                ret = sqLiteDatabase.delete(MoviesContract.Movies.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ret;
    }

    @Override
    public String getType(Uri uri) {
        int type = uriMatcher.match(uri);
        switch (type) {
            case VIDEOS_URI: {
                return "VIDEOS_URI";
            }
            case REVIEWS_URI: {
                return "REVIEWS_URI";
            }
            case MAIN_FEED_URI: {
                return "MAIN_FEED_URI";
            }
            case FAVOURITES_URI: {
                return "FAVOURITES_PATH";
            }
            case MOVIES_SORTING_URI: {
                return "MOVIES_SORTING_PATH";
            }
            case MOVIE_URI: {
                return "MOVIE_PATH";
            }
            default:
                return "Unknown";
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase sqLiteDatabase = popularSQLiteDatabase.getWritableDatabase();
        int type = uriMatcher.match(uri);
        switch (type) {
            case VIDEOS_URI: {
                sqLiteDatabase.insert(MoviesContract.Videos.TABLE_NAME, null, values);//, "Could not insert into " + MoviesContract.Videos.TABLE_NAME);
                break;
            }
            case REVIEWS_URI: {
                sqLiteDatabase.insert(MoviesContract.Reviews.TABLE_NAME, null, values);//, "Could not insert into " + MoviesContract.Reviews.TABLE_NAME);
                break;
            }
            case MAIN_FEED_URI: {
                sqLiteDatabase.insert(MoviesContract.Movies.TABLE_NAME, null, values);//, "Could not insert into " + MoviesContract.Movies.TABLE_NAME);
                break;
            }
            case FAVOURITES_URI: {
                sqLiteDatabase.insert(MoviesContract.Favourites.TABLE_NAME, null, values);//, "Could not insert into " + MoviesContract.Favourites.TABLE_NAME);
                break;
            }
            case MOVIES_SORTING_URI: {
                sqLiteDatabase.insert(MoviesContract.MoviesSorting.TABLE_NAME, null, values);//, "Could not insert into " + MoviesContract.Favourites.TABLE_NAME);
                break;
            }
            case MOVIE_URI: {
                sqLiteDatabase.insert(MoviesContract.Movies.TABLE_NAME, null, values);//, "Could not insert into " + MoviesContract.Favourites.TABLE_NAME);
                break;
            }
            default:
                throw new UnsupportedOperationException();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    public void verifyCompletion(long operation, String message) {
        if (operation < 0) {
            try {
                throw new Exception(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreate() {
        popularSQLiteDatabase = new PopularSQLiteDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase sqLiteDatabase = popularSQLiteDatabase.getWritableDatabase();
        int type = uriMatcher.match(uri);
        Cursor cursor;
        switch (type) {
            case VIDEOS_URI: {
                cursor = sqLiteDatabase.query(MoviesContract.Videos.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case REVIEWS_URI: {
                cursor = sqLiteDatabase.query(MoviesContract.Reviews.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case MAIN_FEED_URI: {
                cursor = sqLiteDatabase.query(MoviesContract.Movies.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case FAVOURITES_URI: {
                //cursor = sqLiteDatabase.query(MoviesContract.Favourites.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                if (selectionArgs != null) {
                    String ID_MovieID_Poster = MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies._ID + " , " + MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies.COLUMN_MovieID + " , " + MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies.COLUMN_Poster;
                    String query = "select " + ID_MovieID_Poster + " from " + MoviesContract.Favourites.TABLE_NAME + " inner join " + MoviesContract.Movies.TABLE_NAME + " on " + MoviesContract.Favourites.TABLE_NAME + "." + MoviesContract.Favourites.COLUMN_MovieID + " = " + MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies.COLUMN_MovieID + " where " + MoviesContract.Favourites.TABLE_NAME + "." + MoviesContract.Favourites.COLUMN_MovieID + " = ?";
                    cursor = sqLiteDatabase.rawQuery(query, selectionArgs);
                } else {
                    String ID_MovieID_Poster = MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies._ID + " , " + MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies.COLUMN_MovieID + " , " + MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies.COLUMN_Poster;
                    String query = "select " + ID_MovieID_Poster + " from " + MoviesContract.Favourites.TABLE_NAME + " inner join " + MoviesContract.Movies.TABLE_NAME + " on " + MoviesContract.Favourites.TABLE_NAME + "." + MoviesContract.Favourites.COLUMN_MovieID + " = " + MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies.COLUMN_MovieID;
                    cursor = sqLiteDatabase.rawQuery(query, null);
                }
                break;
            }
            case MOVIES_SORTING_URI: {
                //cursor = sqLiteDatabase.query(MoviesContract.MoviesSorting.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                String ID_MovieID_Poster_Ranking = MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies._ID + " , " + MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies.COLUMN_MovieID + " , " + MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies.COLUMN_Poster + " , " + MoviesContract.MoviesSorting.TABLE_NAME + "." + MoviesContract.MoviesSorting.COLUMN_MovieFeedRanking;
                String query = "select " + ID_MovieID_Poster_Ranking + " from " + MoviesContract.MoviesSorting.TABLE_NAME + " inner join " + MoviesContract.Movies.TABLE_NAME + " on " + MoviesContract.MoviesSorting.TABLE_NAME + "." + MoviesContract.MoviesSorting.COLUMN_MovieID + " = " + MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies.COLUMN_MovieID + " where " + MoviesContract.MoviesSorting.TABLE_NAME + "." + MoviesContract.MoviesSorting.COLUMN_Type + " = ?"
                        + " order by "
                        + MoviesContract.MoviesSorting.COLUMN_MovieFeedRanking + " asc";
                cursor = sqLiteDatabase.rawQuery(query, selectionArgs);

                /*
                String ID_MovieID_Poster_Ranking_Tableless = MoviesContract.Movies._ID + " , " + MoviesContract.Movies.COLUMN_MovieID + " , " + MoviesContract.Movies.COLUMN_Poster + " , " + MoviesContract.MoviesSorting.COLUMN_MovieFeedRanking;
                String ID_MovieID_Poster_Ranking = MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies._ID + " , " + MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies.COLUMN_MovieID + " , " + MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies.COLUMN_Poster + " , " + MoviesContract.MoviesSorting.COLUMN_MovieFeedRanking;
                String ID_MovieID_Poster_TopRanking = MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies._ID + " , " + MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies.COLUMN_MovieID + " , " + MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies.COLUMN_Poster + " , 0 as " + MoviesContract.MoviesSorting.COLUMN_MovieFeedRanking;
                // we get the favourites then combine them using union with the feed, we remove the repeated MovieIDs from the sorting table that exist in the favourites, return the remaining ids , do an inner join with the sorting table and another inner join with the movies table to get the feed with favourites
                cursor = sqLiteDatabase.rawQuery("select distinct " + ID_MovieID_Poster_Ranking_Tableless + " from ( "
                                + "select " + ID_MovieID_Poster_TopRanking + " from " + MoviesContract.Favourites.TABLE_NAME + " inner join " + MoviesContract.Movies.TABLE_NAME + " on " + MoviesContract.Favourites.TABLE_NAME + "." + MoviesContract.Favourites.COLUMN_MovieID + " = " + MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies.COLUMN_MovieID
                                + " union "
                                + "select " + ID_MovieID_Poster_Ranking + " from ( select " + MoviesContract.MoviesSorting.COLUMN_MovieID + " from " + MoviesContract.MoviesSorting.TABLE_NAME + " where " + MoviesContract.MoviesSorting.TABLE_NAME + "." + MoviesContract.MoviesSorting.COLUMN_Type + " = ?" + " except " + " select " + MoviesContract.Favourites.COLUMN_MovieID + " from " + MoviesContract.Favourites.TABLE_NAME + " ) sortleft inner join " + MoviesContract.MoviesSorting.TABLE_NAME + " on sortleft." + MoviesContract.MoviesSorting.COLUMN_MovieID + " = " + MoviesContract.MoviesSorting.TABLE_NAME + "." + MoviesContract.MoviesSorting.COLUMN_MovieID + " inner join " + MoviesContract.Movies.TABLE_NAME + " on " + "sortleft." + MoviesContract.MoviesSorting.COLUMN_MovieID + " = " + MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies.COLUMN_MovieID
                                + " ) "
                                + " order by "
                                + MoviesContract.MoviesSorting.COLUMN_MovieFeedRanking + " asc"
                        , selectionArgs);
                */
                break;
            }
            case MOVIE_URI: {
                cursor = sqLiteDatabase.query(MoviesContract.Movies.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException();
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = popularSQLiteDatabase.getWritableDatabase();
        int type = uriMatcher.match(uri);
        int ret;
        switch (type) {
            case VIDEOS_URI: {
                ret = sqLiteDatabase.update(MoviesContract.Videos.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case REVIEWS_URI: {
                ret = sqLiteDatabase.update(MoviesContract.Reviews.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case MAIN_FEED_URI: {
                ret = sqLiteDatabase.update(MoviesContract.Movies.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case FAVOURITES_URI: {
                ret = sqLiteDatabase.update(MoviesContract.Favourites.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case MOVIES_SORTING_URI: {
                ret = sqLiteDatabase.update(MoviesContract.MoviesSorting.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case MOVIE_URI: {
                ret = sqLiteDatabase.update(MoviesContract.Movies.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ret;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase sqLiteDatabase = popularSQLiteDatabase.getWritableDatabase();
        int type = uriMatcher.match(uri);
        sqLiteDatabase.beginTransaction();
        int count = 0;
        switch (type) {
            case VIDEOS_URI: {
                try {
                    for (ContentValues contentValue : values) {
                        if (sqLiteDatabase.insert(MoviesContract.Videos.TABLE_NAME, null, contentValue) != -1) {
                            count++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                break;
            }
            case REVIEWS_URI: {
                try {
                    for (ContentValues contentValue : values) {
                        if (sqLiteDatabase.insert(MoviesContract.Reviews.TABLE_NAME, null, contentValue) != -1) {
                            count++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                break;
            }
            case MAIN_FEED_URI: {
                try {
                    for (ContentValues contentValue : values) {
                        String MovieID = (String) contentValue.get(MoviesContract.Movies.COLUMN_MovieID);
                        sqLiteDatabase.delete(MoviesContract.Movies.TABLE_NAME, MoviesContract.Movies.COLUMN_MovieID + " = ?", new String[]{MovieID});
                        if (sqLiteDatabase.insert(MoviesContract.Movies.TABLE_NAME, null, contentValue) != -1) {
                            count++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                break;
            }
            case FAVOURITES_URI: {
                try {
                    for (ContentValues contentValue : values) {
                        if (sqLiteDatabase.insert(MoviesContract.Favourites.TABLE_NAME, null, contentValue) != -1) {
                            count++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                break;
            }
            case MOVIES_SORTING_URI: {
                try {
                    for (ContentValues contentValue : values) {
                        if (sqLiteDatabase.insert(MoviesContract.MoviesSorting.TABLE_NAME, null, contentValue) != -1) {
                            count++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                break;
            }
            case MOVIE_URI: {
                try {
                    for (ContentValues contentValue : values) {
                        if (sqLiteDatabase.insert(MoviesContract.Movies.TABLE_NAME, null, contentValue) != -1) {
                            count++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                break;
            }
            default:
                throw new UnsupportedOperationException();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}

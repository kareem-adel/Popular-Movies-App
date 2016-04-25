package com.kareem_adel.popularmovies.DataSource.Database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class PopularSQLiteDatabase extends SQLiteOpenHelper {
    public static final int mVersion = 20;

    public PopularSQLiteDatabase(Context context) {
        super(context, MoviesContract.DatabaseName, null, mVersion);
    }


    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        String CREATE_MOVIES_MOVIES_SORTING_TABLE = "CREATE TABLE " + MoviesContract.MoviesSorting.TABLE_NAME + " ( " +
                MoviesContract.MoviesSorting._ID + " INTEGER AUTO INCREMENT, " +
                MoviesContract.MoviesSorting.COLUMN_MovieID + " INTEGER UNIQUE, " +
                MoviesContract.MoviesSorting.COLUMN_MovieFeedRanking + " INTEGER, " +
                MoviesContract.MoviesSorting.COLUMN_Type + " TEXT" +
                //" FOREIGN KEY ( " + MoviesContract.Favourites.COLUMN_MovieID + " ) REFERENCES " + MoviesContract.Movies.TABLE_NAME + "  ( " + MoviesContract.Movies.COLUMN_MovieID + ")" +
                ");";
        db.execSQL(CREATE_MOVIES_MOVIES_SORTING_TABLE);

        String CREATE_MOVIES_FEED_TABLE = "CREATE TABLE " + MoviesContract.Movies.TABLE_NAME + " ( " +
                MoviesContract.Movies._ID + " INTEGER AUTO INCREMENT," +
                MoviesContract.Movies.COLUMN_MovieID + " INTEGER, " +
                MoviesContract.Movies.COLUMN_MovieName + " TEXT, " +
                MoviesContract.Movies.COLUMN_Description + " TEXT, " +
                MoviesContract.Movies.COLUMN_Poster + " TEXT, " +
                MoviesContract.Movies.COLUMN_Rating + " FLOAT, " +
                MoviesContract.Movies.COLUMN_Year + " TEXT" +
                //MoviesContract.Movies.COLUMN_Type + " TEXT" +
                ");";
        db.execSQL(CREATE_MOVIES_FEED_TABLE);

        String CREATE_MOVIES_FAVOURITES_TABLE = "CREATE TABLE " + MoviesContract.Favourites.TABLE_NAME + " ( " +
                MoviesContract.Favourites._ID + " INTEGER AUTO INCREMENT, " +
                MoviesContract.Favourites.COLUMN_MovieID + " INTEGER UNIQUE " +
                //" FOREIGN KEY ( " + MoviesContract.Favourites.COLUMN_MovieID + " ) REFERENCES " + MoviesContract.Movies.TABLE_NAME + "  ( " + MoviesContract.Movies.COLUMN_MovieID + ")" +
                ");";
        db.execSQL(CREATE_MOVIES_FAVOURITES_TABLE);

        String CREATE_MOVIES_VIDEOS_TABLE = "CREATE TABLE " + MoviesContract.Videos.TABLE_NAME + " ( " +
                MoviesContract.Videos._ID + " INTEGER AUTO INCREMENT, " +
                MoviesContract.Videos.COLUMN_MovieID + " INTEGER, " +
                MoviesContract.Videos.COLUMN_Name + " TEXT, " +
                MoviesContract.Videos.COLUMN_Key + " TEXT " +
                //" FOREIGN KEY ( " + MoviesContract.Videos.COLUMN_MovieID + " ) REFERENCES " + MoviesContract.Movies.TABLE_NAME + "  ( " + MoviesContract.Movies.COLUMN_MovieID + ")" +
                ");";
        db.execSQL(CREATE_MOVIES_VIDEOS_TABLE);

        String CREATE_MOVIES_REVIEWS_TABLE = "CREATE TABLE " + MoviesContract.Reviews.TABLE_NAME + " ( " +
                MoviesContract.Reviews._ID + " INTEGER AUTO INCREMENT, " +
                MoviesContract.Reviews.COLUMN_MovieID + " INTEGER, " +
                MoviesContract.Reviews.COLUMN_Author + " TEXT, " +
                MoviesContract.Reviews.COLUMN_Content + " TEXT " +
                //" FOREIGN KEY ( " + MoviesContract.Favourites.COLUMN_MovieID + " ) REFERENCES " + MoviesContract.Movies.TABLE_NAME + "  ( " + MoviesContract.Movies.COLUMN_MovieID + ")" +
                ");";
        db.execSQL(CREATE_MOVIES_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesSorting.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.Favourites.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.Reviews.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.Videos.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.Movies.TABLE_NAME);
        onCreate(db);
    }
}

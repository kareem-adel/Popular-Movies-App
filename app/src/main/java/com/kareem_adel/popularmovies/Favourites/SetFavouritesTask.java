package com.kareem_adel.popularmovies.Favourites;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.kareem_adel.popularmovies.DataSource.Database.MoviesContract;

public class SetFavouritesTask extends AsyncTask<String, Void, Void> {

    Context mContext;

    public SetFavouritesTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        String set_unset = params[0];
        String MovieID = params[1];

        switch (set_unset) {
            case "set": {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MoviesContract.Favourites.COLUMN_MovieID, MovieID);
                mContext.getContentResolver().insert(MoviesContract.Favourites.FavouritesUri(), contentValues);
                break;
            }
            case "unset": {
                mContext.getContentResolver().delete(MoviesContract.Favourites.FavouritesUri(), MoviesContract.Favourites.COLUMN_MovieID + " = ?", new String[]{MovieID});
                break;
            }
        }
        return null;
    }
}

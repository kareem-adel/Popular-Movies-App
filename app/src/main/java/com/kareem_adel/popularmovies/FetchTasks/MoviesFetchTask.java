package com.kareem_adel.popularmovies.FetchTasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.kareem_adel.popularmovies.DataSource.Database.MoviesContract;
import com.kareem_adel.popularmovies.DataSource.MovieAPIs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MoviesFetchTask extends AsyncTask<String, Void, Void> {

    Context mContext;


    public MoviesFetchTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        String APIParam = params[0];

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = MovieAPIs.BuildMoviesAPI(APIParam);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            if (inputStream == null) {
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            if (stringBuffer.length() == 0) {
                return null;
            }

            //ContentValues[] contentValues = ParseMoviesObject(stringBuffer.toString(), APIParam);
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            ContentValues[] MoviesValues = new ContentValues[jsonArray.length()];
            ContentValues[] MoviesRankingsValues = new ContentValues[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject movieResultJsonObject = (JSONObject) jsonArray.get(i);
                ContentValues contentValue = new ContentValues();
                ContentValues contentRankingValue = new ContentValues();
                contentValue.put(MoviesContract.Movies.COLUMN_MovieID, movieResultJsonObject.getString("id"));
                contentValue.put(MoviesContract.Movies.COLUMN_MovieName, movieResultJsonObject.getString("title"));
                contentValue.put(MoviesContract.Movies.COLUMN_Description, movieResultJsonObject.getString("overview"));
                contentValue.put(MoviesContract.Movies.COLUMN_Poster, movieResultJsonObject.getString("poster_path"));
                contentValue.put(MoviesContract.Movies.COLUMN_Rating, movieResultJsonObject.getDouble("vote_average"));

                contentValue.put(MoviesContract.Movies.COLUMN_Year, movieResultJsonObject.getString("release_date"));
                MoviesValues[i] = contentValue;

                contentRankingValue.put(MoviesContract.MoviesSorting.COLUMN_MovieID, movieResultJsonObject.getString("id"));
                contentRankingValue.put(MoviesContract.MoviesSorting.COLUMN_MovieFeedRanking, i + 1);
                contentRankingValue.put(MoviesContract.MoviesSorting.COLUMN_Type, APIParam);
                MoviesRankingsValues[i] = contentRankingValue;
            }


            mContext.getContentResolver().bulkInsert(MoviesContract.Movies.MoviesFeedUri(), MoviesValues);
            mContext.getContentResolver().delete(MoviesContract.MoviesSorting.MoviesSortingUri(), MoviesContract.MoviesSorting.COLUMN_Type + " = ?", new String[]{APIParam});
            mContext.getContentResolver().bulkInsert(MoviesContract.MoviesSorting.MoviesSortingUri(), MoviesRankingsValues);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /*
    public static ReturnMoviesObject ParseMoviesObject(String ReturnMoviesString) throws JSONException {
        ReturnMoviesObject returnMoviesObject = new ReturnMoviesObject();
        JSONObject jsonObject = new JSONObject(ReturnMoviesString);
        returnMoviesObject.setPage(jsonObject.getInt("page"));
        returnMoviesObject.setTotal_results(jsonObject.getInt("total_results"));
        returnMoviesObject.setTotal_pages(jsonObject.getInt("total_pages"));
        returnMoviesObject.setResults(new ArrayList<MovieResult>());
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject movieResultJsonObject = (JSONObject) jsonArray.get(i);
            MovieResult movieResult = new MovieResult();
            movieResult.setPoster_path(movieResultJsonObject.getString("poster_path"));
            movieResult.setAdult(movieResultJsonObject.getBoolean("adult"));
            movieResult.setOverview(movieResultJsonObject.getString("overview"));
            movieResult.setRelease_date(movieResultJsonObject.getString("release_date"));
            movieResult.setOriginal_title(movieResultJsonObject.getString("original_title"));
            movieResult.setOriginal_language(movieResultJsonObject.getString("original_language"));
            movieResult.setTitle(movieResultJsonObject.getString("title"));
            movieResult.setBackdrop_path(movieResultJsonObject.getString("backdrop_path"));
            movieResult.setPopularity(movieResultJsonObject.getDouble("popularity"));
            movieResult.setVote_count(movieResultJsonObject.getInt("vote_count"));
            movieResult.setVideo(movieResultJsonObject.getBoolean("video"));
            movieResult.setVote_average(movieResultJsonObject.getDouble("vote_average"));
            returnMoviesObject.getResults().add(movieResult);
        }
        return returnMoviesObject;

    }
    */
}

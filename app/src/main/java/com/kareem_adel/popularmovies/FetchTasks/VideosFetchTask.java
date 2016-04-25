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

public class VideosFetchTask extends AsyncTask<String, Void, Void> {

    Context mContext;

    public VideosFetchTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        String APIParam = params[0];

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = MovieAPIs.BuildVideosAPI(APIParam);
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

            ContentValues[] contentValues = ParseMoviesObject(stringBuffer.toString(), APIParam);

            mContext.getContentResolver().delete(MoviesContract.Videos.VideosUri(), MoviesContract.Videos.COLUMN_MovieID + " = ?", new String[]{APIParam});
            mContext.getContentResolver().bulkInsert(MoviesContract.Videos.VideosUri(), contentValues);
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


    public static ContentValues[] ParseMoviesObject(String ReturnMoviesString, String APIParam) throws JSONException {

        JSONObject jsonObject = new JSONObject(ReturnMoviesString);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        ContentValues[] contentValuesVector = new ContentValues[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject movieResultJsonObject = (JSONObject) jsonArray.get(i);
            ContentValues contentValue = new ContentValues();
            contentValue.put(MoviesContract.Videos.COLUMN_MovieID, APIParam);
            contentValue.put(MoviesContract.Videos.COLUMN_Name, movieResultJsonObject.getString("name"));
            contentValue.put(MoviesContract.Videos.COLUMN_Key, movieResultJsonObject.getString("key"));
            contentValuesVector[i] = contentValue;
        }
        return contentValuesVector;

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

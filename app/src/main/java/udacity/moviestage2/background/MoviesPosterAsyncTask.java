package udacity.moviestage2.background;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import udacity.moviestage2.R;
import udacity.moviestage2.bean.MoviesData;

/**
 * Created by sunnyparihar on 20/08/15.
 */
public class MoviesPosterAsyncTask extends AsyncTask<String, Void, ArrayList<MoviesData>> {

    private final String MOVIES_ID = "id";
    private final String MOVIES_DESC = "overview";
    private final String MOVIES_TITLE = "title";
    private final String POSTER_PATH = "poster_path";
    private final String RELEASE_DATE = "release_date";
    private final String USER_RATING = "vote_average";
    private final String TOTAL_USER_COUNT = "vote_count";

    private ProgressDialog progressDialog;
    private Context mContext;

    private final String LOG_TAG = MoviesPosterAsyncTask.class.getSimpleName();
    private MoviesInterface moviesInterface;

    public MoviesPosterAsyncTask(MoviesInterface moviesInterface, Context context) {
        this.moviesInterface = moviesInterface;
        this.mContext = context;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCancelable(false);
    }

    @Override
    protected ArrayList<MoviesData> doInBackground(String... params) {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
        final String QUERY_PARAM = "sort_by";
        final String API_KEY = "api_key";

        try {

            Uri finalUri = Uri.parse(MOVIES_BASE_URL).buildUpon().appendQueryParameter(QUERY_PARAM, params[0])
                    .appendQueryParameter(API_KEY, params[1]).build();
            URL url = new URL(finalUri.toString());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            if (inputStream == null)
                return null;

            StringBuffer stringBuffer = new StringBuffer();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

            if (stringBuffer.length() == 0)
                return null;

            try {
                return parseMoviesData(stringBuffer.toString());
            } catch (JSONException e) {
                Log.e(LOG_TAG, "JSON Exception" + e);
            }


        } catch (IOException e) {
            Log.e(LOG_TAG, "IO Exception" + e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "IO Exception" + e);
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
        progressDialog.setMessage(mContext.getResources().getString(R.string.laod_movie_data));
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onPostExecute(ArrayList<MoviesData> moviesList) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (moviesList != null)
            moviesInterface.passMoviesData(moviesList);
    }

    private ArrayList<MoviesData> parseMoviesData(String json) throws JSONException {

        JSONObject parentObj = new JSONObject(json);
        JSONArray moviesArray = parentObj.getJSONArray("results");
        ArrayList<MoviesData> moviesList = new ArrayList<>();

        for (int i = 0; i < moviesArray.length(); i++) {

            // add data to movies
            MoviesData moviesData = new MoviesData();
            JSONObject movieObj = moviesArray.getJSONObject(i);
            moviesData.setDesc(movieObj.getString(MOVIES_DESC));
            moviesData.setImgPath(movieObj.getString(POSTER_PATH));
            moviesData.setMovie_id(movieObj.getInt(MOVIES_ID));
            moviesData.setRating(movieObj.getDouble(USER_RATING));
            moviesData.setReleaseDate(movieObj.getString(RELEASE_DATE));
            moviesData.setTitle(movieObj.getString(MOVIES_TITLE));
            moviesData.setVoteCount(movieObj.getInt(TOTAL_USER_COUNT));
            String img_path = movieObj.getString(POSTER_PATH);

            if (img_path != "null") {
                moviesList.add(moviesData);
            }
        }
        return moviesList;
    }
}

package com.example.phenomenon.popularmovies1.utilities;


import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Philip Okonkwo
 * Created on 3/30/2017.
 * For all network calls
 */

public class NetworkUtilities {
    private final static String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private final static String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    private final static String PARAM_KEY = "api_key";
    private final static String API_KEY = "60c5a684b38dafaea8e8296973ba0aeb"; //needs valid key to work

    /**
     * Builds the JSON URL
     * @param sortOrder: the type of sorting wanted
     * @return URL
     */
    public static URL buildJSONUrl(String sortOrder) {
        Uri myUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(sortOrder)
                .appendQueryParameter(PARAM_KEY, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(myUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds a poster image URL
     * @param posterPath: the path of the poster image
     * @return URL with the path appended
     */
    public static URL buildPosterUrl(String posterPath) {
        String myUri= POSTER_BASE_URL.concat(posterPath);
        URL url = null;
        try {
            url = new URL(myUri);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Fetches JSON data from a URL
     * @param url: url to fetch data from
     * @return jsonData: returns the string format of the data
     */

    public static String fetchData(URL url){
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType= MediaType.parse("application/octet_stream");
        RequestBody body = RequestBody.create(mediaType, "{}");
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response responses;
        String jsonData = null;

        try {
            responses = client.newCall(request).execute();
            jsonData = responses.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonData;

    }

    /**
     * Makes movie object from json url
     * @param url same as above
     * @return
     */
    //public static String makeMovies(URL url){
    public static ArrayList<MyMovie> makeMovies(URL url){
        //URL u=url; //for debugging
        ArrayList<MyMovie> madeMovies = new ArrayList<MyMovie>();
        String result= null;
            try {
                result=fetchData(url);


            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                JSONObject jObj = new JSONObject(result);
                JSONArray JResults = jObj.getJSONArray("results");
                for (int i = 0; i < JResults.length(); i++) {
                    JSONObject obj = JResults.getJSONObject(i);
                    String posterURL = obj.getString("poster_path");
                    String synopsis = obj.getString("overview");
                    String releaseDate = obj.getString("release_date");
                    String title = obj.getString("original_title");
                    Double rating = obj.getDouble("vote_average");
                    Long id = obj.getLong("id");

                    MyMovie myMovie = new MyMovie(posterURL, synopsis, releaseDate,
                            title, rating, id);
                    //MovieContent.MOVIES.add(myMovie);
                    madeMovies.add(myMovie);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return madeMovies;
    }

}

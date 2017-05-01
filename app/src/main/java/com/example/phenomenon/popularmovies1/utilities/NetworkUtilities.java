package com.example.phenomenon.popularmovies1.utilities;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.example.phenomenon.popularmovies1.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
    //https://api.themoviedb.org/3/movie/321612/videos?api_key=abcdef

    private final static String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private final static String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    private final static String TRAILER_POSTER_BASE_URL="https://i.ytimg.com/vi/";
    private final static String YOUTUBE_TRAILER_BASE_URL="https://www.youtube.com/watch";
    private final static String PARAM_KEY = "api_key";
    private final static String API_KEY = BuildConfig.MY_MOVIE_DB_API_TOKEN; //needs valid key to work
    private static OkHttpClient client= new OkHttpClient();

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



    public static URL buildTrailerUrl(String MovieID) {
        Uri myUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(MovieID)
                .appendPath("videos")
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

    public static URL buildReviewUrl(String MovieID) {
        Uri myUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(MovieID)
                .appendPath("reviews")
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



    public static URL buildYoutubeTrailerUrl(String trailerKey) {
        Uri myUri = Uri.parse(YOUTUBE_TRAILER_BASE_URL).buildUpon()
                .appendQueryParameter("v", trailerKey)
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

    public static URL buildTrailerPosterUrl(String key) {
        String myUri= TRAILER_POSTER_BASE_URL.concat(key).concat("/hqdefault.jpg");
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
        //OkHttpClient client = new OkHttpClient();
        /*OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();*/

        MediaType mediaType= MediaType.parse("application/octet_stream");
        //MediaType mediaType= MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, "{}");
        Request request = new Request.Builder()
                .url(url.toString())
                .get()
                .build();
        String jsonData=null;

        Log.v("FetchData_url", url.toString());

        try {
            Response responses = client.newCall(request).execute();
            jsonData = responses.body().string();
            Log.v("FetchData", jsonData);
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
                    madeMovies.add(myMovie);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return madeMovies;
    }


    public static ArrayList<Trailer> makeTrailers(URL url){
        ArrayList<Trailer> madeTrailers = new ArrayList<>();
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
                String trailerKey = obj.getString("key");
                Trailer myTrailer = new Trailer(trailerKey);
                madeTrailers.add(myTrailer);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return madeTrailers;
    }


    public static ArrayList<Review> makeReviews(URL url){
        ArrayList<Review> madeReviews = new ArrayList<>();
        String result= null;
        try {
            result=fetchData(url);
            Log.d("Result", result);

        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            JSONObject jObj = new JSONObject(result);
            JSONArray JResults = jObj.getJSONArray("results");
            for (int i = 0; i < JResults.length(); i++) {
                JSONObject obj = JResults.getJSONObject(i);
                String author = obj.getString("author");
                String content = obj.getString("content");
                Review myReview = new Review(author, content, null);
                madeReviews.add(myReview);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return madeReviews;
    }

    public static void loadImageFromFile(String name, ImageView imgView)
    {
        String path= "/data/data/com.example.phenomenon.popularmovies1/app_imageDir";

        try {
            File f=new File(path, name+".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            //ImageView img=(ImageView)findViewById(R.id.imgPicker);
            imgView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

}

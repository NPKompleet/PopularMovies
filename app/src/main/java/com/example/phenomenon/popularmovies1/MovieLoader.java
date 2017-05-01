package com.example.phenomenon.popularmovies1;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;


import com.example.phenomenon.popularmovies1.data.FavoriteMoviesContract;
import com.example.phenomenon.popularmovies1.utilities.MyMovie;
import com.example.phenomenon.popularmovies1.utilities.NetworkUtilities;

import java.net.URL;
import java.util.ArrayList;

import static com.example.phenomenon.popularmovies1.MainActivity.LOADER_QUERY;

/**
 * @author PHENOMENON
 */

public class MovieLoader extends AsyncTaskLoader {
    private Bundle args;

    public MovieLoader(Context context, Bundle bundle){
        super(context);
        args= bundle;
    }

    @Override
    protected void onStartLoading() {
        if (args == null) return;
        super.onStartLoading();
    }


    @Override
    public ArrayList<MyMovie> loadInBackground() {
        if(args==null) return null;
        String param= args.getString(LOADER_QUERY);
        if (param== "favorites"){
            ArrayList<MyMovie> savedMovies= new ArrayList<>();

           Cursor cus= getContext().getContentResolver().query(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    FavoriteMoviesContract.FavoriteMoviesEntry._ID);
            if (cus!= null & cus.moveToFirst()) {
                int idColumn = cus.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID);
                int titleColumn = cus.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_TITLE);
                int urlColumn = cus.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_POSTERURL);
                int synColumn = cus.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_SYNOPSIS);
                int dateColumn = cus.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_DATE);
                int rateColumn = cus.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_RATING);

                do {

                    long id = cus.getLong(idColumn);
                    String title= cus.getString(titleColumn);
                    String url= cus.getString(urlColumn);
                    String synopsis= cus.getString(synColumn);
                    String date= cus.getString(dateColumn);
                    double rating= cus.getDouble(rateColumn);

                    MyMovie movie = new MyMovie(url, synopsis, date, title, rating, id);
                    savedMovies.add(movie);

                } while (cus.moveToNext());
            }
            cus.close();
            return savedMovies;
        }
        URL url = NetworkUtilities.buildJSONUrl(param);
        return NetworkUtilities.makeMovies(url);
    }
}

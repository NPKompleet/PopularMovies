package com.example.phenomenon.popularmovies1;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.example.phenomenon.popularmovies1.utilities.NetworkUtilities;
import com.example.phenomenon.popularmovies1.utilities.Trailer;

import java.net.URL;
import java.util.ArrayList;

import static com.example.phenomenon.popularmovies1.DetailActivity.TRAILER_LOADER_QUERY;

/**
 * Created by PHENOMENON on 4/22/2017.
 */

public class TrailerLoader extends AsyncTaskLoader<ArrayList<Trailer>> {
    private Bundle args;

    public TrailerLoader(Context context, Bundle bundle) {
        super(context);
        args= bundle;
    }

    @Override
    public ArrayList<Trailer> loadInBackground() {
        if(args==null) return null;
        URL url = NetworkUtilities.buildTrailerUrl(String.valueOf(args.getLong(TRAILER_LOADER_QUERY)));
        return NetworkUtilities.makeTrailers(url);
    }
}

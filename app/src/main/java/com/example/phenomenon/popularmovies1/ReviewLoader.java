package com.example.phenomenon.popularmovies1;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.example.phenomenon.popularmovies1.utilities.NetworkUtilities;
import com.example.phenomenon.popularmovies1.utilities.Review;
import com.example.phenomenon.popularmovies1.utilities.Trailer;

import java.net.URL;
import java.util.ArrayList;

import static com.example.phenomenon.popularmovies1.DetailActivity.REVIEW_LOADER_QUERY;

/**
 * Created by PHENOMENON on 4/22/2017.
 */

public class ReviewLoader extends AsyncTaskLoader<ArrayList<Review>> {
    Bundle args;
    public ReviewLoader(Context context, Bundle bundle) {
        super(context);
        args= bundle;
    }

    @Override
    public ArrayList<Review> loadInBackground() {
        if(args==null) return null;
        URL url = NetworkUtilities.buildReviewUrl(String.valueOf(args.getLong(REVIEW_LOADER_QUERY)));
        return NetworkUtilities.makeReviews(url);
    }
}

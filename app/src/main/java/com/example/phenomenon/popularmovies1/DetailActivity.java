package com.example.phenomenon.popularmovies1;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phenomenon.popularmovies1.data.FavoriteMoviesContract;
import com.example.phenomenon.popularmovies1.utilities.MyMovie;
import com.example.phenomenon.popularmovies1.utilities.NetworkUtilities;
import com.example.phenomenon.popularmovies1.utilities.Review;
import com.example.phenomenon.popularmovies1.utilities.Trailer;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.phenomenon.popularmovies1.MainActivity.loadFavorite;
import static com.example.phenomenon.popularmovies1.utilities.NetworkUtilities.buildPosterUrl;
import static com.example.phenomenon.popularmovies1.utilities.NetworkUtilities.loadImageFromFile;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Trailer>>{
    final String PARCEL_KEY= "MOVIE";
    TrailerAdapter trailerAdapter;
    ReviewAdapter reviewAdapter;
    ArrayList<Trailer> myTrailers= new ArrayList<>();
    ArrayList<Review> myReviews= new ArrayList<>();
    LinearLayoutManager lLm;
    LinearLayoutManager revLM;
    MyMovie movie;
    ImageView imgView;
    ImageView fav;
    static final String TRAILER_LOADER_QUERY = "query";
    static final String REVIEW_LOADER_QUERY = "query";
    static final int TRAILER_SEARCH_LOADER= 12;
    static final int REVIEW_SEARCH_LOADER= 13;
    boolean isFavorite;
    final String TRAILER_LIST= "Trailers";
    final String REVIEW_LIST= "Reviews";

    private LoaderManager.LoaderCallbacks<ArrayList<Review>> reviewLoaderListener =
            new LoaderManager.LoaderCallbacks<ArrayList<Review>>(){

                @Override
                public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {
                    Log.d("Review Loader", "Loader created");
                    return new ReviewLoader(DetailActivity.this, args);
                }

                @Override
                public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {
                    if (data !=null){
                        reviewAdapter.swapData(data);
                    }

                }

                @Override
                public void onLoaderReset(Loader<ArrayList<Review>> loader) {

                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //ActionBar actionBar= getSupportActionBar();
        this.setSupportActionBar(toolbar);
        ActionBar actionBar= this.getSupportActionBar();
        imgView= (ImageView) findViewById(R.id.detail_image);
        TextView dView= (TextView) findViewById(R.id.detail_year);
        TextView tView= (TextView) findViewById(R.id.detail_title);
        TextView rView= (TextView) findViewById(R.id.detail_rating);
        TextView sView= (TextView) findViewById(R.id.detail_synopsis);
        fav= (ImageView) findViewById(R.id.detail_favorite);
        RecyclerView tRV= (RecyclerView) findViewById(R.id.rv_trailer);
        RecyclerView rRV= (RecyclerView) findViewById(R.id.rv_review);
        final FloatingActionButton fab= (FloatingActionButton) findViewById(R.id.detail_fab);

        if (actionBar !=null) {actionBar.setDisplayHomeAsUpEnabled(true);}
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent= getIntent();
        if (intent.hasExtra(PARCEL_KEY)){
            movie = intent.getParcelableExtra(PARCEL_KEY);
            if (loadFavorite){
                loadImageFromFile(movie.getTitle(), imgView);
            } else{
                URL imgURL= buildPosterUrl(movie.getPosterUrl());
                Picasso.with(this).load(imgURL.toString()).into(imgView);
            }
            dView.setText(movie.getReleaseDate());
            tView.setText(movie.getTitle());
            rView.setText(movie.getUserRating() + getString(R.string.rating_max));
            sView.setText(movie.getSynopsis());
        }

        lLm= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tRV.setLayoutManager(lLm);
        trailerAdapter= new TrailerAdapter(myTrailers);
        tRV.setAdapter(trailerAdapter);

        //this linear manager will ely on the enclosing Scrollview
        //for scrolling
        revLM= new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rRV.setLayoutManager(revLM);
        reviewAdapter= new ReviewAdapter(myReviews);
        rRV.setAdapter(reviewAdapter);

        isFavorite = checkFavorite(movie.getId());
        if (isFavorite){
            fav.setImageResource(R.drawable.ic_favorite_black_24dp);
        }

        if (savedInstanceState != null){
            //ArrayList<Trailer> newTrailerArray= savedInstanceState.getParcelableArrayList(TRAILER_LIST);
            myTrailers= savedInstanceState.getParcelableArrayList(TRAILER_LIST);
            trailerAdapter.swapData(myTrailers);
            myReviews= savedInstanceState.getParcelableArrayList(REVIEW_LIST);
            reviewAdapter.swapData(myReviews);

        } else{
            getTrailers(movie.getId());
            getReviews(movie.getId());
        }

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //fab.setImageResource(R.drawable.ic_star_black_24dp);
                //saveFavorite();
                Intent shareInt= new Intent(Intent.ACTION_SEND);
                shareInt.setType("text/plain");
                URL url = NetworkUtilities.buildYoutubeTrailerUrl(myTrailers.get(0).getKey());
                shareInt.putExtra(Intent.EXTRA_TEXT,
                        "Hey, check out this cool movie "+ url.toString());
                startActivity(shareInt);
            }
        });


    }


    @Override
    protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        //Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();
        bundle.putParcelableArrayList(TRAILER_LIST, myTrailers);
        bundle.putParcelableArrayList(REVIEW_LIST, myReviews);
    }


    public void getTrailers(Long movieID){
        Bundle loaderBundle= new Bundle();
        loaderBundle.putLong(TRAILER_LOADER_QUERY, movieID);
        LoaderManager loaderManager= getSupportLoaderManager();
        Loader<ArrayList<Trailer>> loader= loaderManager.getLoader(TRAILER_SEARCH_LOADER);
        if (isOnline()) {
            if (loader == null) {
                loaderManager.initLoader(TRAILER_SEARCH_LOADER, loaderBundle, this).forceLoad();
            } else {
                loaderManager.restartLoader(TRAILER_SEARCH_LOADER, loaderBundle, this).forceLoad();
            }
        }
    }



    public void getReviews(Long movieID){
        Bundle loaderBundle= new Bundle();
        loaderBundle.putLong(REVIEW_LOADER_QUERY, movieID);
        LoaderManager loaderManager= getSupportLoaderManager();
        Loader<ArrayList<Review>> loader= loaderManager.getLoader(REVIEW_SEARCH_LOADER);
        if (isOnline()) {
            if (loader == null) {
                loaderManager.initLoader(REVIEW_SEARCH_LOADER, loaderBundle, reviewLoaderListener).forceLoad();
                //loaderManager.initLoader(REVIEW_SEARCH_LOADER, loaderBundle, this).forceLoad();
            } else {
                loaderManager.restartLoader(REVIEW_SEARCH_LOADER, loaderBundle, reviewLoaderListener).forceLoad();
                //loaderManager.restartLoader(REVIEW_SEARCH_LOADER, loaderBundle, this).forceLoad();
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<Trailer>> onCreateLoader(int id, Bundle args) {
        return new TrailerLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> trailerArrayList) {
        if (trailerArrayList !=null){
            trailerAdapter.swapData(trailerArrayList);
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {

    }



    public void saveFavorite(View view){
        //if it's already a favoite, it is removed instead
        if(isFavorite) {
            Uri uri= FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI;
            uri=uri.buildUpon().appendPath(movie.getId().toString()).build();
            getContentResolver().delete(uri, null, null);

            File dir = getFilesDir();
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_APPEND);
            File file = new File(directory, movie.getTitle()+".jpg");
            boolean deleted = file.delete();
            if (deleted) Toast.makeText(this, "Image deleted", Toast.LENGTH_SHORT).show();

            fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            return;
        }
        Long movieId= movie.getId();
        String movieTitle= movie.getTitle();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imgView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        String posterURL = saveToInternalStorage(bitmap, movieTitle);
        Toast.makeText(this, posterURL, Toast.LENGTH_SHORT).show();

        String synopsis= movie.getSynopsis();
        String date= movie.getReleaseDate();
        Double rating = movie.getUserRating();

        ContentValues contentValues= new ContentValues();
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID, movieId.intValue());
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_TITLE, movieTitle);
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_POSTERURL, movieTitle);
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_SYNOPSIS, synopsis);
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_DATE, date);
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_RATING, rating.toString());

        Uri uri= getContentResolver().insert(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, contentValues);

        if (uri != null){
            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
        }

        fav.setImageResource(R.drawable.ic_favorite_black_24dp);
        Toast.makeText(this, "Saved to Favorites", Toast.LENGTH_SHORT).show();


    }



    private String saveToInternalStorage(Bitmap bitmapImage, String name){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_APPEND);
        // Create imageDir
        File mypath=new File(directory, name+".jpg");


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } /*finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        //return directory.getAbsolutePath();
        return mypath.getAbsolutePath();
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }


    private boolean checkFavorite(Long mId) {
        // Check if the movie is already save as a favorite using its id
        Uri uri = FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI
                .buildUpon()
                .appendPath(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID)
                .build();
        //String[] projection = new String[] {FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID};
        /*String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs,
                sortOrder);*/
        Cursor cursor = getContentResolver().query(uri, null, null, null,
                null);
        ArrayList<Integer> myList= new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            //list= new int[cursor.getCount()];
            for (int i = 0; i < cursor.getCount(); i++){
                int movId = cursor.getInt(cursor
                        .getColumnIndexOrThrow(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID));
                myList.add(movId);
                cursor.moveToNext();
            }
            //close the cursor
            cursor.close();
        }
        return (myList.contains(mId.intValue()));
    }

}

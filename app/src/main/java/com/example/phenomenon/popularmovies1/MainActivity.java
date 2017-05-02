package com.example.phenomenon.popularmovies1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.phenomenon.popularmovies1.utilities.MyMovie;
import com.example.phenomenon.popularmovies1.utilities.NetworkUtilities;


import java.net.URL;
import java.util.ArrayList;

/**
 * @author Philip Okonkwo
 * Created on 3/30/2017.
 */
public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener,
        LoaderManager.LoaderCallbacks<ArrayList<MyMovie>>{

    RecyclerView mMovieGrid;
    ProgressBar mPBar;
    ArrayList<MyMovie> myMovieArray = new ArrayList<>();
    MovieAdapter mAdapter;
    GridLayoutManager gridLayoutManager;
    final String MOVIE_LIST= "MOVIE_LIST";
    final String SORT_OPTION= "SORT";
    static final String LOADER_QUERY = "query";
    static final int MOVIE_SEARCH_LOADER= 11;
    int sortOption=0;
    static boolean loadFavorite= false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieGrid = (RecyclerView) findViewById(R.id.rv_movies);
        mPBar= (ProgressBar) findViewById(R.id.progress_bar);

        //GridLayoutManager gridLayoutManager= new GridLayoutManager(this, 2);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridLayoutManager= new GridLayoutManager(this, 2);
        }
        else{
            gridLayoutManager= new GridLayoutManager(this, 3);
        }

        mMovieGrid.setLayoutManager(gridLayoutManager);
        //mMovieGrid.setHasFixedSize(true);

        //MovieAdapter mAdapter = new MovieAdapter(MovieContent.MOVIES,this);
        mAdapter = new MovieAdapter(myMovieArray,this);
        mMovieGrid.setAdapter(mAdapter);


        if (savedInstanceState != null){
            //Toast.makeText(this, "oh oh", Toast.LENGTH_SHORT).show();
            sortOption= savedInstanceState.getInt(SORT_OPTION);
            //ArrayList<MyMovie> newMovieArray= savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            myMovieArray= savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            mAdapter.swapData(myMovieArray);

        } else{getMovieBySortOrder("popular");}

    }


    @Override
    protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        //Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();
        bundle.putInt(SORT_OPTION, sortOption);
        bundle.putParcelableArrayList(MOVIE_LIST, myMovieArray);
    }

    /**
     * Gets movie from movie endpoints by sortorder
     * and calls the task to be performed on another thread
     * @param sortOrder: movie sort order
     */
    public void getMovieBySortOrder(String sortOrder){
        //new MovieDBQueryTask().execute(NetworkUtilities.buildJSONUrl(sortOrder));
        Bundle loaderBundle= new Bundle();
        loaderBundle.putString(LOADER_QUERY, sortOrder);
        LoaderManager loaderManager= getSupportLoaderManager();
        Loader<ArrayList<MyMovie>> loader= loaderManager.getLoader(MOVIE_SEARCH_LOADER);
        if (isOnline()){
            if (loader==null) {
                loaderManager.initLoader(MOVIE_SEARCH_LOADER, loaderBundle, this).forceLoad();
            }else{
                loaderManager.restartLoader(MOVIE_SEARCH_LOADER, loaderBundle, this).forceLoad();
            }
            //Toast.makeText(MainActivity.this,buildJSONUrl("top_rated").toString(),Toast.LENGTH_SHORT).show();
        } else {
            if (loadFavorite){
                if (loader==null) {
                    loaderManager.initLoader(MOVIE_SEARCH_LOADER, loaderBundle, this).forceLoad();
                }else{
                    loaderManager.restartLoader(MOVIE_SEARCH_LOADER, loaderBundle, this).forceLoad();
                }
                //do something here
            } else {
                Toast.makeText(MainActivity.this, "No network, please try again", Toast.LENGTH_SHORT).show();
            }

        }
    }



    @Override
    public void onListItemClicked(int clickedItemPosition) {
        MyMovie movie= myMovieArray.get(clickedItemPosition);
        final String PARCEL_KEY= "MOVIE";
        Intent intent= new Intent(this, DetailActivity.class);
        intent.putExtra(PARCEL_KEY, movie);
        startActivity(intent);
    }

    @Override
    public Loader<ArrayList<MyMovie>> onCreateLoader(int id, final Bundle args) {
        mPBar.setVisibility(View.VISIBLE);
        //Log.v("loadin Oncreate", args.getString(LOADER_QUERY));
        return new MovieLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MyMovie>> loader, ArrayList<MyMovie> movieArrayList) {
        if (movieArrayList !=null){
            mAdapter.swapData(movieArrayList);
            mPBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MyMovie>> loader) {

    }



    // The background thread execution class
    //class no longer in use. AsyncTaskLoader favored
    class MovieDBQueryTask extends AsyncTask<URL, Void, ArrayList<MyMovie>>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mPBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected ArrayList<MyMovie> doInBackground(URL... urls) {
            URL url= urls[0];
            return NetworkUtilities.makeMovies(url);
        }

        @Override
        protected void onPostExecute(ArrayList<MyMovie> movieArrayList){
            if (movieArrayList !=null){
                mAdapter.swapData(movieArrayList);
                mPBar.setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (sortOption !=0){
            MenuItem item= menu.findItem(sortOption);
            item.setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.mostPopular:
                //
                sortOption= itemId;
                loadFavorite= false;
                if (!item.isChecked()) item.setChecked(true);
                getMovieBySortOrder("popular");
                break;

            case R.id.upComing:
                //
                sortOption= itemId;
                loadFavorite= false;
                if (!item.isChecked()) item.setChecked(true);
                getMovieBySortOrder("upcoming");
                break;

            case R.id.topRated:
                //
                sortOption= itemId;
                loadFavorite= false;
                if (!item.isChecked()) item.setChecked(true);
                getMovieBySortOrder("top_rated");
                break;

            case R.id.favorites:
                //
                sortOption= itemId;
                loadFavorite= true;
                if (!item.isChecked()) item.setChecked(true);
                getMovieBySortOrder("favorites");
                break;

            case R.id.settings:
                Intent i= new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    //check for internet connectivity
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

}

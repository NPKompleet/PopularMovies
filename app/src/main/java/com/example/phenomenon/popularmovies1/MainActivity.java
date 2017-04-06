package com.example.phenomenon.popularmovies1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener{

    RecyclerView mMovieGrid;
    ProgressBar mPBar;
    ArrayList<MyMovie> myMovieArray = new ArrayList<>();
    MovieAdapter mAdapter;
    GridLayoutManager gridLayoutManager;


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
        getMovieBySortOrder("popular");


    }

    /**
     * Gets movie from movie endpoints by sortorder
     * and calls the task to be performed on another thread
     * @param sortOrder: movie sort order
     */
    public void getMovieBySortOrder(String sortOrder){
        if (isOnline()){
            new MovieDBQueryTask().execute(NetworkUtilities.buildJSONUrl(sortOrder));
            //Toast.makeText(MainActivity.this,buildJSONUrl("top_rated").toString(),Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "No network, please try again",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onListItemClicked(int clickedItemPosition) {
        MyMovie movie= myMovieArray.get(clickedItemPosition);
        final String PARCEL_KEY= "MOVIE";
        Intent intent= new Intent(this, DetailActivity.class);
        //MyMovie movie= MovieContent.MOVIES.get(clickedItemPosition);
        intent.putExtra(PARCEL_KEY, movie);
        startActivity(intent);
    }


     // The background thread execution class
    //class MovieDBQueryTask extends AsyncTask<URL, Void, String>{
    class MovieDBQueryTask extends AsyncTask<URL, Void, ArrayList<MyMovie>>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mPBar.setVisibility(View.VISIBLE);
        }


        @Override
        //protected String doInBackground(URL... urls) {
        protected ArrayList<MyMovie> doInBackground(URL... urls) {
            URL url= urls[0];
            return NetworkUtilities.makeMovies(url);
        }

        @Override
        protected void onPostExecute(ArrayList<MyMovie> movieArrayList){
            //if (s!=null && !s.equals("")){
            if (movieArrayList !=null){
                //MovieAdapter Adapter = new MovieAdapter(MovieContent.MOVIES, MainActivity.this);
                //mMovieGrid.swapAdapter(Adapter, true);
                mAdapter.swapData(movieArrayList);
                mPBar.setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        //String url;

        switch (itemId) {
            case R.id.mostPopular:
                //
                //MovieContent.MOVIES.clear();
                if (!item.isChecked()) item.setChecked(true);
                getMovieBySortOrder("popular");
                break;

            case R.id.upComing:
                //
                //MovieContent.MOVIES.clear();
                if (!item.isChecked()) item.setChecked(true);
                getMovieBySortOrder("upcoming");
                break;
            case R.id.topRated:
                //
                //MovieContent.MOVIES.clear();
                if (!item.isChecked()) item.setChecked(true);
                getMovieBySortOrder("top_rated");
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

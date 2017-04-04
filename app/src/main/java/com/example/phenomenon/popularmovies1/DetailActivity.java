package com.example.phenomenon.popularmovies1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phenomenon.popularmovies1.utilities.MovieContent;
import com.squareup.picasso.Picasso;

import java.net.URL;

import static com.example.phenomenon.popularmovies1.utilities.NetworkUtilities.buildPosterUrl;

public class DetailActivity extends AppCompatActivity {
    ImageView imgView;
    final String PARCEL_KEY= "MOVIE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //ActionBar actionBar= getSupportActionBar();
        setSupportActionBar(toolbar);
        imgView= (ImageView) findViewById(R.id.detail_image);
        TextView dView= (TextView) findViewById(R.id.detail_year);
        TextView tView= (TextView) findViewById(R.id.detail_title);
        TextView rView= (TextView) findViewById(R.id.detail_rating);
        TextView sView= (TextView) findViewById(R.id.detail_synopsis);

        //if (actionBar !=null) {actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent= getIntent();
        if (intent.hasExtra(PARCEL_KEY)){
            MovieContent.MyMovie movie = intent.getParcelableExtra(PARCEL_KEY);
            URL imgURL= buildPosterUrl(movie.getPosterUrl());
            Picasso.with(this).load(imgURL.toString()).into(imgView);
            dView.setText(movie.getReleaseDate());
            tView.setText(movie.getTitle());
            rView.setText(movie.getUserRating() + getString(R.string.rating_max));
            sView.setText(movie.getSynopsis());
        }

    }

}

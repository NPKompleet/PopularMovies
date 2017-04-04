package com.example.phenomenon.popularmovies1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.phenomenon.popularmovies1.utilities.MovieContent;
import com.example.phenomenon.popularmovies1.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;


/**
 * Created by PHENOMENON on 3/30/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.PosterHolder> {
    private final ArrayList<MovieContent.MyMovie> myMovies;
    private Context context;
    private final MovieItemClickListener mOnMovieClickListener;

    public interface MovieItemClickListener{
        void onListItemClicked(int clickedItemPosition);
    }

    public MovieAdapter(ArrayList<MovieContent.MyMovie> movies, MovieItemClickListener listener){
        myMovies = movies;
        mOnMovieClickListener= listener;
    }

    @Override
    public MovieAdapter.PosterHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_poster;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new PosterHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.PosterHolder holder, int position) {
        MovieContent.MyMovie movie= myMovies.get(position);
        URL imgURL= NetworkUtilities.buildPosterUrl(movie.getPosterUrl());
        Picasso.with(context).load(imgURL.toString()).into(holder.imageView);
        //Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return myMovies.size();
    }

    class PosterHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        public PosterHolder(View poster){
            super(poster);
            imageView= (ImageView) poster.findViewById(R.id.movie_poster_image);
            imageView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            mOnMovieClickListener.onListItemClicked(position);
            /*Intent intent= new Intent(context, DetailActivity.class);
            MovieContent.MyMovie movie= MovieContent.MOVIES.get(getLayoutPosition());
            intent.putExtra("MOVIE", movie);
            context.startActivity(intent);*/

        }
    }
}

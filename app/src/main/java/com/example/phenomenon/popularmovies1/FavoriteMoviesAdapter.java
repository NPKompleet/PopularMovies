package com.example.phenomenon.popularmovies1;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.phenomenon.popularmovies1.utilities.MyMovie;

import java.util.ArrayList;

/**
 * Created by PHENOMENON on 4/24/2017.
 */

public class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesAdapter.FavoriteMovieHolder> {
    ArrayList<MyMovie> myMovies;
    public FavoriteMoviesAdapter(ArrayList<MyMovie> movies){
        myMovies= movies;
    }

    @Override
    public FavoriteMoviesAdapter.FavoriteMovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(FavoriteMoviesAdapter.FavoriteMovieHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class FavoriteMovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        public FavoriteMovieHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.movie_poster_image);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
           //mOnMovieClickListener.onListItemClicked(position);
            /*Intent intent= new Intent(context, DetailActivity.class);
            MyMovie movie= MovieContent.MOVIES.get(getLayoutPosition());
            intent.putExtra("MOVIE", movie);
            context.startActivity(intent);*/

        }
    }
}

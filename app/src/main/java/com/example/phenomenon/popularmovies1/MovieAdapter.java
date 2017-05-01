package com.example.phenomenon.popularmovies1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.phenomenon.popularmovies1.utilities.MyMovie;
import com.example.phenomenon.popularmovies1.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.phenomenon.popularmovies1.MainActivity.loadFavorite;


/**
 * @author Philip Okonkwo
 * Created on 3/30/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.PosterHolder> {
    private ArrayList<MyMovie> myMovies;
    private Context context;
    private final MovieItemClickListener mOnMovieClickListener;

    public interface MovieItemClickListener{
        void onListItemClicked(int clickedItemPosition);
    }

    public MovieAdapter(ArrayList<MyMovie> movies, MovieItemClickListener listener){
        myMovies = movies;
        mOnMovieClickListener= listener;
    }

    public void swapData(ArrayList<MyMovie> newMovieList){
        myMovies.clear();
        myMovies.addAll(newMovieList);
        notifyDataSetChanged();
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
        MyMovie movie= myMovies.get(position);
        if (loadFavorite){
            NetworkUtilities.loadImageFromFile(movie.getPosterUrl(), holder.imageView);
            return;
        }
        URL imgURL= NetworkUtilities.buildPosterUrl(movie.getPosterUrl());
        //load image
        Picasso.with(context).load(imgURL.toString()).into(holder.imageView);
        //if (position==3){holder.imageView.setElevation(5);}

    }

    /*private void loadImageFromStorage(String name, ImageView imgView)
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

    }*/

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
            MyMovie movie= MovieContent.MOVIES.get(getLayoutPosition());
            intent.putExtra("MOVIE", movie);
            context.startActivity(intent);*/

        }
    }
}

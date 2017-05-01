package com.example.phenomenon.popularmovies1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phenomenon.popularmovies1.utilities.NetworkUtilities;
import com.example.phenomenon.popularmovies1.utilities.Trailer;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

/**
 * @author Philip C. Okonkwo
 * Created on 4/22/2017
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {
    private ArrayList<Trailer> myTrailers;
    private Context context;

    public TrailerAdapter(ArrayList<Trailer> trailers){
        myTrailers= trailers;
    }

    public void swapData(ArrayList<Trailer> newTrailerList){
        myTrailers.clear();
        myTrailers.addAll(newTrailerList);
        notifyDataSetChanged();
    }


    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIDForTrailerList= R.layout.trailer_list_item;
        LayoutInflater inflater= LayoutInflater.from(context);
        boolean attachImmediately= false;
        View view= inflater.inflate(layoutIDForTrailerList, parent, attachImmediately);
        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {
        Trailer trailer = myTrailers.get(position);
        URL url= NetworkUtilities.buildTrailerPosterUrl(trailer.getKey());
        //Log.v("Trailer url: ", url.toString());
        Picasso.with(context).load(url.toString())
                .resize(200, 200)
                .centerInside()
                .into(holder.imgView);
        holder.txtView.setText("Trailer "+ ++position);
    }

    @Override
    public int getItemCount() {
        return myTrailers.size();
    }


    public class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgView;
        TextView txtView;

        public TrailerHolder(View itemView) {
            super(itemView);
            imgView= (ImageView) itemView.findViewById(R.id.trailer_image);
            txtView= (TextView) itemView.findViewById(R.id.trailer_number);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Trailer trailer= myTrailers.get(getLayoutPosition());
            URL url= NetworkUtilities.buildYoutubeTrailerUrl(trailer.getKey());
            Intent i= new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
            context.startActivity(i);
        }
    }
}

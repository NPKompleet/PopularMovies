package com.example.phenomenon.popularmovies1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phenomenon.popularmovies1.utilities.Review;

import java.util.ArrayList;

/**
 * Created by PHENOMENON on 4/22/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    ArrayList<Review> myReviews;
    Context context;

    public ReviewAdapter(ArrayList<Review> reviews){
        myReviews= reviews;
    }

    public void swapData(ArrayList<Review> newReviewList){
        myReviews.clear();
        myReviews.addAll(newReviewList);
        notifyDataSetChanged();
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context= parent.getContext();
        int layoutId= R.layout.review_list_item;
        LayoutInflater inflater= LayoutInflater.from(context);
        View view= inflater.inflate(layoutId, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review review= myReviews.get(position);
        holder.txtAuthor.setText(review.getAuthor());
        holder.txtContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return myReviews.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder{
        TextView txtAuthor;
        TextView txtContent;

        public ReviewHolder(View itemView) {
            super(itemView);
            txtAuthor= (TextView) itemView.findViewById(R.id.review_author);
            txtContent= (TextView) itemView.findViewById(R.id.review_content);
        }
    }
}

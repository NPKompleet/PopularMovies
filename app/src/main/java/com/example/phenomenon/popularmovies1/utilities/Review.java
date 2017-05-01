package com.example.phenomenon.popularmovies1.utilities;

/**
 * Created by PHENOMENON on 4/22/2017.
 */

public class Review {
    String mAuthor;
    String mContent;
    String mURL;

    public Review(String author, String content, String url){
        mAuthor= author;
        mContent= content;
        mURL= url;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }
}

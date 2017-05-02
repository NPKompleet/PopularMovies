package com.example.phenomenon.popularmovies1.utilities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PHENOMENON on 4/22/2017.
 */

public class Review implements Parcelable{
    String mAuthor;
    String mContent;
    String mURL;

    public Review(String author, String content, String url){
        mAuthor= author;
        mContent= content;
        mURL= url;
    }

    protected Review(Parcel in) {
        mAuthor = in.readString();
        mContent = in.readString();
        mURL = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mAuthor);
        parcel.writeString(mContent);
        parcel.writeString(mURL);
    }
}

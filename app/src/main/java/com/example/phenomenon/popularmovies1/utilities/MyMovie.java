package com.example.phenomenon.popularmovies1.utilities;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * @author Philip Okonkwo
 * Created on 3/31/2017.
 */


public class MyMovie implements Parcelable{
   /* Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
    original title
    movie poster image thumbnail
    A plot synopsis (called overview in the api)
    user rating (called vote_average in the api)
    release date*/

    private Long mId;
    private String mTitle;
    private String mPosterUrl;
    private String mSynopsis;
    private String mReleaseDate;
    private Double mUserRating;

    public MyMovie(String posterUrl, String synopsis, String date, String title,  Double userRating, Long id){
        mId= id;
        mTitle= title;
        mPosterUrl=posterUrl;
        mSynopsis= synopsis;
        mReleaseDate= date;
        mUserRating= userRating;
    }

    public MyMovie(Parcel parcel){
        mPosterUrl= parcel.readString();
        mSynopsis= parcel.readString();
        mReleaseDate= parcel.readString();
        mTitle= parcel.readString();
        mUserRating= parcel.readDouble();
        mId= parcel.readLong();

    }


    public Long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public Double getUserRating() {
        return mUserRating;
    }

    //used when unparceling: creating the object
    public static final Parcelable.Creator<MyMovie> CREATOR= new Creator<MyMovie>() {
        @Override
        public MyMovie createFromParcel(Parcel parcel) {
            return new MyMovie(parcel);
        }

        @Override
        public MyMovie[] newArray(int i) {
            return new MyMovie[0];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    //write object values to destination parcel for storage
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(mPosterUrl);
        dest.writeString(mSynopsis);
        dest.writeString(mReleaseDate);
        dest.writeString(mTitle);
        dest.writeDouble(mUserRating);
        dest.writeLong(mId);
    }
}




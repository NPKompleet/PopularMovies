package com.example.phenomenon.popularmovies1.utilities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PHENOMENON on 4/22/2017.
 */

public class Trailer implements Parcelable{
    private String mKey;

    public Trailer(String key){
        mKey= key;
    }

    protected Trailer(Parcel in) {
        mKey = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public String getKey() {
        return mKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mKey);
    }
}

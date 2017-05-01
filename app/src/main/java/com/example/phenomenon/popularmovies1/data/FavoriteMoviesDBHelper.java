package com.example.phenomenon.popularmovies1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PHENOMENON on 4/18/2017.
 */

public class FavoriteMoviesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME= "FavoriteMoviesDB.db";
    private static final int DATABASE_VERSION= 1;

    public FavoriteMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_FAVORITE_MOVIES_TABLE= "CREATE TABLE " +
                FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME + " ("+
                FavoriteMoviesContract.FavoriteMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "+
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_POSTERURL + " TEXT, "+
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_SYNOPSIS + " TEXT, " +
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_DATE + " TEXT, " +
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_RATING + " TEXT NOT NULL" +
                ");";

        final String CREATE_FAVORITE_MOVIES_TRAILER_TABLE= "CREATE TABLE " +
                FavoriteMoviesContract.FavoriteMoviesTrailers.TABLE_NAME + " ("+
                FavoriteMoviesContract.FavoriteMoviesTrailers._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                FavoriteMoviesContract.FavoriteMoviesTrailers.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavoriteMoviesContract.FavoriteMoviesTrailers.COLUMN_TRAILER_IMAGE_LOCATION + " TEXT NOT NULL"+
                ");";

        final String CREATE_FAVORITE_MOVIES_REVIEW_TABLE= "CREATE TABLE " +
                FavoriteMoviesContract.FavoriteMoviesReviews.TABLE_NAME + " ("+
                FavoriteMoviesContract.FavoriteMoviesReviews._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                FavoriteMoviesContract.FavoriteMoviesReviews.COLUMN_MOVIE_ID +  " INTEGER NOT NULL, " +
                FavoriteMoviesContract.FavoriteMoviesReviews.COLUMN_MOVIE_REVIEW + " TEXT NOT NULL " +
                ");";

        sqLiteDatabase.execSQL(CREATE_FAVORITE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(CREATE_FAVORITE_MOVIES_TRAILER_TABLE);
        sqLiteDatabase.execSQL(CREATE_FAVORITE_MOVIES_REVIEW_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ FavoriteMoviesContract.FavoriteMoviesTrailers.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ FavoriteMoviesContract.FavoriteMoviesReviews.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

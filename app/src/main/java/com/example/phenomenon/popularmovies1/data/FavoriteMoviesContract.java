package com.example.phenomenon.popularmovies1.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by PHENOMENON on 4/18/2017.
 */

public class FavoriteMoviesContract {
    private FavoriteMoviesContract(){}

    public static final String AUTHORITY = "com.example.phenomenon.popularmovies1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITE_MOVIES= "FavoriteMovies";
    public static final String PATH_FAVORITE_MOVIES_TRAILER= "FavoriteMoviesTrailer";
    public static final String PATH_FAVORITE_MOVIES_REVIEW= "FavoriteMoviesReview";

    /**
     * Follows the structure of the MyMovie object
     * @see com.example.phenomenon.popularmovies1.utilities.MyMovie
     */
    public static final class FavoriteMoviesEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String TABLE_NAME= "FavoriteMovies";
        public static final String COLUMN_MOVIE_ID= "Movie_ID";
        public static final String COLUMN_MOVIE_TITLE= "Movie_Title";
        public static final String COLUMN_MOVIE_POSTERURL= "Movie_PosterURL";
        public static final String COLUMN_MOVIE_SYNOPSIS= "Movie_Synopsis";
        public static final String COLUMN_MOVIE_DATE= "Movie_Date";
        public static final String COLUMN_MOVIE_RATING= "Movie_Rating";

    }

    public static final class FavoriteMoviesTrailers implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES_TRAILER).build();

        public static final String TABLE_NAME = "FavoriteMoviesTrailer";
        public static final String COLUMN_MOVIE_ID = "Movie_ID";
        public static final String COLUMN_TRAILER_IMAGE_LOCATION = "File_Location";
    }

    public static final class FavoriteMoviesReviews implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES_REVIEW).build();

        public static final String TABLE_NAME= "FavoriteMoviesReview";
        public static final String COLUMN_MOVIE_ID = "Movie_ID";
        public static final String COLUMN_MOVIE_REVIEW = "Movie_Review";
    }
}

package com.example.phenomenon.popularmovies1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Switch;
import android.widget.Toast;


/**
 * Created by PHENOMENON on 4/28/2017.
 */

public class FavoriteMoviesProvider extends ContentProvider {

    public static final int FAVORITE_MOVIES= 100;
    public static final int FAVORITE_MOVIES_WITH_ID= 101;
    public static final int FAVORITE_MOVIES_WITH_COLUMN= 102;
    public static final int FAVORITE_MOVIES_TRAILER= 200;
    public static final int FAVORITE_MOVIES_REVIEW= 300;

    private FavoriteMoviesDBHelper moviesDBHelper;

    public static final UriMatcher sUriMatcher= buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher= new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITE_MOVIES, FAVORITE_MOVIES);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITE_MOVIES + "/#", FAVORITE_MOVIES_WITH_ID);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITE_MOVIES + "/*", FAVORITE_MOVIES_WITH_COLUMN);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITE_MOVIES_TRAILER, FAVORITE_MOVIES_TRAILER);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITE_MOVIES_REVIEW, FAVORITE_MOVIES_REVIEW);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        Context context= getContext();
        moviesDBHelper= new FavoriteMoviesDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db= moviesDBHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor= null;
        switch (match){
            case FAVORITE_MOVIES:
                retCursor =db.query(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case FAVORITE_MOVIES_WITH_COLUMN:
                String column= uri.getLastPathSegment();
                String[] mProjection= new String[]{column};
                retCursor =db.query(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
                        mProjection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case FAVORITE_MOVIES_WITH_ID:
                break;
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db= moviesDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri retUri= null;
        switch (match){
            case FAVORITE_MOVIES:
                long id = db.insert(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME, null, contentValues);
                if (id > 0){
                    retUri= ContentUris.withAppendedId(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, id);
                } else{
                    throw new SQLException("no insert "+ uri);
                }
                break;

            case FAVORITE_MOVIES_TRAILER:
                long trailer_id = db.insert(FavoriteMoviesContract.FavoriteMoviesTrailers.TABLE_NAME, null, contentValues);
                if (trailer_id > 0){
                    retUri= ContentUris.withAppendedId(FavoriteMoviesContract.FavoriteMoviesTrailers.CONTENT_URI, trailer_id);
                } else{
                    throw new SQLException("no insert "+ uri);
                }
                break;

            case FAVORITE_MOVIES_REVIEW:
                long review_id = db.insert(FavoriteMoviesContract.FavoriteMoviesReviews.TABLE_NAME, null, contentValues);
                if (review_id > 0){
                    retUri= ContentUris.withAppendedId(FavoriteMoviesContract.FavoriteMoviesReviews.CONTENT_URI, review_id);
                } else{
                    throw new SQLException("no insert "+ uri);
                }
                break;

            default:
                Toast.makeText(getContext(), "Unsupported Operation", Toast.LENGTH_SHORT).show();
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return retUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        final SQLiteDatabase db= moviesDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int moviesDeleted=0;

        switch (match){
            case FAVORITE_MOVIES_WITH_ID:
                String movId= uri.getLastPathSegment();
                moviesDeleted= db.delete(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
                        FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID + "=?",
                        new String[] {movId});
                break;
        }

        if (moviesDeleted != 0) {
            // notify
            getContext().getContentResolver().notifyChange(uri, null);
            Toast.makeText(getContext(), "something gone", Toast.LENGTH_SHORT).show();
        }

        return moviesDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}

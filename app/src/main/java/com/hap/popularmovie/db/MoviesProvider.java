package com.hap.popularmovie.db;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class MoviesProvider extends ContentProvider {
    private static final int CODE_ALL_MOVIES = 1111;
    private static final int CODE_SINGLE_MOVIE = 2222;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDatabaseHelper movieDatabaseHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.PATH_MOVIES, CODE_ALL_MOVIES);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/#", CODE_SINGLE_MOVIE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        movieDatabaseHelper = new MovieDatabaseHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = movieDatabaseHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case CODE_ALL_MOVIES:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (final ContentValues value : values) {
                        long _id = db.insert(MovieContract.MoviesEntity.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0 && getContext() != null) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not yet implemented, check bulkInsert method");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int numRowsDeleted;

        if (null == selection) selection = "1";
        switch (sUriMatcher.match(uri)) {
            case CODE_ALL_MOVIES:
                numRowsDeleted = movieDatabaseHelper.getWritableDatabase().delete(
                        MovieContract.MoviesEntity.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_SINGLE_MOVIE:
                final String normalizedUtcDateString = uri.getLastPathSegment();
                final String[] selectionArguments = new String[]{normalizedUtcDateString};
                numRowsDeleted = movieDatabaseHelper.getWritableDatabase().delete(
                        MovieContract.MoviesEntity.TABLE_NAME,
                        MovieContract.MoviesEntity._ID + " = ? ",
                        selectionArguments);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_ALL_MOVIES:
                cursor = movieDatabaseHelper.getReadableDatabase().query(
                        MovieContract.MoviesEntity.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_SINGLE_MOVIE:
                final String normalizedUtcDateString = uri.getLastPathSegment();
                final String[] selectionArguments = new String[]{normalizedUtcDateString};
                cursor = movieDatabaseHelper.getReadableDatabase().query(
                        MovieContract.MoviesEntity.TABLE_NAME,
                        projection,
                        MovieContract.MoviesEntity._ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        movieDatabaseHelper.close();
        super.shutdown();
    }
}
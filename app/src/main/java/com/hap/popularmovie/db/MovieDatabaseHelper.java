package com.hap.popularmovie.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by luis on 11/18/17.
 */

public class MovieDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE =

                "CREATE TABLE " + MovieContract.MoviesEntity.TABLE_NAME + " (" +
                        MovieContract.MoviesEntity._ID + " INTEGER PRIMARY KEY, " +

                        MovieContract.MoviesEntity.COLUMN_TITLE + " TEXT NOT NULL, " +
                        MovieContract.MoviesEntity.COLUMN_ORIGINAL_TITLE + " TEXT NULL," +
                        MovieContract.MoviesEntity.COLUMN_ORIGINAL_LANGUAGE + " TEXT NULL, " +

                        MovieContract.MoviesEntity.COLUMN_THUMBNAIL + " TEXT NOT NULL, " +

                        MovieContract.MoviesEntity.COLUMN_SYNOPSIS + " TEXT NULL, " +
                        MovieContract.MoviesEntity.COLUMN_GENRES + " TEXT NULL, " +

                        MovieContract.MoviesEntity.COLUMN_RELEASE_DATE + " TEXT NULL, " +
                        MovieContract.MoviesEntity.COLUMN_DURATION + " INTEGER NULL, " +

                        MovieContract.MoviesEntity.COLUMN_VOTE_AVERAGE + " REAL NULL, " +
                        MovieContract.MoviesEntity.COLUMN_IS_FAVORITE + " INTEGER NOT NULL, " +

                        " UNIQUE (" + MovieContract.MoviesEntity._ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MoviesEntity.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

package com.hap.popularmovie.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by luis on 11/18/17.
 */

public class MovieContract {
    static final String CONTENT_AUTHORITY = "com.hap.popularmovie";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_MOVIES = "movies";

    public static final class MoviesEntity implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        static final String TABLE_NAME = "movies";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_GENRES = "genres";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_IS_FAVORITE = "is_favorite";

        public static Uri getContentUriByMovieId(final String movieId) {
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_MOVIES)
                    .appendPath(movieId)
                    .build();
        }
    }
}

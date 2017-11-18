package com.hap.popularmovie.util;

import com.hap.popularmovie.BuildConfig;

import java.util.Locale;

/**
 * Created by luis on 9/15/17.
 */

public class MovieSettings {
    public static final String API_KEY = BuildConfig.API_KEY;
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=%s";
    private static final String BASE_YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getBaseYoutubeUrl(final String videoKey) {
        return String.format(Locale.US, BASE_YOUTUBE_URL, videoKey);
    }

    public static String getBaseYoutubeThumbnailUrl(final String videoKey) {
        return String.format(Locale.US, BASE_YOUTUBE_THUMBNAIL_URL, videoKey);
    }
}

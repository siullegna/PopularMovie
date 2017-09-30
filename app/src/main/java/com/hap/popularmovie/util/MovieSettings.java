package com.hap.popularmovie.util;

import com.hap.popularmovie.BuildConfig;

/**
 * Created by luis on 9/15/17.
 */

public class MovieSettings {
    public static final String API_KEY = BuildConfig.API_KEY;
    private static final String BASE_URL= "http://api.themoviedb.org/3/movie/";

    public static String getBaseUrl() {
        return BASE_URL;
    }
}

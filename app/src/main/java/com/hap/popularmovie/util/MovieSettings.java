package com.hap.popularmovie.util;

/**
 * Created by luis on 9/15/17.
 */

public class MovieSettings {
    private static final String BASE_URL= "http://api.themoviedb.org/3/movie/";

    public static String getBaseUrl() {
        return BASE_URL;
    }
}

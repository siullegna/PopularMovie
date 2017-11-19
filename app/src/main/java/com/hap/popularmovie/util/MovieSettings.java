package com.hap.popularmovie.util;

import android.util.Log;

import com.hap.popularmovie.BuildConfig;
import com.hap.popularmovie.model.Genre;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by luis on 9/15/17.
 */

public class MovieSettings {
    private static final String TAG = MovieSettings.class.getName();
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

    /**
     * To be able to save the movie genres
     * we get this form:
     * <p>
     * id,movie;id,movie;id,movie...id,movie
     */
    public static String getMovieGenres(final ArrayList<Genre> movieGenres) {
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < movieGenres.size() - 1; i++) {
            sb.append(getMovieGenre(movieGenres.get(i)));
            sb.append(";");
        }

        sb.append(getMovieGenre(movieGenres.get(movieGenres.size() - 1)));

        return sb.toString();
    }

    private static String getMovieGenre(final Genre genre) {
        return String.format(Locale.US, "%s,%s", genre.getId(), genre.getName());
    }

    public static ArrayList<Genre> getMovieGenres(final String genres) {
        final ArrayList<Genre> movieGenres = new ArrayList<>();

        final String[] genreParts = genres.split(";");
        for (final String genrePart : genreParts) {
            final String[] genre = genrePart.split(",");
            try {
                final Genre newGenre = new Genre();
                newGenre.setId(Integer.parseInt(genre[0]));
                newGenre.setName(genre[1]);
                movieGenres.add(newGenre);
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return movieGenres;
    }
}

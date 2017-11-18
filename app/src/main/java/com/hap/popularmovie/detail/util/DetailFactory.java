package com.hap.popularmovie.detail.util;

import android.text.TextUtils;
import android.util.Log;

import com.hap.popularmovie.MovieApplication;
import com.hap.popularmovie.R;
import com.hap.popularmovie.model.Genre;
import com.hap.popularmovie.model.detail.InformationItem;
import com.hap.popularmovie.model.detail.SeparatorItem;
import com.hap.popularmovie.model.movie.MovieItem;

import java.util.ArrayList;

/**
 * Created by luis on 11/18/17.
 */

public class DetailFactory {
    private static final String TAG = DetailFactory.class.getName();

    public static ArrayList<Object> getInformationList(final MovieItem movieItem) {
        final ArrayList<Object> informationList = new ArrayList<>();
        final SeparatorItem separatorItem = new SeparatorItem();
        if (!TextUtils.isEmpty(movieItem.getOriginalTitle())) {
            final InformationItem originalTitle = new InformationItem(R.string.original_title_header, movieItem.getOriginalTitle());
            informationList.add(originalTitle);
        }

        if (!TextUtils.isEmpty(movieItem.getOriginalLanguage())) {
            final InformationItem originalLanguage = new InformationItem(R.string.original_language_header, movieItem.getOriginalLanguage());
            informationList.add(originalLanguage);
            informationList.add(separatorItem);
        }

        if (movieItem.getDuration() > 0) {
            final InformationItem runtime = new InformationItem(R.string.runtime_header, MovieApplication.getInstance().getString(R.string.runtime, movieItem.getDuration()));
            informationList.add(runtime);
            informationList.add(separatorItem);
        }

        if (!TextUtils.isEmpty(movieItem.getReleaseDate())) {
            final InformationItem releaseDate = new InformationItem(R.string.release_date_header, movieItem.getReleaseDate());
            informationList.add(releaseDate);
            if (movieItem.getDuration() > 0) {
                informationList.add(separatorItem);
            }
        }

        final InformationItem rating = new InformationItem(R.string.rating_header, MovieApplication.getInstance().getString(R.string.rating, String.valueOf(movieItem.getVoteAverage())));
        informationList.add(rating);

        try {
            final InformationItem genres = new InformationItem(R.string.genres_header, getGenres(movieItem.getGenres()));
            informationList.add(genres);
            informationList.add(separatorItem);
        } catch (NoGenreException e) {
            Log.i(TAG, e.getMessage());
        }

        final InformationItem synopsis = new InformationItem(R.string.synopsis_header, movieItem.getSynopsis());
        informationList.add(synopsis);

        return informationList;
    }

    private static String getGenres(final ArrayList<Genre> genres) throws NoGenreException {
        if (genres == null || genres.isEmpty()) {
            throw new NoGenreException("Cannot find any genre for the specific movie");
        }
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(genres.get(0).getName());
        if (genres.size() > 1) {
            for (int i = 1; i < genres.size(); i++) {
                stringBuilder.append(MovieApplication.getInstance().getString(R.string.genres_mask, genres.get(i).getName()));
            }
        }

        return stringBuilder.toString();
    }

    private static class NoGenreException extends Exception {
        NoGenreException(String message) {
            super(message);
        }
    }
}

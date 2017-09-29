package com.hap.popularmovie.detail.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.hap.popularmovie.R;
import com.hap.popularmovie.model.Genre;
import com.hap.popularmovie.model.MovieItem;

import java.util.ArrayList;

/**
 * Created by luis on 9/28/17.
 */

public class MovieDetailsView extends LinearLayout {
    private static final String TAG = MovieDetailsView.class.getName();
    private LinearLayout viewContainer;

    public MovieDetailsView(Context context) {
        this(context, null);
    }

    public MovieDetailsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovieDetailsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.movie_details_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        viewContainer = findViewById(R.id.view_container);
    }

    public void setupDetails(final MovieItem movieItem) {
        if (!TextUtils.isEmpty(movieItem.getOriginalTitle())) {
            final DetailView originalTitle = new DetailView(getContext());
            originalTitle.setupDetail(R.string.original_title_header, movieItem.getOriginalTitle(), false);
            viewContainer.addView(originalTitle);
        }

        if (!TextUtils.isEmpty(movieItem.getOriginalLanguage())) {
            final DetailView originalLanguage = new DetailView(getContext());
            originalLanguage.setupDetail(R.string.original_language_header, movieItem.getOriginalLanguage(), true);
            viewContainer.addView(originalLanguage);
        }

        if (movieItem.getDuration() > 0) {
            final DetailView runtime = new DetailView(getContext());
            runtime.setupDetail(R.string.runtime_header, getResources().getString(R.string.runtime, movieItem.getDuration()), true);
            viewContainer.addView(runtime);
        }

        if (!TextUtils.isEmpty(movieItem.getReleaseDate())) {
            final DetailView releaseDate = new DetailView(getContext());
            releaseDate.setupDetail(R.string.release_date_header, movieItem.getReleaseDate(), movieItem.getDuration() > 0);
            viewContainer.addView(releaseDate);
        }

        final DetailView rating = new DetailView(getContext());
        rating.setupDetail(R.string.rating_header, getResources().getString(R.string.rating, String.valueOf(movieItem.getVoteAverage())), false);
        viewContainer.addView(rating);

        try {
            final DetailView genres = new DetailView(getContext());
            genres.setupDetail(R.string.genres_header, getGenres(movieItem.getGenres()), true);
            viewContainer.addView(genres);
        } catch (NoGenreException e) {
            Log.i(MovieDetailsView.TAG, e.getMessage());
        }

        final DetailView synopsis = new DetailView(getContext());
        synopsis.setupDetail(R.string.synopsis_header, movieItem.getSynopsis(), false);
        viewContainer.addView(synopsis);
    }

    private String getGenres(final ArrayList<Genre> genres) throws NoGenreException {
        if (genres == null || genres.isEmpty()) {
            throw new NoGenreException("Cannot find any genre for the specific movie");
        }
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(genres.get(0).getName());
        if (genres.size() > 1) {
            for (int i = 1; i < genres.size(); i++) {
                stringBuilder.append(getResources().getString(R.string.genres_mask, genres.get(i).getName()));
            }
        }

        return stringBuilder.toString();
    }

    private class NoGenreException extends Exception {
        public NoGenreException(String message) {
            super(message);
        }
    }
}

package com.hap.popularmovie.network;

import android.content.Context;

import com.hap.popularmovie.R;
import com.hap.popularmovie.dagger.scope.ApplicationScope;
import com.hap.popularmovie.model.MovieItem;
import com.hap.popularmovie.model.MovieResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luis on 9/16/17.
 */
@ApplicationScope
public class MovieRestService {
    private final String apiKey;
    private final MovieRestApi movieRestApi;

    public MovieRestService(final Context context, final MovieRestApi movieRestApi) {
        this.apiKey = context.getString(R.string.api_key);
        this.movieRestApi = movieRestApi;
    }

    public Observable<MovieResponse> getMovies(final SortType sortType) {
        final Observable<MovieResponse> movieObservable = this.movieRestApi.getMovies(sortType.type, apiKey);
        return movieObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MovieItem> getMovieById(final String movieId) {
        final Observable<MovieItem> movieObservable = this.movieRestApi.getMovieById(movieId, apiKey);
        return movieObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public enum SortType {
        POPULAR("popular"),
        RATING("top_rated");

        private final String type;

        SortType(final String type) {
            this.type = type;
        }
    }
}

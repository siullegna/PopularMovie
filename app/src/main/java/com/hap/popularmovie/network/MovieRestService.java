package com.hap.popularmovie.network;

import com.hap.popularmovie.dagger.scope.ApplicationScope;
import com.hap.popularmovie.model.MovieItem;
import com.hap.popularmovie.model.MovieResponse;
import com.hap.popularmovie.util.MovieSettings;

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

    public MovieRestService(final MovieRestApi movieRestApi) {
        this.apiKey = MovieSettings.API_KEY;
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

package com.hap.popularmovie.network;

import com.hap.popularmovie.dagger.scope.ApplicationScope;
import com.hap.popularmovie.model.MovieItem;
import com.hap.popularmovie.model.MovieResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by luis on 9/15/17.
 */
@ApplicationScope
public interface MovieRestApi {
    @GET("https://api.themoviedb.org/3/movie/{sortType}/")
    Observable<MovieResponse> getMovies(@Path("sortType") final String sortType, @Query("api_key") final String apiKey);

    @GET("https://api.themoviedb.org/3/movie/{movieId}")
    Observable<MovieItem> getMovieById(@Path("movieId") final String movieId, @Query("api_key") final String apiKey);
}

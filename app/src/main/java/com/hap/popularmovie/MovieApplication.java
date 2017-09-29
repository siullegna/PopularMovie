package com.hap.popularmovie;

import android.app.Application;

import com.hap.popularmovie.dagger.component.DaggerMovieAppComponent;
import com.hap.popularmovie.dagger.component.MovieAppComponent;
import com.hap.popularmovie.dagger.module.NetworkModule;

/**
 * Created by luis on 9/21/17.
 */

public class MovieApplication extends Application {
    private static MovieApplication INSTANCE;
    private MovieAppComponent movieAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        MovieApplication.INSTANCE = this;

        movieAppComponent = DaggerMovieAppComponent.builder()
                .networkModule(new NetworkModule())
                .build();
    }

    public MovieAppComponent getMovieAppComponent() {
        return movieAppComponent;
    }

    public static MovieApplication getInstance() {
        return (MovieApplication) MovieApplication.INSTANCE.getApplicationContext();
    }
}

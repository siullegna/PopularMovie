package com.hap.popularmovie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import com.hap.popularmovie.network.MovieRestService;

import javax.inject.Inject;

/**
 * Created by luis on 11/18/17.
 */

public abstract class BaseMovieActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIES_KEY = "com.hap.popularmovie.EXTRA_MOVIES_KEY";
    public static final String EXTRA_SORT_TYPE_KEY = "com.hap.popularmovie.EXTRA_SORT_TYPE_KEY";
    public static final String EXTRA_MOVIE_ITEM = "com.hap.popularmovie.EXTRA_MOVIE_ITEM";
    public static final String EXTRA_IS_FAVORITE = "com.hap.popularmovie.EXTRA_IS_FAVORITE";
    public static final String EXTRA_TRAILERS = "com.hap.popularmovie.EXTRA_TRAILERS";
    public static final String EXTRA_REVIEW_RESPONSE = "com.hap.popularmovie.EXTRA_REVIEW_RESPONSE";

    protected Toolbar toolbar;
    protected ProgressBar loader;
    protected RecyclerView rvList;
    @Inject
    protected MovieRestService movieRestService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MovieApplication.getInstance().getMovieAppComponent().inject(this);
    }

    protected abstract void showLoader();

    protected abstract void hideLoader();
}

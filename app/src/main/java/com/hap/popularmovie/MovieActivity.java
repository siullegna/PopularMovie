package com.hap.popularmovie;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hap.popularmovie.model.MovieItem;
import com.hap.popularmovie.model.MovieResponse;
import com.hap.popularmovie.movie.adapter.MovieAdapter;
import com.hap.popularmovie.network.MovieRestService;
import com.hap.popularmovie.util.ImageSettings;
import com.hap.popularmovie.widget.EmptyScreenView;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MovieActivity extends AppCompatActivity {
    private static final String EXTRA_MOVIES_KEY = "com.hap.popularmovie.EXTRA_MOVIES_KEY";
    private static final String EXTRA_SORT_TYPE_KEY = "com.hap.popularmovie.EXTRA_SORT_TYPE_KEY";
    private MovieRestService.SortType currentSortType = null;
    private ArrayList<MovieItem> movies;
    private MovieAdapter movieAdapter;
    private ProgressBar loader;
    private SwipeRefreshLayout refresh;
    private RecyclerView rvMovies;
    private EmptyScreenView emptyScreenView;
    @Inject
    MovieRestService movieRestService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        MovieApplication.getInstance().getMovieAppComponent().inject(this);

        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList(MovieActivity.EXTRA_MOVIES_KEY);
            currentSortType = MovieRestService.SortType.valueOf(savedInstanceState.getString(MovieActivity.EXTRA_SORT_TYPE_KEY));
        }

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loader = findViewById(R.id.loader);
        refresh = findViewById(R.id.refresh);
        rvMovies = findViewById(R.id.rv_movies);
        emptyScreenView = findViewById(R.id.empty_screen_view);

        rvMovies.setHasFixedSize(true);
        final int cols = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                ? 2
                : 3;
        final GridLayoutManager layoutManager = new GridLayoutManager(this, cols, LinearLayoutManager.VERTICAL, false);
        rvMovies.setLayoutManager(layoutManager);

        final int itemSize = ImageSettings.getItemSize(this, cols);
        movieAdapter = new MovieAdapter(itemSize);
        rvMovies.setAdapter(movieAdapter);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMovies(currentSortType);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentSortType == null) {
            emptyScreenView.setupEmptyScreen(EmptyScreenView.ScreenType.GONE);
            currentSortType = MovieRestService.SortType.POPULAR;
            loadMovies(currentSortType);
        } else {
            movieAdapter.addAll(movies);
            hideLoader();
            if (movieAdapter.getItemCount() > 0) {
                emptyScreenView.setupEmptyScreen(EmptyScreenView.ScreenType.GONE);
                rvMovies.setVisibility(View.VISIBLE);
            } else {
                emptyScreenView.setupEmptyScreen(EmptyScreenView.ScreenType.EMPTY_MOVIES);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int actionId = item.getItemId();

        switch (actionId) {
            case R.id.action_sort_popular:
                if (currentSortType == MovieRestService.SortType.POPULAR && movieAdapter.getItemCount() > 0) {
                    return false;
                }
                rvMovies.setVisibility(View.GONE);
                showLoader();
                movies.clear();
                movieAdapter.clear();
                currentSortType = MovieRestService.SortType.POPULAR;
                loadMovies(currentSortType);
                return true;
            case R.id.action_sort_rating:
                if (currentSortType == MovieRestService.SortType.RATING && movieAdapter.getItemCount() > 0) {
                    return false;
                }
                rvMovies.setVisibility(View.GONE);
                showLoader();
                movies.clear();
                movieAdapter.clear();
                currentSortType = MovieRestService.SortType.RATING;
                loadMovies(currentSortType);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MovieActivity.EXTRA_MOVIES_KEY, movies);
        outState.putString(MovieActivity.EXTRA_SORT_TYPE_KEY, currentSortType.name());
        super.onSaveInstanceState(outState);
    }

    private void showLoader() {
        if (loader == null || emptyScreenView == null) {
            return;
        }
        loader.setVisibility(View.VISIBLE);
        emptyScreenView.setupEmptyScreen(EmptyScreenView.ScreenType.GONE);
    }

    private void hideLoader() {
        if (loader == null || refresh == null) {
            return;
        }
        loader.setVisibility(View.GONE);
        refresh.setRefreshing(false);
    }

    private void loadMovies(final MovieRestService.SortType sortType) {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            switch (sortType) {
                case POPULAR:
                    actionBar.setTitle(R.string.action_sort_popular);
                    break;
                case RATING:
                    actionBar.setTitle(R.string.action_sort_rating);
                    break;
            }
        }

        movieRestService.getMovies(sortType)
                .subscribe(new Consumer<MovieResponse>() {
                    @Override
                    public void accept(@NonNull MovieResponse movieResponse) throws Exception {
                        if (movieAdapter == null || rvMovies == null || emptyScreenView == null) {
                            return;
                        }

                        movies = movieResponse.getMovies();
                        movieAdapter.addAll(movieResponse.getMovies());
                        hideLoader();
                        if (movieAdapter.getItemCount() > 0) {
                            emptyScreenView.setupEmptyScreen(EmptyScreenView.ScreenType.GONE);
                            rvMovies.setVisibility(View.VISIBLE);
                        } else {
                            emptyScreenView.setupEmptyScreen(EmptyScreenView.ScreenType.EMPTY_MOVIES);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        hideLoader();
                        if (emptyScreenView != null && movieAdapter != null) {
                            if (movieAdapter.getItemCount() > 0) {
                                Toast.makeText(MovieApplication.getInstance(), MovieApplication.getInstance().getString(R.string.error_cannot_load_movies), Toast.LENGTH_LONG).show();
                            } else {
                                emptyScreenView.setupEmptyScreen(EmptyScreenView.ScreenType.ERROR_NO_MOVIES);
                            }
                        } else {
                            Toast.makeText(MovieApplication.getInstance(), MovieApplication.getInstance().getString(R.string.error_cannot_load_movies), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
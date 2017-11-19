package com.hap.popularmovie;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hap.popularmovie.db.MovieContract;
import com.hap.popularmovie.model.Genre;
import com.hap.popularmovie.model.movie.MovieItem;
import com.hap.popularmovie.model.movie.MovieResponse;
import com.hap.popularmovie.movie.adapter.MovieAdapter;
import com.hap.popularmovie.movie.holder.MovieItemHolder;
import com.hap.popularmovie.network.MovieRestService;
import com.hap.popularmovie.util.ImageSettings;
import com.hap.popularmovie.util.MovieSettings;
import com.hap.popularmovie.widget.EmptyScreenView;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MovieActivity extends BaseMovieActivity implements MovieItemHolder.OnViewClickListener {
    private MovieRestService.SortType currentSortType = null;
    private ArrayList<MovieItem> movies;
    private MovieAdapter movieAdapter;
    private SwipeRefreshLayout refresh;
    private EmptyScreenView emptyScreenView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList(BaseMovieActivity.EXTRA_MOVIES_KEY);
            currentSortType = MovieRestService.SortType.valueOf(savedInstanceState.getString(BaseMovieActivity.EXTRA_SORT_TYPE_KEY));
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loader = findViewById(R.id.loader);
        refresh = findViewById(R.id.refresh);
        rvList = findViewById(R.id.rv_movies);
        emptyScreenView = findViewById(R.id.empty_screen_view);

        rvList.setHasFixedSize(true);
        final int cols = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                ? 2
                : 3;
        final GridLayoutManager layoutManager = new GridLayoutManager(this, cols, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);

        final int itemSize = ImageSettings.getItemSize(this, cols);
        movieAdapter = new MovieAdapter(itemSize, this);
        rvList.setAdapter(movieAdapter);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (currentSortType == MovieRestService.SortType.FAVORITE) {
                    loadFavorites();
                } else {
                    loadMovies(currentSortType);
                }
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
        } else if (currentSortType == MovieRestService.SortType.FAVORITE) {
            loadFavorites();
        } else {
            if (movieAdapter.isEmpty()) {
                movieAdapter.addAll(movies);
            }
            hideLoader();
            if (movieAdapter.getItemCount() > 0) {
                emptyScreenView.setupEmptyScreen(EmptyScreenView.ScreenType.GONE);
                rvList.setVisibility(View.VISIBLE);
            } else {
                emptyScreenView.setupEmptyScreen(EmptyScreenView.ScreenType.EMPTY_MOVIES);
            }
        }
        setupToolbar(currentSortType);
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
                rvList.setVisibility(View.GONE);
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
                rvList.setVisibility(View.GONE);
                showLoader();
                movies.clear();
                movieAdapter.clear();
                currentSortType = MovieRestService.SortType.RATING;
                loadMovies(currentSortType);
                return true;
            case R.id.action_show_favorites:
                if (currentSortType == MovieRestService.SortType.FAVORITE && movieAdapter.getItemCount() > 0) {
                    return false;
                }
                rvList.setVisibility(View.GONE);
                showLoader();
                currentSortType = MovieRestService.SortType.FAVORITE;
                loadFavorites();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BaseMovieActivity.EXTRA_MOVIES_KEY, movies);
        outState.putString(BaseMovieActivity.EXTRA_SORT_TYPE_KEY, currentSortType.name());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(MovieItem movieItem) {
        final Intent detailsIntent = new Intent(this, MovieDetailActivity.class);
        final Bundle args = new Bundle();
        args.putParcelable(MovieDetailActivity.EXTRA_MOVIE_ITEM, movieItem);
        detailsIntent.putExtras(args);
        startActivity(detailsIntent);
    }

    @Override
    protected void showLoader() {
        if (loader == null || emptyScreenView == null) {
            return;
        }
        loader.setVisibility(View.VISIBLE);
        emptyScreenView.setupEmptyScreen(EmptyScreenView.ScreenType.GONE);
    }

    @Override
    protected void hideLoader() {
        if (loader == null || refresh == null) {
            return;
        }
        loader.setVisibility(View.GONE);
        refresh.setRefreshing(false);
    }

    private void loadMovies(final MovieRestService.SortType sortType) {
        setupToolbar(sortType);

        movieRestService.getMovies(sortType)
                .subscribe(new Consumer<MovieResponse>() {
                    @Override
                    public void accept(@NonNull MovieResponse movieResponse) throws Exception {
                        if (movieAdapter == null || rvList == null || emptyScreenView == null) {
                            return;
                        }

                        movies = movieResponse.getMovies();
                        movieAdapter.addAll(movieResponse.getMovies(), true);
                        hideLoader();
                        if (movieAdapter.getItemCount() > 0) {
                            emptyScreenView.setupEmptyScreen(EmptyScreenView.ScreenType.GONE);
                            rvList.setVisibility(View.VISIBLE);
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

    private void setupToolbar(final MovieRestService.SortType sortType) {
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
    }

    private void loadFavorites() {
        final Cursor cursor = getContentResolver().query(MovieContract.MoviesEntity.CONTENT_URI,
                null,
                null,
                null,
                null);
        try {
            movies.clear();
            if (cursor == null || cursor.getCount() == 0) {
                rvList.setVisibility(View.GONE);
                emptyScreenView.setupEmptyScreen(EmptyScreenView.ScreenType.NO_FAVORITES);
                hideLoader();
                return;
            }

            final int idColumn = cursor.getColumnIndex(MovieContract.MoviesEntity._ID);
            final int titleColumn = cursor.getColumnIndex(MovieContract.MoviesEntity.COLUMN_TITLE);
            final int originalTitleColumn = cursor.getColumnIndex(MovieContract.MoviesEntity.COLUMN_ORIGINAL_TITLE);
            final int originalLanguageColumn = cursor.getColumnIndex(MovieContract.MoviesEntity.COLUMN_ORIGINAL_LANGUAGE);
            final int thumbnailColumn = cursor.getColumnIndex(MovieContract.MoviesEntity.COLUMN_THUMBNAIL);
            final int synopsisColumn = cursor.getColumnIndex(MovieContract.MoviesEntity.COLUMN_SYNOPSIS);
            final int genresColumn = cursor.getColumnIndex(MovieContract.MoviesEntity.COLUMN_GENRES);
            final int releaseDateColumn = cursor.getColumnIndex(MovieContract.MoviesEntity.COLUMN_RELEASE_DATE);
            final int durationColumn = cursor.getColumnIndex(MovieContract.MoviesEntity.COLUMN_DURATION);
            final int voteAverageColumn = cursor.getColumnIndex(MovieContract.MoviesEntity.COLUMN_VOTE_AVERAGE);

            while (cursor.moveToNext()) {
                final MovieItem movieItem = new MovieItem();
                movieItem.setId(cursor.getInt(idColumn));
                movieItem.setTitle(cursor.getString(titleColumn));
                movieItem.setOriginalTitle(cursor.getString(originalTitleColumn));
                movieItem.setOriginalLanguage(cursor.getString(originalLanguageColumn));
                movieItem.setThumbnail(cursor.getString(thumbnailColumn));
                movieItem.setSynopsis(cursor.getString(synopsisColumn));
                final String genres = cursor.getString(genresColumn);
                final ArrayList<Genre> genreList = MovieSettings.getMovieGenres(genres);
                movieItem.setGenres(genreList);
                movieItem.setReleaseDate(cursor.getString(releaseDateColumn));
                movieItem.setDuration(cursor.getInt(durationColumn));
                movieItem.setVoteAverage(cursor.getFloat(voteAverageColumn));

                movies.add(movieItem);
            }

            movieAdapter.addAll(movies, true);
            rvList.setVisibility(View.VISIBLE);
            hideLoader();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
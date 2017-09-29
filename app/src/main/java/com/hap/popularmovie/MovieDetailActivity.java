package com.hap.popularmovie;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hap.popularmovie.detail.widget.MovieDetailsView;
import com.hap.popularmovie.model.MovieItem;
import com.hap.popularmovie.network.MovieRestService;
import com.hap.popularmovie.util.DeviceSettings;
import com.hap.popularmovie.util.ImageSettings;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE_ITEM = "com.hap.popularmovie.EXTRA_MOVIE_ITEM";
    public static final String EXTRA_IS_FAVORITE = "com.hap.popularmovie.EXTRA_IS_FAVORITE";

    private boolean isFavorite;
    @Inject
    MovieRestService movieRestService;
    private ProgressBar loader;
    private MovieDetailsView movieDetailsView;
    private MovieItem movieItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        MovieApplication.getInstance().getMovieAppComponent().inject(this);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        final CardView cardView = findViewById(R.id.card_view);
        final int minimumHeight = (int) (DeviceSettings.getDeviceHeight(this) - (DeviceSettings.getDeviceHeight(this) * 0.27));
        cardView.setMinimumHeight(minimumHeight);

        loader = findViewById(R.id.loader);
        movieDetailsView = findViewById(R.id.movie_details_view);
        final FloatingActionButton fabFavorite = findViewById(R.id.fab_favorite);
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabFavorite.setSelected(!fabFavorite.isSelected());
                isFavorite = fabFavorite.isSelected();
            }
        });

        showLoader();
        if (savedInstanceState == null) {
            movieItem = getIntent().getParcelableExtra(MovieDetailActivity.EXTRA_MOVIE_ITEM);
            loadMovies(String.valueOf(movieItem.getId()));
        } else {
            isFavorite = savedInstanceState.getBoolean(MovieDetailActivity.EXTRA_IS_FAVORITE);
            fabFavorite.setSelected(isFavorite);
            movieItem = savedInstanceState.getParcelable(MovieDetailActivity.EXTRA_MOVIE_ITEM);
            movieDetailsView.setupDetails(movieItem);
            movieDetailsView.setVisibility(View.VISIBLE);
            hideLoader();
        }
        toolbar.setTitle(movieItem.getTitle());
        final ImageView ivMovieThumbnail = findViewById(R.id.iv_movie_thumbnail);
        Picasso.with(this)
                .load(ImageSettings.getBasePhotoUrl(ImageSettings.SIZE_W780) + movieItem.getThumbnail())
                .error(ContextCompat.getDrawable(this, R.mipmap.ic_launcher_round))
                .into(ivMovieThumbnail);

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MovieDetailActivity.EXTRA_MOVIE_ITEM, movieItem);
        outState.putBoolean(MovieDetailActivity.EXTRA_IS_FAVORITE, isFavorite);
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int actionId = item.getItemId();

        switch (actionId) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showLoader() {
        if (loader == null) {
            return;
        }
        loader.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        if (loader == null) {
            return;
        }
        loader.setVisibility(View.GONE);
    }

    private void loadMovies(final String movieId) {
        movieRestService.getMovieById(movieId)
                .subscribe(new Consumer<MovieItem>() {
                    @Override
                    public void accept(@NonNull MovieItem movieItem) throws Exception {
                        if (movieDetailsView == null) {
                            return;
                        }
                        MovieDetailActivity.this.movieItem = movieItem;
                        movieDetailsView.setupDetails(movieItem);
                        movieDetailsView.setVisibility(View.VISIBLE);
                        hideLoader();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (movieDetailsView == null) {
                            return;
                        }
                        movieDetailsView.setupDetails(movieItem);
                        movieDetailsView.setVisibility(View.VISIBLE);
                        hideLoader();
                        Toast.makeText(MovieApplication.getInstance(), MovieApplication.getInstance().getString(R.string.error_cannot_load_details), Toast.LENGTH_LONG).show();
                    }
                });
    }
}

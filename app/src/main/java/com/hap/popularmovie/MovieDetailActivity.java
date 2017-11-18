package com.hap.popularmovie;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hap.popularmovie.detail.adapter.MovieDetailAdapter;
import com.hap.popularmovie.detail.util.DetailFactory;
import com.hap.popularmovie.model.movie.MovieItem;
import com.hap.popularmovie.model.trailer.TrailerItem;
import com.hap.popularmovie.model.trailer.TrailerResponse;
import com.hap.popularmovie.network.MovieRestService;
import com.hap.popularmovie.util.ImageSettings;
import com.hap.popularmovie.util.MovieSettings;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailAdapter.OnTrailerClickListener {
    private static final String TAG = MovieDetailActivity.class.getName();
    public static final String EXTRA_MOVIE_ITEM = "com.hap.popularmovie.EXTRA_MOVIE_ITEM";
    public static final String EXTRA_IS_FAVORITE = "com.hap.popularmovie.EXTRA_IS_FAVORITE";
    public static final String EXTRA_TRAILERS = "com.hap.popularmovie.EXTRA_TRAILERS";

    private boolean isFavorite;
    @Inject
    MovieRestService movieRestService;
    private ProgressBar loader;
    private CardView cardView;
    private RecyclerView movieDetailsList;
    private MovieDetailAdapter movieDetailAdapter;
    private MovieItem movieItem;
    private ArrayList<TrailerItem> trailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        MovieApplication.getInstance().getMovieAppComponent().inject(this);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        loader = findViewById(R.id.loader);
        cardView = findViewById(R.id.card_view);
        movieDetailsList = findViewById(R.id.movie_details_list);
        final FloatingActionButton fabFavorite = findViewById(R.id.fab_favorite);
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabFavorite.setSelected(!fabFavorite.isSelected());
                isFavorite = fabFavorite.isSelected();
            }
        });
        movieDetailAdapter = new MovieDetailAdapter(this);
        setupList();
        showLoader();
        if (savedInstanceState == null) {
            movieItem = getIntent().getParcelableExtra(MovieDetailActivity.EXTRA_MOVIE_ITEM);
            loadMovies(String.valueOf(movieItem.getId()));
            loadTrailers(String.valueOf(movieItem.getId()));
        } else {
            isFavorite = savedInstanceState.getBoolean(MovieDetailActivity.EXTRA_IS_FAVORITE);
            fabFavorite.setSelected(isFavorite);
            movieItem = savedInstanceState.getParcelable(MovieDetailActivity.EXTRA_MOVIE_ITEM);
            final ArrayList<Object> movieDetails = DetailFactory.getInformationList(movieItem);
            movieDetailAdapter.addDetailsAll(movieDetails);
            cardView.setVisibility(View.VISIBLE);
            trailers = savedInstanceState.getParcelableArrayList(MovieDetailActivity.EXTRA_TRAILERS);
            if (trailers == null) {
                loadTrailers(String.valueOf(movieItem.getId()));
            } else {
                movieDetailAdapter.addTrailers(getString(R.string.trailers), trailers);
            }
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
        outState.putParcelableArrayList(MovieDetailActivity.EXTRA_TRAILERS, trailers);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(TrailerItem trailerItem) {
        final Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MovieSettings.getBaseYoutubeUrl(trailerItem.getKey())));
        final Intent chooser = Intent.createChooser(youtubeIntent , getString(R.string.open_with));
        final PackageManager packageManager = getPackageManager();
        if (chooser.resolveActivity(packageManager) != null) {
            startActivity(chooser);
        }
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

    private void setupList() {
        movieDetailsList.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        movieDetailsList.setLayoutManager(layoutManager);
        movieDetailsList.setAdapter(movieDetailAdapter);
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
                        if (cardView == null) {
                            return;
                        }
                        MovieDetailActivity.this.movieItem = movieItem;
                        final ArrayList<Object> movieDetails = DetailFactory.getInformationList(movieItem);
                        movieDetailAdapter.addDetailsAll(movieDetails);
                        cardView.setVisibility(View.VISIBLE);
                        hideLoader();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (cardView == null) {
                            return;
                        }
                        final ArrayList<Object> movieDetails = DetailFactory.getInformationList(movieItem);
                        movieDetailAdapter.addDetailsAll(movieDetails);
                        cardView.setVisibility(View.VISIBLE);
                        hideLoader();
                        Toast.makeText(MovieApplication.getInstance(), MovieApplication.getInstance().getString(R.string.error_cannot_load_details), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loadTrailers(final String movieId) {
        movieRestService.getMovieTrailerById(movieId)
                .subscribe(new Consumer<TrailerResponse>() {
                    @Override
                    public void accept(@NonNull TrailerResponse trailerResponse) throws Exception {
                        if (cardView == null) {
                            return;
                        }
                        MovieDetailActivity.this.trailers = trailerResponse.getTrailers();
                        movieDetailAdapter.addTrailers(getString(R.string.trailers), trailerResponse.getTrailers());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (cardView == null) {
                            return;
                        }
                        Log.e(TAG, throwable.getMessage());
                        Toast.makeText(MovieApplication.getInstance(), MovieApplication.getInstance().getString(R.string.trailer_loading_error), Toast.LENGTH_LONG).show();
                    }
                });
    }
}

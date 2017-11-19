package com.hap.popularmovie;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hap.popularmovie.db.MovieContract;
import com.hap.popularmovie.detail.adapter.MovieDetailAdapter;
import com.hap.popularmovie.detail.util.DetailFactory;
import com.hap.popularmovie.model.movie.MovieItem;
import com.hap.popularmovie.model.trailer.TrailerItem;
import com.hap.popularmovie.model.trailer.TrailerResponse;
import com.hap.popularmovie.util.ImageSettings;
import com.hap.popularmovie.util.MovieSettings;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MovieDetailActivity extends BaseMovieActivity implements MovieDetailAdapter.OnTrailerClickListener {
    private static final String TAG = MovieDetailActivity.class.getName();

    private boolean isFavorite;
    private CardView cardView;
    private FloatingActionButton fabFavorite;
    private MovieDetailAdapter movieDetailAdapter;
    private MovieItem movieItem;
    private ArrayList<TrailerItem> trailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        toolbar = findViewById(R.id.toolbar);
        loader = findViewById(R.id.loader);
        cardView = findViewById(R.id.card_view);
        rvList = findViewById(R.id.movie_details_list);
        fabFavorite = findViewById(R.id.fab_favorite);
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabFavorite.setSelected(!fabFavorite.isSelected());
                isFavorite = fabFavorite.isSelected();
                updateFavoriteState();
            }
        });
        movieDetailAdapter = new MovieDetailAdapter(this);
        rvList.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(movieDetailAdapter);
        showLoader();
        if (savedInstanceState == null) {
            movieItem = getIntent().getParcelableExtra(BaseMovieActivity.EXTRA_MOVIE_ITEM);
            loadMovieDetails(String.valueOf(movieItem.getId()));
            loadTrailers(String.valueOf(movieItem.getId()));
        } else {
            isFavorite = savedInstanceState.getBoolean(BaseMovieActivity.EXTRA_IS_FAVORITE);
            fabFavorite.setSelected(isFavorite);
            movieItem = savedInstanceState.getParcelable(BaseMovieActivity.EXTRA_MOVIE_ITEM);
            final ArrayList<Object> movieDetails = DetailFactory.getInformationList(movieItem);
            movieDetailAdapter.addDetailsAll(movieDetails);
            cardView.setVisibility(View.VISIBLE);
            trailers = savedInstanceState.getParcelableArrayList(BaseMovieActivity.EXTRA_TRAILERS);
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
        outState.putParcelable(BaseMovieActivity.EXTRA_MOVIE_ITEM, movieItem);
        outState.putBoolean(BaseMovieActivity.EXTRA_IS_FAVORITE, isFavorite);
        outState.putParcelableArrayList(BaseMovieActivity.EXTRA_TRAILERS, trailers);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(TrailerItem trailerItem) {
        final Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MovieSettings.getBaseYoutubeUrl(trailerItem.getKey())));
        final Intent chooser = Intent.createChooser(youtubeIntent, getString(R.string.open_with));
        final PackageManager packageManager = getPackageManager();
        if (chooser.resolveActivity(packageManager) != null) {
            startActivity(chooser);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int actionId = item.getItemId();

        switch (actionId) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_reviews:
                final Intent reviewIntent = new Intent(this, MovieReviewActivity.class);
                final Bundle args = new Bundle();
                args.putParcelable(BaseMovieActivity.EXTRA_MOVIE_ITEM, movieItem);
                reviewIntent.putExtras(args);
                startActivity(reviewIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void showLoader() {
        if (loader == null) {
            return;
        }
        loader.setVisibility(View.VISIBLE);
    }

    @Override
    protected void hideLoader() {
        if (loader == null) {
            return;
        }
        loader.setVisibility(View.GONE);
    }

    private void updateFavoriteState() {
        if (isFavorite) {
            final ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MoviesEntity._ID, movieItem.getId());
            contentValues.put(MovieContract.MoviesEntity.COLUMN_TITLE, movieItem.getTitle());
            contentValues.put(MovieContract.MoviesEntity.COLUMN_ORIGINAL_TITLE, movieItem.getOriginalTitle());
            contentValues.put(MovieContract.MoviesEntity.COLUMN_ORIGINAL_LANGUAGE, movieItem.getOriginalLanguage());
            contentValues.put(MovieContract.MoviesEntity.COLUMN_THUMBNAIL, movieItem.getThumbnail());
            contentValues.put(MovieContract.MoviesEntity.COLUMN_SYNOPSIS, movieItem.getSynopsis());
            contentValues.put(MovieContract.MoviesEntity.COLUMN_GENRES, MovieSettings.getMovieGenres(movieItem.getGenres()));
            contentValues.put(MovieContract.MoviesEntity.COLUMN_RELEASE_DATE, movieItem.getReleaseDate());
            contentValues.put(MovieContract.MoviesEntity.COLUMN_DURATION, movieItem.getDuration());
            contentValues.put(MovieContract.MoviesEntity.COLUMN_VOTE_AVERAGE, movieItem.getVoteAverage());
            contentValues.put(MovieContract.MoviesEntity.COLUMN_IS_FAVORITE, isFavorite ? 1 : 0);

            final ContentValues[] contentValuesList = new ContentValues[1];
            contentValuesList[0] = contentValues;

            getContentResolver().bulkInsert(MovieContract.MoviesEntity.CONTENT_URI, contentValuesList);
        } else {
            getContentResolver().delete(MovieContract.MoviesEntity.getContentUriByMovieId(String.valueOf(movieItem.getId())),
                    null,
                    null);
        }
    }

    private void loadMovieDetails(final String movieId) {
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
                        loadFavorite();
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
                        loadFavorite();
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

    private void loadFavorite() {
        final Cursor cursor = getContentResolver().query(MovieContract.MoviesEntity.getContentUriByMovieId(String.valueOf(movieItem.getId())),
                null,
                null,
                null,
                null);

        try {
            if (cursor == null || cursor.getCount() == 0) {
                return;
            }

            final int favoriteColumn = cursor.getColumnIndex(MovieContract.MoviesEntity.COLUMN_IS_FAVORITE);

            cursor.moveToNext();
            isFavorite = cursor.getInt(favoriteColumn) == 1;
            fabFavorite.setSelected(isFavorite);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}

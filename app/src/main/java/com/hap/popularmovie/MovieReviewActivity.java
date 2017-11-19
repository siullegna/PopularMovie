package com.hap.popularmovie;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.hap.popularmovie.model.movie.MovieItem;
import com.hap.popularmovie.model.review.ReviewResponse;
import com.hap.popularmovie.review.adapter.ReviewAdapter;
import com.hap.popularmovie.util.ImageSettings;
import com.squareup.picasso.Picasso;

import io.reactivex.functions.Consumer;

public class MovieReviewActivity extends BaseMovieActivity {
    private MovieItem movieItem;
    private ReviewAdapter reviewAdapter;
    private ReviewResponse reviewResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_review);

        toolbar = findViewById(R.id.toolbar);
        loader = findViewById(R.id.loader);
        rvList = findViewById(R.id.movie_reviews);

        reviewAdapter = new ReviewAdapter();
        rvList.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(reviewAdapter);

        showLoader();
        if (savedInstanceState == null) {
            movieItem = getIntent().getParcelableExtra(BaseMovieActivity.EXTRA_MOVIE_ITEM);
            loadReviews(String.valueOf(movieItem.getId()));
        } else {
            movieItem = savedInstanceState.getParcelable(BaseMovieActivity.EXTRA_MOVIE_ITEM);
            reviewResponse = savedInstanceState.getParcelable(BaseMovieActivity.EXTRA_REVIEW_RESPONSE);
            if (reviewResponse == null) {
                loadReviews(String.valueOf(movieItem.getId()));
            } else {
                reviewAdapter.addAll(reviewResponse.getReviews());
                hideLoader();
            }
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
        outState.putParcelable(BaseMovieActivity.EXTRA_REVIEW_RESPONSE, reviewResponse);
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

    private void loadReviews(final String movieId) {
        movieRestService.getMovieReviewById(movieId)
                .subscribe(new Consumer<ReviewResponse>() {
                    @Override
                    public void accept(ReviewResponse reviewResponse) throws Exception {
                        if (reviewAdapter == null) {
                            return;
                        }

                        reviewAdapter.addAll(reviewResponse.getReviews());
                        hideLoader();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (reviewAdapter == null) {
                            return;
                        }
                        hideLoader();
                    }
                });
    }
}

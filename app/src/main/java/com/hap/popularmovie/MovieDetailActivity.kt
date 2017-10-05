package com.hap.popularmovie

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.hap.popularmovie.detail.widget.MovieDetailsView
import com.hap.popularmovie.extension.makePretty
import com.hap.popularmovie.model.MovieItem
import com.hap.popularmovie.network.MovieRestService
import com.hap.popularmovie.util.DeviceSettings
import com.hap.popularmovie.util.ImageSettings
import com.squareup.picasso.Picasso
import io.reactivex.functions.Consumer
import javax.inject.Inject

/**
 * Created by luis on 10/4/17.
 */
class MovieDetailActivity : AppCompatActivity() {
    companion object {
        // TODO 05 - Immutability
        const val EXTRA_MOVIE_ITEM = "com.hap.popularmovie.EXTRA_MOVIE_ITEM"
        const val EXTRA_IS_FAVORITE = "com.hap.popularmovie.EXTRA_IS_FAVORITE"
    }

    private var isFavorite: Boolean = false
    @Inject
    lateinit var movieRestService: MovieRestService
    private var loader: ProgressBar? = null
    // TODO 01 - Null safety
    private var movieDetailsView: MovieDetailsView? = null
    private var movieItem: MovieItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        MovieApplication.getInstance().movieAppComponent.inject(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val cardView = findViewById<CardView>(R.id.card_view)
        val minimumHeight = (DeviceSettings.getDeviceHeight(this) - DeviceSettings.getDeviceHeight(this) * 0.27).toInt()
        cardView.minimumHeight = minimumHeight

        loader = findViewById(R.id.loader)
        movieDetailsView = findViewById(R.id.movie_details_view)
        val fabFavorite = findViewById<FloatingActionButton>(R.id.fab_favorite)
        fabFavorite.setOnClickListener {
            fabFavorite.isSelected = !fabFavorite.isSelected
            isFavorite = fabFavorite.isSelected
        }

        showLoader()
        if (savedInstanceState == null) {
            movieItem = intent.getParcelableExtra(MovieDetailActivity.EXTRA_MOVIE_ITEM)
            loadMovies(movieItem!!.id.toString())
        } else {
            isFavorite = savedInstanceState.getBoolean(MovieDetailActivity.EXTRA_IS_FAVORITE)
            fabFavorite.isSelected = isFavorite
            movieItem = savedInstanceState.getParcelable(MovieDetailActivity.EXTRA_MOVIE_ITEM)
            movieDetailsView!!.setupDetails(movieItem)
            movieDetailsView!!.visibility = View.VISIBLE
            hideLoader()
        }

        toolbar.title = movieItem!!.title.makePretty()
        val ivMovieThumbnail = findViewById<ImageView>(R.id.iv_movie_thumbnail)
        Picasso.with(this)
                .load(ImageSettings.getBasePhotoUrl(ImageSettings.SIZE_W780) + movieItem!!.thumbnail)
                .error(ContextCompat.getDrawable(this, R.mipmap.ic_launcher_round))
                .into(ivMovieThumbnail)

        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(MovieDetailActivity.EXTRA_MOVIE_ITEM, movieItem)
        outState.putBoolean(MovieDetailActivity.EXTRA_IS_FAVORITE, isFavorite)
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val actionId = item.itemId

        when (actionId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoader() {
        if (loader == null) {
            return
        }
        loader!!.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        if (loader == null) {
            return
        }
        loader!!.visibility = View.GONE
    }

    private fun loadMovies(movieId: String) {
        // TODO 03 - Higher-order functions and lambdas
        movieRestService.getMovieById(movieId)
                .subscribe(Consumer { movieItem ->
                    if (movieDetailsView == null) {
                        return@Consumer
                    }
                    this@MovieDetailActivity.movieItem = movieItem
                    movieDetailsView!!.setupDetails(movieItem)
                    movieDetailsView!!.visibility = View.VISIBLE
                    hideLoader()
                }, Consumer {
                    if (movieDetailsView == null) {
                        return@Consumer
                    }
                    movieDetailsView!!.setupDetails(movieItem)
                    movieDetailsView!!.visibility = View.VISIBLE
                    hideLoader()
                    Toast.makeText(MovieApplication.getInstance(), MovieApplication.getInstance().getString(R.string.error_cannot_load_details), Toast.LENGTH_LONG).show()
                })
    }
}
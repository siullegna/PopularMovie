package com.hap.popularmovie.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by luis on 10/4/17.
 */
// TODO 04 - Data classes
class MovieResponse(private val page: Int = 0,
                    @SerializedName("total_results")
                    private val totalResults: Int = 0,
                    @SerializedName("total_pages")
                    private val totalPages: Int = 0,
                    @SerializedName("results")
                    val movies: ArrayList<MovieItem>? = null)
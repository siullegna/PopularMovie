package com.hap.popularmovie.model.movie;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by luis on 9/16/17.
 */

public class MovieResponse {
    private int page;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("results")
    private ArrayList<MovieItem> movies;

    public int getPage() {
        return page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public ArrayList<MovieItem> getMovies() {
        return movies;
    }
}

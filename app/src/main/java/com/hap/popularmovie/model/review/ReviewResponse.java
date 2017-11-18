package com.hap.popularmovie.model.review;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by luis on 11/16/17.
 */

public class ReviewResponse {
    private long id;
    private int page;
    @SerializedName("results")
    private ArrayList<ReviewItem> reviews;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("total_results")
    private int totalResults;

    public long getId() {
        return id;
    }

    public int getPage() {
        return page;
    }

    public ArrayList<ReviewItem> getReviews() {
        return reviews;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }
}

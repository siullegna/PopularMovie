package com.hap.popularmovie.model.trailer;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by luis on 11/16/17.
 */

public class TrailerResponse {
    private long id;
    @SerializedName("results")
    private ArrayList<TrailerItem> trailers;

    public long getId() {
        return id;
    }

    public ArrayList<TrailerItem> getTrailers() {
        return trailers;
    }
}

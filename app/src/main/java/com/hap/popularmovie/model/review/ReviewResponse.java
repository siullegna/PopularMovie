package com.hap.popularmovie.model.review;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by luis on 11/16/17.
 */

public class ReviewResponse implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeInt(this.page);
        dest.writeList(this.reviews);
        dest.writeInt(this.totalPages);
        dest.writeInt(this.totalResults);
    }

    public ReviewResponse() {
    }

    protected ReviewResponse(Parcel in) {
        this.id = in.readLong();
        this.page = in.readInt();
        this.reviews = new ArrayList<ReviewItem>();
        in.readList(this.reviews, ReviewItem.class.getClassLoader());
        this.totalPages = in.readInt();
        this.totalResults = in.readInt();
    }

    public static final Parcelable.Creator<ReviewResponse> CREATOR = new Parcelable.Creator<ReviewResponse>() {
        @Override
        public ReviewResponse createFromParcel(Parcel source) {
            return new ReviewResponse(source);
        }

        @Override
        public ReviewResponse[] newArray(int size) {
            return new ReviewResponse[size];
        }
    };
}

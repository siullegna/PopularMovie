package com.hap.popularmovie.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by luis on 9/16/17.
 */

public class MovieItem implements Parcelable {
    private ArrayList<Genre> genres;
    @SerializedName("homepage")
    private String homepageUrl;
    private int id;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("overview")
    private String synopsis;
    private double popularity;
    @SerializedName("poster_path")
    private String thumbnail;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("runtime")
    private int duration;
    private String status;
    private String title;
    @SerializedName("video")
    private boolean isVideo;
    @SerializedName("vote_average")
    private float voteAverage;
    @SerializedName("vote_count")
    private int voteCount;

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public String getHomepageUrl() {
        return homepageUrl;
    }

    public int getId() {
        return id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getDuration() {
        return duration;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.genres);
        dest.writeString(this.homepageUrl);
        dest.writeInt(this.id);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.originalTitle);
        dest.writeString(this.synopsis);
        dest.writeDouble(this.popularity);
        dest.writeString(this.thumbnail);
        dest.writeString(this.releaseDate);
        dest.writeInt(this.duration);
        dest.writeString(this.status);
        dest.writeString(this.title);
        dest.writeByte(this.isVideo ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.voteAverage);
        dest.writeInt(this.voteCount);
    }

    public MovieItem() {
    }

    protected MovieItem(Parcel in) {
        this.genres = in.createTypedArrayList(Genre.CREATOR);
        this.homepageUrl = in.readString();
        this.id = in.readInt();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();
        this.synopsis = in.readString();
        this.popularity = in.readDouble();
        this.thumbnail = in.readString();
        this.releaseDate = in.readString();
        this.duration = in.readInt();
        this.status = in.readString();
        this.title = in.readString();
        this.isVideo = in.readByte() != 0;
        this.voteAverage = in.readFloat();
        this.voteCount = in.readInt();
    }

    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel source) {
            return new MovieItem(source);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };
}

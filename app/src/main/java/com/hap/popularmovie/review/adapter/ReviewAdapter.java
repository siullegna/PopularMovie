package com.hap.popularmovie.review.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hap.popularmovie.R;
import com.hap.popularmovie.model.review.ReviewItem;
import com.hap.popularmovie.review.holder.ReviewHolder;

import java.util.ArrayList;

/**
 * Created by luis on 11/18/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder> {
    private final ArrayList<ReviewItem> reviews = new ArrayList<>();

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.review_holder, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        holder.setupReview(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void addAll(ArrayList<ReviewItem> reviews) {
        this.reviews.addAll(reviews);
        notifyDataSetChanged();
    }
}

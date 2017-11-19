package com.hap.popularmovie.review.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hap.popularmovie.R;
import com.hap.popularmovie.model.review.ReviewItem;

/**
 * Created by luis on 11/18/17.
 */

public class ReviewHolder extends RecyclerView.ViewHolder {
    private final TextView reviewer;
    private final TextView review;

    public ReviewHolder(View itemView) {
        super(itemView);

        reviewer = itemView.findViewById(R.id.reviewer);
        review = itemView.findViewById(R.id.review);
    }

    public void setupReview(final ReviewItem reviewItem) {
        reviewer.setText(reviewItem.getAuthor());
        review.setText(reviewItem.getContent());
    }
}

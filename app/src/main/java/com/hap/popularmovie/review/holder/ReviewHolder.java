package com.hap.popularmovie.review.holder;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
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

    public void setupReview() {
        reviewer.setText(R.string.action_reviews);
        review.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            reviewer.setTextAppearance(R.style.ReviewItemStyle);
        } else {
            reviewer.setTextAppearance(itemView.getContext(), R.style.ReviewItemStyle);
        }
        final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) reviewer.getLayoutParams();
        lp.setMargins(itemView.getResources().getDimensionPixelSize(R.dimen.review_title_margin_horizontal), itemView.getResources().getDimensionPixelSize(R.dimen.review_title_margin_vertical), 0, itemView.getResources().getDimensionPixelSize(R.dimen.review_title_margin_vertical));
        reviewer.requestLayout();
    }
}

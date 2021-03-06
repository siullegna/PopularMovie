package com.hap.popularmovie.movie.holder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.hap.popularmovie.R;
import com.hap.popularmovie.model.movie.MovieItem;
import com.hap.popularmovie.util.ImageSettings;
import com.squareup.picasso.Picasso;

/**
 * Created by luis on 9/16/17.
 */

public class MovieItemHolder extends RecyclerView.ViewHolder {
    private final Context context;
    private final ImageView ivMovieThumbnail;

    public MovieItemHolder(View itemView) {
        super(itemView);

        this.context = this.itemView.getContext();
        this.ivMovieThumbnail = itemView.findViewById(R.id.iv_movie_thumbnail);
    }

    public void bindView(final int itemSize, final MovieItem movieItem, final OnViewClickListener onViewClickListener) {
        final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) this.itemView.getLayoutParams();
        layoutParams.height = itemSize;
        layoutParams.width = itemSize;
        this.itemView.requestLayout();

        Picasso.with(this.itemView.getContext())
                .load(ImageSettings.getBasePhotoUrl(ImageSettings.SIZE_W185) + movieItem.getThumbnail())
                .error(ContextCompat.getDrawable(this.context, R.mipmap.ic_launcher_round))
                .into(this.ivMovieThumbnail);

        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onViewClickListener != null) {
                    onViewClickListener.onClick(movieItem);
                }
            }
        });
    }

    public interface OnViewClickListener {
        void onClick(final MovieItem movieItem);
    }
}

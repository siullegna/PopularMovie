package com.hap.popularmovie.detail.holder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hap.popularmovie.R;
import com.hap.popularmovie.model.trailer.TrailerItem;
import com.hap.popularmovie.util.MovieSettings;
import com.squareup.picasso.Picasso;

/**
 * Created by luis on 11/18/17.
 */

public class TrailerViewHolder extends RecyclerView.ViewHolder {
    private final Context context;
    private final TextView trailerName;
    private final ImageView trailerPreview;

    public TrailerViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();

        trailerName = itemView.findViewById(R.id.trailer_name);
        trailerPreview = itemView.findViewById(R.id.trailer_preview);
    }

    public void setupTrailer(final TrailerItem trailer) {
        trailerName.setText(trailer.getName());

        Picasso.with(context)
                .load(MovieSettings.getBaseYoutubeThumbnailUrl(trailer.getKey()))
                .error(ContextCompat.getDrawable(context, R.mipmap.ic_launcher_round))
                .into(trailerPreview);
    }
}

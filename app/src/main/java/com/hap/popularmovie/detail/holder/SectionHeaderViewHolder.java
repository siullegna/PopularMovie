package com.hap.popularmovie.detail.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hap.popularmovie.R;

/**
 * Created by luis on 11/18/17.
 */

public class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
    private final TextView header;

    public SectionHeaderViewHolder(View itemView) {
        super(itemView);

        header = itemView.findViewById(R.id.header);
    }

    public void setupHeader(final String header) {
        this.header.setText(header);
    }
}

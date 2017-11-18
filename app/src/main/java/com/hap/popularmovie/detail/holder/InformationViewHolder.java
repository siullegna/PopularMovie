package com.hap.popularmovie.detail.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hap.popularmovie.R;
import com.hap.popularmovie.model.detail.InformationItem;

/**
 * Created by luis on 11/18/17.
 */

public class InformationViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvHeader;
    private final TextView tvDetail;

    public InformationViewHolder(View itemView) {
        super(itemView);

        tvHeader = itemView.findViewById(R.id.tv_header);
        tvDetail = itemView.findViewById(R.id.tv_detail);
    }

    public void setupInformation(final InformationItem informationItem) {
        tvHeader.setText(informationItem.getHeader());
        tvDetail.setText(informationItem.getDetail());
    }
}

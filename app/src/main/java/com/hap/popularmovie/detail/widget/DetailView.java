package com.hap.popularmovie.detail.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hap.popularmovie.R;

/**
 * Created by luis on 9/28/17.
 */

public class DetailView extends LinearLayout {
    private final TextView tvHeader;
    private final TextView tvDetail;
    private final View separator;

    public DetailView(Context context) {
        this(context, null);
    }

    public DetailView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.detail_view, this);

        tvHeader = findViewById(R.id.tv_header);
        tvDetail = findViewById(R.id.tv_detail);
        separator = findViewById(R.id.separator);
    }

    public void setupDetail(final int headerId, final String detail, final boolean isSeparator) {
        setupDetail(getResources().getString(headerId), detail, isSeparator);
    }

    public void setupDetail(final String header, final String detail, final boolean isSeparator) {
        tvHeader.setText(header);
        tvDetail.setText(detail);
        separator.setVisibility(isSeparator
                ? VISIBLE
                : GONE);
    }
}

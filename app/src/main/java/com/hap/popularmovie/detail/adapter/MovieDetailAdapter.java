package com.hap.popularmovie.detail.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hap.popularmovie.R;
import com.hap.popularmovie.detail.DetailType;
import com.hap.popularmovie.detail.holder.EmptyViewHolder;
import com.hap.popularmovie.detail.holder.InformationViewHolder;
import com.hap.popularmovie.detail.holder.SectionHeaderViewHolder;
import com.hap.popularmovie.detail.holder.SeparatorViewHolder;
import com.hap.popularmovie.detail.holder.TrailerViewHolder;
import com.hap.popularmovie.model.detail.InformationItem;
import com.hap.popularmovie.model.detail.SeparatorItem;
import com.hap.popularmovie.model.trailer.TrailerItem;

import java.util.ArrayList;

/**
 * Created by luis on 11/18/17.
 */

public class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<Object> items = new ArrayList<>();
    private final OnTrailerClickListener onTrailerClickListener;

    public MovieDetailAdapter(OnTrailerClickListener onTrailerClickListener) {
        this.onTrailerClickListener = onTrailerClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final DetailType detailType = DetailType.fromOrdinal(viewType);
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final RecyclerView.ViewHolder holder;
        final View view;

        switch (detailType) {
            case INFORMATION:
                view = inflater.inflate(R.layout.information_view_holder, parent, false);
                holder = new InformationViewHolder(view);
                break;
            case SEPARATOR:
                view = inflater.inflate(R.layout.separator_view_holder, parent, false);
                holder = new SeparatorViewHolder(view);
                break;
            case TRAILER:
                view = inflater.inflate(R.layout.trailer_view_holder, parent, false);
                holder = new TrailerViewHolder(view);
                break;
            case HEADER:
                view = inflater.inflate(R.layout.section_header_view_holder, parent, false);
                holder = new SectionHeaderViewHolder(view);
                break;
            default:
                view = inflater.inflate(R.layout.empty_view_holder, parent, false);
                holder = new EmptyViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DetailType detailType = DetailType.fromOrdinal(getItemViewType(position));
        final Object object = items.get(position);

        switch (detailType) {
            case INFORMATION:
                ((InformationViewHolder) holder).setupInformation((InformationItem) object);
                break;
            case SEPARATOR:

                break;
            case TRAILER:
                final TrailerViewHolder trailerViewHolder = ((TrailerViewHolder) holder);
                final TrailerItem trailerItem = (TrailerItem) object;
                trailerViewHolder.setupTrailer(trailerItem);
                trailerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onTrailerClickListener != null) {
                            onTrailerClickListener.onClick(trailerItem);
                        }
                    }
                });
                break;
            case HEADER:
                ((SectionHeaderViewHolder) holder).setupHeader((String) object);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        final Object object = items.get(position);
        if (object instanceof InformationItem) {
            return DetailType.INFORMATION.ordinal();
        } else if (object instanceof SeparatorItem) {
            return DetailType.SEPARATOR.ordinal();
        } else if (object instanceof TrailerItem) {
            return DetailType.TRAILER.ordinal();
        } else if (object instanceof String) {
            return DetailType.HEADER.ordinal();
        }
        return super.getItemViewType(position);
    }

    public void addDetailsAll(final ArrayList<Object> movieDetails) {
        this.items.addAll(0, movieDetails);
        notifyDataSetChanged();
    }

    public void addTrailers(final String header, final ArrayList<TrailerItem> trailers) {
        this.items.add(header);
        this.items.addAll(trailers);
        notifyDataSetChanged();
    }

    public interface OnTrailerClickListener {
        void onClick(TrailerItem trailerItem);
    }
}

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
import com.hap.popularmovie.model.review.ReviewItem;
import com.hap.popularmovie.model.trailer.TrailerItem;
import com.hap.popularmovie.review.holder.ReviewHolder;

import java.util.ArrayList;

/**
 * Created by luis on 11/18/17.
 */

public class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<Object> items = new ArrayList<>();
    private final OnItemClickListener onItemClickListener;

    public MovieDetailAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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
            case REVIEW:
                view = inflater.inflate(R.layout.review_holder, parent, false);
                holder = new ReviewHolder(view);
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
                        if (onItemClickListener != null) {
                            onItemClickListener.onClickTrailer(trailerItem);
                        }
                    }
                });
                break;
            case HEADER:
                ((SectionHeaderViewHolder) holder).setupHeader((String) object);
                break;
            case REVIEW:
                final ReviewHolder reviewHolder = ((ReviewHolder) holder);
                reviewHolder.setupReview();
                reviewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onClickReview();
                        }
                    }
                });
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
        } else if (object instanceof ReviewItem) {
            return DetailType.REVIEW.ordinal();
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
        this.items.add(new ReviewItem());
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onClickTrailer(TrailerItem trailerItem);
        void onClickReview();
    }
}

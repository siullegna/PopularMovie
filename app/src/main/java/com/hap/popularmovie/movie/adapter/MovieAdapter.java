package com.hap.popularmovie.movie.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hap.popularmovie.R;
import com.hap.popularmovie.model.MovieItem;
import com.hap.popularmovie.movie.holder.MovieItemHolder;

import java.util.ArrayList;

/**
 * Created by luis on 9/16/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieItemHolder> {
    private final ArrayList<MovieItem> movies = new ArrayList<>();
    private final int itemSize;
    private final MovieItemHolder.OnViewClickListener onViewClickListener;

    public MovieAdapter(final int itemSize, final MovieItemHolder.OnViewClickListener onViewClickListener) {
        this.itemSize = itemSize;
        this.onViewClickListener = onViewClickListener;
    }

    @Override
    public MovieItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_holder, parent, false);
        return new MovieItemHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(MovieItemHolder holder, int position) {
        holder.bindView(itemSize, movies.get(position), onViewClickListener);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void addAll(final ArrayList<MovieItem> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void clear() {
        this.movies.clear();
    }
}

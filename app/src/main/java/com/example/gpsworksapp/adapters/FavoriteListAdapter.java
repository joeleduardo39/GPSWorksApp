package com.example.gpsworksapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gpsworksapp.R;
import com.example.gpsworksapp.databinding.ItemContainerTvShowBinding;
import com.example.gpsworksapp.listeners.FavoriteListListener;
import com.example.gpsworksapp.models.Movies;

import java.util.List;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.MovieViewHolder> {

    private final List<Movies> movies;
    private LayoutInflater layoutInflater;
    private FavoriteListListener favoriteListListener;


    public FavoriteListAdapter(List<Movies> movies, FavoriteListListener favoriteListListener) {
        this.movies = movies;
        this.favoriteListListener = favoriteListListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerTvShowBinding tvShowBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_tv_show, parent, false
        );
        return new MovieViewHolder(tvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bindTvShow(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {


        private ItemContainerTvShowBinding itemContainerTvShowBinding;

        public MovieViewHolder(ItemContainerTvShowBinding itemContainerTvShowBinding) {
            super(itemContainerTvShowBinding.getRoot());
            this.itemContainerTvShowBinding = itemContainerTvShowBinding;
        }

        public void bindTvShow(Movies movies) {
            itemContainerTvShowBinding.setMovies(movies);
            itemContainerTvShowBinding.executePendingBindings();
            itemContainerTvShowBinding.getRoot().setOnClickListener(view -> favoriteListListener.onTVShowClicked(movies));
            itemContainerTvShowBinding.imageDelete.setOnClickListener(view -> favoriteListListener.removeMoviesFromFavoriteList(movies, getAdapterPosition()));
            itemContainerTvShowBinding.imageDelete.setVisibility(View.VISIBLE);
        }

    }

}

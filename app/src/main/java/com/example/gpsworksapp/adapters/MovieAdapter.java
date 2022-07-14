package com.example.gpsworksapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gpsworksapp.R;
import com.example.gpsworksapp.databinding.ItemContainerTvShowBinding;
import com.example.gpsworksapp.listeners.MoviesListener;
import com.example.gpsworksapp.models.Movies;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movies> movies;
    private LayoutInflater layoutInflater;
    private MoviesListener moviesListener;


    public MovieAdapter(List<Movies> moviesList, MoviesListener moviesListener) {
        this.movies = moviesList;
        this.moviesListener = moviesListener;
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
            itemContainerTvShowBinding.getRoot().setOnClickListener(view -> moviesListener.onTvShowClicked(movies));
        }

    }

}

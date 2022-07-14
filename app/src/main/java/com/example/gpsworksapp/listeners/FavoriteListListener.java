package com.example.gpsworksapp.listeners;

import com.example.gpsworksapp.models.Movies;

public interface FavoriteListListener {

    void onTVShowClicked (Movies movies);

    void removeMoviesFromFavoriteList(Movies movies, int position);



}

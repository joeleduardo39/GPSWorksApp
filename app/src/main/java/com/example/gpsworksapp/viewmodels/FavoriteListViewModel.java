package com.example.gpsworksapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.gpsworksapp.database.MoviesDatabase;
import com.example.gpsworksapp.models.Movies;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class FavoriteListViewModel extends AndroidViewModel {

    private MoviesDatabase moviesDatabase;

    public FavoriteListViewModel(@NonNull Application application) {
        super(application);
        moviesDatabase = MoviesDatabase.getMoviesDatabase(application);
    }

    public Flowable<List<Movies>> loadFavoriteList() {
        return moviesDatabase.moviesDao().getWatchList();
    }

    public Completable removeMoviesFromFavoritList(Movies movies) {
        return moviesDatabase.moviesDao().removeFromFavoriteList(movies);
    }
}

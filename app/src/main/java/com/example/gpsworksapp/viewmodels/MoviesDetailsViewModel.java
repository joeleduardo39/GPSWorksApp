package com.example.gpsworksapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gpsworksapp.database.MoviesDatabase;
import com.example.gpsworksapp.models.Movies;
import com.example.gpsworksapp.repositories.MoviesDetailsRepository;
import com.example.gpsworksapp.responses.MovieDetailsResponse;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class MoviesDetailsViewModel extends AndroidViewModel {

    private MoviesDetailsRepository moviesDetailsRepository;
    private MoviesDatabase moviesDatabase;


    public MoviesDetailsViewModel(@NonNull Application application) {
        super(application);
        moviesDetailsRepository = new MoviesDetailsRepository();
        moviesDatabase = MoviesDatabase.getMoviesDatabase(application);
    }


    public LiveData<MovieDetailsResponse> getMoviesDetails(String tvShowId) {
        return moviesDetailsRepository.getMovieDetails(tvShowId);
    }

    public Completable addToWatchList(Movies movies) {
        return moviesDatabase.moviesDao().addWatchList(movies);
    }

    public Flowable<Movies> getMoviesFromFavoriteList(String tvShowId) {
        return moviesDatabase.moviesDao().getMoviesFromFavoriteList(tvShowId);
    }

    public Completable removeMoviesFromFavoriteList(Movies movies) {
        return moviesDatabase.moviesDao().removeFromFavoriteList(movies);
    }

}

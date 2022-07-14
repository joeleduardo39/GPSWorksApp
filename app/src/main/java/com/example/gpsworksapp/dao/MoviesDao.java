package com.example.gpsworksapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.gpsworksapp.models.Movies;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM tvShow")
    Flowable<List<Movies>> getWatchList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addWatchList(Movies movies);

     @Delete
    Completable removeFromFavoriteList(Movies movies);

    @Query("SELECT * FROM tvShow WHERE id = :tvShowId")
    Flowable<Movies> getMoviesFromFavoriteList(String tvShowId);

}

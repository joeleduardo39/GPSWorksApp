package com.example.gpsworksapp.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gpsworksapp.dao.MoviesDao;
import com.example.gpsworksapp.models.Movies;

@Database(entities = Movies.class, version = 1, exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase {

    private static MoviesDatabase moviesDatabase;

    public static  synchronized MoviesDatabase getMoviesDatabase(Context context) {
        if (moviesDatabase == null) {
            moviesDatabase = Room.databaseBuilder(
                    context,
                    MoviesDatabase.class,
                    "tv_shows_db"
            ).build();
        }
        return moviesDatabase;
    }
    public abstract MoviesDao moviesDao();

}









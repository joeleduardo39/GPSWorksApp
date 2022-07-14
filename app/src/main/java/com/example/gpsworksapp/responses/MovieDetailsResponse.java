package com.example.gpsworksapp.responses;

import com.example.gpsworksapp.models.MoviesDetails;
import com.google.gson.annotations.SerializedName;

public class MovieDetailsResponse {

    @SerializedName("tvShow")
    private MoviesDetails moviesDetails;

public MoviesDetails getMovieDetails(){
    return moviesDetails;
}




}

package com.example.gpsworksapp.network;

import com.example.gpsworksapp.responses.GpsWorksAppResponse;
import com.example.gpsworksapp.responses.MovieDetailsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<GpsWorksAppResponse> getMostPopularTVShows(@Query("page")int page);


    @GET("show-details")
    Call<MovieDetailsResponse> getMovieDetails(@Query("q") String tvShowId);

    @GET("search")
    Call<GpsWorksAppResponse> searchMovies(@Query("q") String query, @Query("page") int page);

}

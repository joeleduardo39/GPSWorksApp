package com.example.gpsworksapp.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gpsworksapp.network.ApiClient;
import com.example.gpsworksapp.network.ApiService;
import com.example.gpsworksapp.responses.MovieDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesDetailsRepository {

    private ApiService apiService;

    public MoviesDetailsRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<MovieDetailsResponse> getMovieDetails(String tvShowId ) {
        MutableLiveData<MovieDetailsResponse> data = new MutableLiveData<>();
        apiService.getMovieDetails(tvShowId).enqueue(new Callback<MovieDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetailsResponse> call,@NonNull Response<MovieDetailsResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetailsResponse> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }


}

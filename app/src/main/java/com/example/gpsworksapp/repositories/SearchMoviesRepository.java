package com.example.gpsworksapp.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gpsworksapp.models.Movies;
import com.example.gpsworksapp.network.ApiClient;
import com.example.gpsworksapp.network.ApiService;
import com.example.gpsworksapp.responses.GpsWorksAppResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMoviesRepository {

    private ApiService apiService;

    public SearchMoviesRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<GpsWorksAppResponse> searchMovies(String query, int page) {
        MutableLiveData<GpsWorksAppResponse> data = new MutableLiveData<>();
        apiService.searchMovies(query, page).enqueue(new Callback<GpsWorksAppResponse>() {
            @Override
            public void onResponse(@NonNull Call<GpsWorksAppResponse> call, @NonNull Response<GpsWorksAppResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<GpsWorksAppResponse> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

}

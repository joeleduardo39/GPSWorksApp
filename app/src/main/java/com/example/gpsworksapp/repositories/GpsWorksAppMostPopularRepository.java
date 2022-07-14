package com.example.gpsworksapp.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gpsworksapp.network.ApiClient;
import com.example.gpsworksapp.network.ApiService;
import com.example.gpsworksapp.responses.GpsWorksAppResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GpsWorksAppMostPopularRepository {

    private ApiService apiService;

    public GpsWorksAppMostPopularRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<GpsWorksAppResponse> getMostPopularTVShows(int page) {
        MutableLiveData<GpsWorksAppResponse> data = new MutableLiveData<>();
        apiService.getMostPopularTVShows(page).enqueue(new Callback<GpsWorksAppResponse>() {
            @Override
            public void onResponse(@NonNull Call<GpsWorksAppResponse> call,@NonNull Response<GpsWorksAppResponse> response) {
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

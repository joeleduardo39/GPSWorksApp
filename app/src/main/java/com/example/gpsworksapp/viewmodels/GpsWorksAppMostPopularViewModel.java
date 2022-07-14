package com.example.gpsworksapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.gpsworksapp.repositories.GpsWorksAppMostPopularRepository;
import com.example.gpsworksapp.responses.GpsWorksAppResponse;

public class GpsWorksAppMostPopularViewModel extends ViewModel {

    private GpsWorksAppMostPopularRepository gpsWorksAppMostPopularRepository;

    public GpsWorksAppMostPopularViewModel() {
        gpsWorksAppMostPopularRepository = new GpsWorksAppMostPopularRepository();

    }

    public LiveData<GpsWorksAppResponse> getMostPopularTVShows(int page) {
        return gpsWorksAppMostPopularRepository.getMostPopularTVShows(page);
    }

}

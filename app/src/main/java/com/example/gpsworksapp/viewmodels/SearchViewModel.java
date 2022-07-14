package com.example.gpsworksapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.gpsworksapp.repositories.SearchMoviesRepository;
import com.example.gpsworksapp.responses.GpsWorksAppResponse;

public class SearchViewModel extends ViewModel {

    private SearchMoviesRepository searchMoviesRepository;

    public SearchViewModel() {
        searchMoviesRepository = new SearchMoviesRepository();
    }


    public LiveData<GpsWorksAppResponse> searchMovies(String query, int page) {
        return searchMoviesRepository.searchMovies(query, page);

    }


}

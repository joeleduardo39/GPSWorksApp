package com.example.gpsworksapp.responses;

import com.example.gpsworksapp.models.Movies;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GpsWorksAppResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int totalpages;

    @SerializedName("tv_shows")
    private List<Movies> movies;  //MODEL(Movies)


    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalpages;
    }

    public List<Movies> getMovies() {
        return movies;
    }
}

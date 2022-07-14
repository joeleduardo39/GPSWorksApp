package com.example.gpsworksapp.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gpsworksapp.R;
import com.example.gpsworksapp.adapters.MovieAdapter;
import com.example.gpsworksapp.databinding.ActivityMainBinding;
import com.example.gpsworksapp.listeners.MoviesListener;
import com.example.gpsworksapp.models.Movies;
import com.example.gpsworksapp.viewmodels.GpsWorksAppMostPopularViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesListener {

    private ActivityMainBinding activityMainBinding;
    private GpsWorksAppMostPopularViewModel viewModel;
    private List<Movies> movies = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        doInitialization();
    }

    private void doInitialization() {
        activityMainBinding.tvShowsRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(GpsWorksAppMostPopularViewModel.class);
        movieAdapter = new MovieAdapter(movies, this);
        activityMainBinding.tvShowsRecyclerView.setAdapter(movieAdapter);
        activityMainBinding.tvShowsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (activityMainBinding.tvShowsRecyclerView.canScrollVertically(1)) {
                    if (currentPage <= totalAvailablePages) {
                        currentPage += 1;
                        getMostPopularTVShows();
                    }
                }
            }
        });
        activityMainBinding.imageFavorite.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), FavoriteListAcitivity.class)));
        activityMainBinding.imageSearch.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), SearchActivity.class)));
        getMostPopularTVShows();


    }

    private void getMostPopularTVShows() {
        toggleLoading();
        viewModel.getMostPopularTVShows(currentPage).observe(this, gpsWorksAppResponse -> {
            toggleLoading();
            if (gpsWorksAppResponse != null) {
                totalAvailablePages = gpsWorksAppResponse.getTotalPages();
                if (gpsWorksAppResponse.getMovies() != null) {
                    int oldCount = movies.size();
                    movies.addAll(gpsWorksAppResponse.getMovies());
                    movieAdapter.notifyItemRangeInserted(oldCount, movies.size());
                }
            }
        });
    }

    private void toggleLoading() {
        if (currentPage == 1) {
            if (activityMainBinding.getIsLoading() != null && activityMainBinding.getIsLoading()) {
                activityMainBinding.setIsLoading(false);
            } else {
                activityMainBinding.setIsLoading(true);
            }
        } else {
            if (activityMainBinding.getIsLoadingMore() != null && activityMainBinding.getIsLoadingMore()) {
                activityMainBinding.setIsLoadingMore(false);
            } else {
                activityMainBinding.setIsLoadingMore(true);
            }
        }
    }

    @Override
    public void onTvShowClicked(Movies movies) {
        Intent intent = new Intent(getApplicationContext(), MoviesDetailsActivity.class);
        intent.putExtra("tvShow", movies);
        startActivity(intent);

    }
}

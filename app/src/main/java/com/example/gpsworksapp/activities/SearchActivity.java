package com.example.gpsworksapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gpsworksapp.R;
import com.example.gpsworksapp.adapters.MovieAdapter;
import com.example.gpsworksapp.databinding.ActivitySearchBinding;
import com.example.gpsworksapp.listeners.MoviesListener;
import com.example.gpsworksapp.models.Movies;
import com.example.gpsworksapp.viewmodels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements MoviesListener {

    private ActivitySearchBinding activitySearchBinding;
    private SearchViewModel viewModel;
    private List<Movies> movies = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        doInitialization();
    }

    private void doInitialization() {
        activitySearchBinding.imageBack.setOnClickListener(view -> onBackPressed());
        activitySearchBinding.MoviesRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        movieAdapter = new MovieAdapter(movies, this);
        activitySearchBinding.MoviesRecyclerView.setAdapter(movieAdapter);
        activitySearchBinding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().isEmpty()) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                currentPage = 1;
                                totalAvailablePages = 1;
                                searchMovies(editable.toString());
                            });
                        }
                    }, 800);
                } else {
                    movies.clear();
                    movieAdapter.notifyDataSetChanged();
                }
            }
        });
        activitySearchBinding.MoviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activitySearchBinding.MoviesRecyclerView.canScrollVertically(1)) {
                    if (!activitySearchBinding.inputSearch.getText().toString().isEmpty()) {
                        if (currentPage < totalAvailablePages) {
                            currentPage += 1;
                            searchMovies(activitySearchBinding.inputSearch.getText().toString());
                        }
                    }
                }
            }
        });
        activitySearchBinding.inputSearch.requestFocus();

    }

    private void searchMovies(String query) {
        toggleLoading();
        viewModel.searchMovies(query, currentPage).observe(this, gpsWorksAppResponse -> {
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
            if (activitySearchBinding.getIsLoading() != null && activitySearchBinding.getIsLoading()) {
                activitySearchBinding.setIsLoading(false);
            } else {
                activitySearchBinding.setIsLoading(true);
            }
        } else {
            if (activitySearchBinding.getIsLoadingMore() != null && activitySearchBinding.getIsLoadingMore()) {
                activitySearchBinding.setIsLoadingMore(false);
            } else {
                activitySearchBinding.setIsLoadingMore(true);
            }
        }
    }


    @Override
    public void onTvShowClicked(Movies movies) {
        Intent intent = new Intent(getApplicationContext(), MoviesDetailsActivity.class);
        intent.putExtra("movies", movies);
        startActivity(intent);

    }
}

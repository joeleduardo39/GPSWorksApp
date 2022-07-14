package com.example.gpsworksapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.gpsworksapp.R;
import com.example.gpsworksapp.adapters.FavoriteListAdapter;
import com.example.gpsworksapp.databinding.ActivityFavoriteListAcitivityBinding;
import com.example.gpsworksapp.listeners.FavoriteListListener;
import com.example.gpsworksapp.models.Movies;
import com.example.gpsworksapp.utils.TempDataHolder;
import com.example.gpsworksapp.viewmodels.FavoriteListViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FavoriteListAcitivity extends AppCompatActivity implements FavoriteListListener {

    private ActivityFavoriteListAcitivityBinding activityFavoriteListAcitivityBinding;
    private FavoriteListViewModel viewModel;
    private FavoriteListAdapter favoriteListAdapter;
    private List<Movies> favoriteList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       activityFavoriteListAcitivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_favorite_list_acitivity);
        doInitialization();
    }

    private void doInitialization() {
        viewModel = new ViewModelProvider(this).get(FavoriteListViewModel.class);
        activityFavoriteListAcitivityBinding.imageBack.setOnClickListener(view -> onBackPressed());
        favoriteList = new ArrayList<>();
        loadWatchList();
    }

    private void loadWatchList() {
        activityFavoriteListAcitivityBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                viewModel.loadFavoriteList().subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(tvShows -> {
                            activityFavoriteListAcitivityBinding.setIsLoading(false);
                            if (favoriteList.size() > 0) {
                                favoriteList.clear();
                            }
                            favoriteList.addAll(tvShows);
                            favoriteListAdapter = new FavoriteListAdapter(favoriteList, this);
                            activityFavoriteListAcitivityBinding.favoriteListRecyclerView.setAdapter(favoriteListAdapter);
                            activityFavoriteListAcitivityBinding.favoriteListRecyclerView.setVisibility(View.VISIBLE);
                            compositeDisposable.dispose();
                        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(TempDataHolder.IS_FAVORITELIST_UPDADE) {
            loadWatchList();
            TempDataHolder.IS_FAVORITELIST_UPDADE = false;
        }
    }

    @Override
    public void onTVShowClicked(Movies movies) {
        Intent intent = new Intent(getApplicationContext(), MoviesDetailsActivity.class);
        intent.putExtra("movies", movies);
                startActivity(intent);
    }

    @Override
    public void removeMoviesFromFavoriteList(Movies movies, int position) {
        CompositeDisposable compositeDisposableForDelete = new CompositeDisposable();
        compositeDisposableForDelete.add(viewModel.removeMoviesFromFavoritList(movies)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    favoriteList.remove(position);
                    favoriteListAdapter.notifyItemRemoved(position);
                    favoriteListAdapter.notifyItemRangeChanged(position, favoriteListAdapter.getItemCount());
                    compositeDisposableForDelete.dispose();
                        }));

    }
}

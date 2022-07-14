package com.example.gpsworksapp.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gpsworksapp.R;
import com.example.gpsworksapp.adapters.EpisodesAdapter;
import com.example.gpsworksapp.adapters.ImageSliderAdapter;
import com.example.gpsworksapp.databinding.ActivityMoviesDetailsBinding;
import com.example.gpsworksapp.databinding.LayoutEpisodesBottomSheetBinding;
import com.example.gpsworksapp.models.Movies;
import com.example.gpsworksapp.utils.TempDataHolder;
import com.example.gpsworksapp.viewmodels.MoviesDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MoviesDetailsActivity extends AppCompatActivity {

    private ActivityMoviesDetailsBinding activityMoviesDetailsBinding;
    private MoviesDetailsViewModel moviesDetailsViewModel;
    private BottomSheetDialog episodesBottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;
    private Movies movies;
    private Boolean isMoviesAvailableInFavoritList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMoviesDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_movies_details);
        doInitialization();
    }

    private void doInitialization() {
        moviesDetailsViewModel = new ViewModelProvider(this).get(MoviesDetailsViewModel.class);
        activityMoviesDetailsBinding.imageBack.setOnClickListener(view -> onBackPressed());
        movies = (Movies) getIntent().getSerializableExtra("tvShow");
        checkMoviesInFavoriteList();
        getMoviesDetails();
    }

    private void checkMoviesInFavoriteList() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(moviesDetailsViewModel.getMoviesFromFavoriteList(String.valueOf(movies.getId()))
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(movies -> {
            isMoviesAvailableInFavoritList = true;
            activityMoviesDetailsBinding.imageFavorite.setImageResource(R.drawable.ic_added);
            compositeDisposable.dispose();
        }));
    }

    private void getMoviesDetails() {
        activityMoviesDetailsBinding.setIsLoading(true);
        String tvShowId = String.valueOf(movies.getId());
        moviesDetailsViewModel.getMoviesDetails(tvShowId).observe(
                this, movieDetailsResponse -> {
                    activityMoviesDetailsBinding.setIsLoading(false);
                    if (movieDetailsResponse.getMovieDetails() != null) {
                        if (movieDetailsResponse.getMovieDetails().getPictures() != null) {
                            loadImageSliders(movieDetailsResponse.getMovieDetails().getPictures()); //metodo de slide de imagens dentro de Details
                        }
                        activityMoviesDetailsBinding.setTvShowImageURL(
                                movieDetailsResponse.getMovieDetails().getImagePath()
                        );
                        activityMoviesDetailsBinding.imageTVShow.setVisibility(View.VISIBLE);
                        activityMoviesDetailsBinding.setDescription(
                                String.valueOf(
                                        HtmlCompat.fromHtml(
                                                movieDetailsResponse.getMovieDetails().getDescription(),
                                                HtmlCompat.FROM_HTML_MODE_LEGACY
                                        )
                                )
                        );
                        activityMoviesDetailsBinding.textDescription.setVisibility(View.VISIBLE);
                        activityMoviesDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
                        activityMoviesDetailsBinding.textReadMore.setOnClickListener(view -> {
                            if (activityMoviesDetailsBinding.textReadMore.getText().toString().equals("Read More")) {
                                activityMoviesDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                activityMoviesDetailsBinding.textDescription.setEllipsize(null);
                                activityMoviesDetailsBinding.textReadMore.setText(getString(R.string.read_less)); //Leia menos
                            } else {
                                activityMoviesDetailsBinding.textDescription.setMaxLines(4);
                                activityMoviesDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                activityMoviesDetailsBinding.textReadMore.setText(R.string.read_more); //Sinopse Completa
                            }
                        });
                        activityMoviesDetailsBinding.setRating(
                                String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        Double.parseDouble((movieDetailsResponse.getMovieDetails().getRating()))
                                )
                        );
                        if (movieDetailsResponse.getMovieDetails().getGenres() != null) {
                            activityMoviesDetailsBinding.setGenre(movieDetailsResponse.getMovieDetails().getGenres()[0]);
                        } else {
                            activityMoviesDetailsBinding.setGenre("N/A");
                        }
                        activityMoviesDetailsBinding.setRuntime(movieDetailsResponse.getMovieDetails().getRunTime() + " Min"); //tempo de filme
                        activityMoviesDetailsBinding.viewDivider1.setVisibility(View.VISIBLE);
                        activityMoviesDetailsBinding.layoutMusic.setVisibility(View.VISIBLE);
                        activityMoviesDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);
                        activityMoviesDetailsBinding.buttonWebsite.setOnClickListener(view -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(movieDetailsResponse.getMovieDetails().getUrl()));
                            startActivity(intent);
                        });
                        activityMoviesDetailsBinding.buttonWebsite.setVisibility(View.VISIBLE);
                        activityMoviesDetailsBinding.buttonEpisodes.setVisibility(View.VISIBLE);
                        activityMoviesDetailsBinding.buttonEpisodes.setOnClickListener(view -> {
                            if (episodesBottomSheetDialog == null) {
                                episodesBottomSheetDialog = new BottomSheetDialog(MoviesDetailsActivity.this);
                                layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                                        LayoutInflater.from(MoviesDetailsActivity.this),
                                        R.layout.layout_episodes_bottom_sheet,
                                        findViewById(R.id.episodesContainer),
                                        false
                                );
                                episodesBottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());
                                layoutEpisodesBottomSheetBinding.episodesRecyclerView.setAdapter(
                                        new EpisodesAdapter(movieDetailsResponse.getMovieDetails().getEpisodes())
                                );
                                layoutEpisodesBottomSheetBinding.textTitle.setText(
                                        String.format("Episodes | %s", movies.getName())
                                );
                                layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener(view1 -> episodesBottomSheetDialog.dismiss());
                            }
                            FrameLayout frameLayout = episodesBottomSheetDialog.findViewById(
                                    com.google.android.material.R.id.design_bottom_sheet
                            );
                            if (frameLayout != null) {
                                BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                                bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }

                            episodesBottomSheetDialog.show();
                        });

                        activityMoviesDetailsBinding.imageFavorite.setOnClickListener(view -> {
                            CompositeDisposable compositeDisposable = new CompositeDisposable();
                            if (isMoviesAvailableInFavoritList) {
                                compositeDisposable.add(moviesDetailsViewModel.removeMoviesFromFavoriteList(movies)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    isMoviesAvailableInFavoritList = false;
                                    TempDataHolder.IS_FAVORITELIST_UPDADE = true;
                                    activityMoviesDetailsBinding.imageFavorite.setImageResource(R.drawable.ic_favorite);
                                    Toast.makeText(getApplicationContext(), "Removed from favorite List", Toast.LENGTH_SHORT).show();
                                    compositeDisposable.dispose();

                                }));

                            }else {
                                compositeDisposable.add(moviesDetailsViewModel.addToWatchList(movies)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            TempDataHolder.IS_FAVORITELIST_UPDADE = true;
                                            activityMoviesDetailsBinding.imageFavorite.setImageResource(R.drawable.ic_added);
                                            Toast.makeText(getApplicationContext(), "Added to favorite list", Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();

                                        })
                                );
                            }
                        });
                        activityMoviesDetailsBinding.imageFavorite.setVisibility(View.VISIBLE);

                        loadBasicTVShowDetails(); //Lista detalhes do titulo
                    }
                }
        );
    }

    private void loadImageSliders(String[] sliderImages) {
        activityMoviesDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityMoviesDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        activityMoviesDetailsBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityMoviesDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setUpSliderIndicators(sliderImages.length);
        activityMoviesDetailsBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setUpSliderIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.background_slider_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            activityMoviesDetailsBinding.layoutSliderIndicators.addView(indicators[i]);
        }
        activityMoviesDetailsBinding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position) {
        int childCount = activityMoviesDetailsBinding.layoutSliderIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) activityMoviesDetailsBinding.layoutSliderIndicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_inactive)
                );
            }

        }
    }

    private void loadBasicTVShowDetails() {
        activityMoviesDetailsBinding.setTvShowName(movies.getName());
        activityMoviesDetailsBinding.setNetworkCountry(
                movies.getNetwork() + " (" +
                        movies.getCountry() + ")"
        );
        activityMoviesDetailsBinding.setStatus(movies.getStatus());
        activityMoviesDetailsBinding.setStartedDate(movies.getStartDate());
        activityMoviesDetailsBinding.textName.setVisibility((View.VISIBLE));
        activityMoviesDetailsBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityMoviesDetailsBinding.textStatus.setVisibility(View.VISIBLE);
        activityMoviesDetailsBinding.textStarted.setVisibility(View.VISIBLE);
    }


}

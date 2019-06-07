package com.example.mymoviecatalogue.layout;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.adapter.FavoriteMovieAdapter;
import com.example.mymoviecatalogue.database.FavoriteEntry;
import com.example.mymoviecatalogue.model.Movie;
import com.example.mymoviecatalogue.model.MovieFavorite;
import com.example.mymoviecatalogue.presenter.CheckLanguage;
import com.example.mymoviecatalogue.presenter.FavoriteAPI;
import com.example.mymoviecatalogue.view.MainView;
import com.example.mymoviecatalogue.view.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvFavoriteFragment extends Fragment implements MainView {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorLoad;
    private Button refresh;

    private String language;
    private final String LIST_STATE_KEY = "list_key";
    private final String LIST_DATA_KEY = "data_key";

    private FavoriteMovieAdapter adapter;
    private boolean isError;

    private ArrayList<MovieFavorite> favorites = new ArrayList<>();

    private Parcelable savedRecycleViewState;

    public static TvFavoriteFragment newInstance() {
        return new TvFavoriteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null){
            favorites = savedInstanceState.getParcelableArrayList(LIST_DATA_KEY);
            savedRecycleViewState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }

        progressBar = view.findViewById(R.id.pb_fav_tv);
        errorLoad = view.findViewById(R.id.fav_tv_error);
        recyclerView = view.findViewById(R.id.rv_fav_tv_list);
        refresh = view.findViewById(R.id.btn_fav_tv_refresh);

        adapter = new FavoriteMovieAdapter(getContext(), false);
        adapter.notifyDataSetChanged();

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        errorLoad.setText(Objects.requireNonNull(getContext()).getResources().getString(R.string.no_favorite_data));

        language = CheckLanguage.getLanguage(getContext());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (favorites != null && savedRecycleViewState != null){
            Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(savedRecycleViewState);
            adapter.setData(favorites);
        }else {
            displayFavorite();
        }

    }

    private void displayFavorite(){

        showLoading(true);
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getFavorite().observe(this, new Observer<List<FavoriteEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteEntry> aEntity) {

                if (aEntity != null){

                    List<FavoriteEntry> entity = thisMovie(aEntity);
                    favorites.clear();

                    for (int i = 0; i < entity.size(); i++){

                        isError = false;

                        FavoriteAPI.GetFavorite service = FavoriteAPI
                                .getFavorite()
                                .create(FavoriteAPI.GetFavorite.class);

                        Call<MovieFavorite> call = service.getTv(entity.get(i).getMovieid(), MainActivity.API_KEY, language);
                        call.enqueue(new Callback<MovieFavorite>() {

                            @Override
                            public void onResponse(@NonNull Call<MovieFavorite> call, @NonNull Response<MovieFavorite> response) {

                                MovieFavorite movieFavorite = response.body();

                                if (movieFavorite != null){

                                    favorites.add(movieFavorite);
                                    adapter.setData(favorites);

                                    onSuccess(null);

                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<MovieFavorite> call, @NonNull Throwable t) {
                                onError();
                                isError = true;
                            }
                        });

                        if (isError){
                            break;
                        }
                    }

                }else {
                    showLoading(false);
                }

            }
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        Parcelable state = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, state);
        outState.putParcelableArrayList(LIST_DATA_KEY, favorites);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onError() {
        showLoading(false);
        errorLoad.setVisibility(TextView.VISIBLE);
        refresh.setVisibility(Button.VISIBLE);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorLoad.setVisibility(TextView.GONE);
                refresh.setVisibility(Button.GONE);
                displayFavorite();
            }
        });
    }

    @Override
    public void onSuccess(ArrayList<Movie> movie) {
        showLoading(false);
    }

    private void showLoading(boolean state){
        if (state){
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }else {
            progressBar.setVisibility(ProgressBar.GONE);
        }
    }

    private List<FavoriteEntry> thisMovie(List<FavoriteEntry> entity){

        for (int i = 0; i < entity.size(); i++){
            if (entity.get(i).isCategory()){
                entity.remove(entity.get(i));
            }
        }

        return entity;
    }

}

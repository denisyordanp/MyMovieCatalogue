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
import com.example.mymoviecatalogue.model.MovieFavorite;
import com.example.mymoviecatalogue.presenter.CheckLanguage;
import com.example.mymoviecatalogue.presenter.ClientAPI;
import com.example.mymoviecatalogue.view.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mymoviecatalogue.config.Config.API_KEY;

public class MovieFavoriteFragment extends Fragment{

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorLoad;
    private Button refresh;

    private String language;
    private final String LIST_STATE_KEY = "list_key";

    private FavoriteMovieAdapter adapter;
    private boolean isError;

    private ArrayList<MovieFavorite> favorites = new ArrayList<>();

    private Parcelable savedRecycleViewState;

    public static MovieFavoriteFragment newInstance() {
        return new MovieFavoriteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null){
            savedRecycleViewState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }

        progressBar = view.findViewById(R.id.pb_fav_movie);
        errorLoad = view.findViewById(R.id.fav_movie_error);
        recyclerView = view.findViewById(R.id.rv_fav_movie_list);
        refresh = view.findViewById(R.id.btn_fav_movie_refresh);

        adapter = new FavoriteMovieAdapter(getContext(), true);
        adapter.notifyDataSetChanged();

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        errorLoad.setText(Objects.requireNonNull(getContext()).getResources().getString(R.string.no_favourite_data));

        language = CheckLanguage.getLanguage(getContext());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedRecycleViewState != null){
            Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(savedRecycleViewState);
        }

        displayFavorite();

    }

    private void displayFavorite(){

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getFavorite().observe(this, new Observer<List<FavoriteEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteEntry> aEntity) {

                showLoading(true);

                if (favorites != null){
                    favorites.clear();
                }

                if (aEntity != null){

                    List<FavoriteEntry> entity = thisMovie(aEntity);

                    if (entity.isEmpty()){

                        adapter.setData(favorites);
                        onError(true);

                    }else {

                        onError(false);
                        showLoading(true);

                        for (int i = 0; i < entity.size(); i++){

                            isError = false;

                            ClientAPI.GetFavorite service = ClientAPI
                                    .getClient()
                                    .create(ClientAPI.GetFavorite.class);

                            Call<MovieFavorite> call = service.getMovie(entity.get(i).getMovieid(), API_KEY, language);
                            call.enqueue(new Callback<MovieFavorite>() {

                                @Override
                                public void onResponse(@NonNull Call<MovieFavorite> call, @NonNull Response<MovieFavorite> response) {

                                    MovieFavorite movieFavorite = response.body();

                                    if (movieFavorite != null){

                                        favorites.add(movieFavorite);
                                        adapter.setData(favorites);

                                        showLoading(false);

                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<MovieFavorite> call, @NonNull Throwable t) {
                                    onError(true);
                                    isError = true;
                                }
                            });

                            if (isError){
                                break;
                            }
                        }
                    }

                }else {
                    onError(true);
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        Parcelable state = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, state);
        super.onSaveInstanceState(outState);

    }

    private void onError(boolean state){
        showLoading(false);
        if (state){
            errorLoad.setVisibility(TextView.VISIBLE);
            refresh.setVisibility(Button.VISIBLE);
        }else {
            errorLoad.setVisibility(TextView.GONE);
            refresh.setVisibility(Button.GONE);
        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorLoad.setVisibility(TextView.GONE);
                refresh.setVisibility(Button.GONE);
                displayFavorite();
            }
        });
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
            if (!entity.get(i).isCategory()){
                entity.remove(entity.get(i));
            }
        }

        return entity;
    }
}

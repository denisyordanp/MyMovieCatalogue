package com.example.mymoviecatalogue.layout;

import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.adapter.FavoriteMovieAdapter;
import com.example.mymoviecatalogue.database.DatabaseMovie;
import com.example.mymoviecatalogue.database.EntityMovie;
import com.example.mymoviecatalogue.model.Movie;
import com.example.mymoviecatalogue.presenter.CheckLanguage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MovieFavoriteFragment extends Fragment{

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorLoad;

    private ArrayList<Movie> movies = new ArrayList<>();

    private String language;
    private final String LIST_STATE_KEY = "list_key";
    private final String LIST_DATA_KEY = "data_key";

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

        progressBar = view.findViewById(R.id.pb_fav_movie);
        errorLoad = view.findViewById(R.id.fav_movie_error);
        recyclerView = view.findViewById(R.id.rv_fav_movie_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        errorLoad.setText(getContext().getResources().getString(R.string.no_favorite_data));

        language = CheckLanguage.getLanguage(getContext());


//        if (savedInstanceState != null) {
//
//            movies = savedInstanceState.getParcelableArrayList(LIST_DATA_KEY);
//            savedRecycleViewState = savedInstanceState.getParcelable(LIST_STATE_KEY);
//            showRecyclerList(movies);
//
//        } else {
//
//            displayData(language);
//
//        }

        displayData();

    }

    public void displayData() {

        progressBar.setVisibility(ProgressBar.VISIBLE);

        class GetFavorite extends AsyncTask<Void, Void, List<EntityMovie>>{

            @Override
            protected List<EntityMovie> doInBackground(Void... voids) {

                List<EntityMovie> arrayList = DatabaseMovie
                        .getInstance(getContext())
                        .getAppDatabase()
                        .movieDao()
                        .getAll();

                return arrayList;
            }

            @Override
            protected void onPostExecute(List<EntityMovie> entityMovies) {
                super.onPostExecute(entityMovies);

                if (!entityMovies.isEmpty()){

                    for (int i = 0; i < entityMovies.size(); i++){

                        if (!entityMovies.get(i).isCategory()){
                            entityMovies.remove(i);
                        }

                    }

                    showRecyclerList(entityMovies);
                }else {
                    errorLoad.setVisibility(TextView.VISIBLE);
                }
            }
        }

        GetFavorite gt = new GetFavorite();
        gt.execute();

    }

    private void showRecyclerList(final List<EntityMovie> movieFavorite) {

        FavoriteMovieAdapter adapter = new FavoriteMovieAdapter(getContext(), movieFavorite, language);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(ProgressBar.GONE);

//        if (savedRecycleViewState != null) {
//            Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(savedRecycleViewState);
//        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        Parcelable state = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, state);
        outState.putParcelableArrayList(LIST_DATA_KEY, movies);
        super.onSaveInstanceState(outState);

    }

}

package com.example.mymoviecatalogue.layout;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.mymoviecatalogue.model.MovieFavorite;
import com.example.mymoviecatalogue.presenter.CheckLanguage;
import com.example.mymoviecatalogue.presenter.FavoriteAPI;
import com.example.mymoviecatalogue.view.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFavoriteFragment extends Fragment{

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorLoad;

    private ArrayList<Movie> movies = new ArrayList<>();

    private String language;
    private final String LIST_STATE_KEY = "list_key";
    private final String LIST_DATA_KEY = "data_key";

    private FavoriteMovieAdapter adapter;
    private MainViewModel mainViewModel;

    private ArrayList<MovieFavorite> movie = new ArrayList<>();

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

        adapter = new FavoriteMovieAdapter(getContext());
        adapter.notifyDataSetChanged();

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getFavorite().observe(this, getDataFavorite);
    }

    private Observer<ArrayList<MovieFavorite>> getDataFavorite = new Observer<ArrayList<MovieFavorite>>() {
        @Override
        public void onChanged(@Nullable ArrayList<MovieFavorite> movieFavorites) {
            if (movieFavorites != null){
                adapter.setData(movieFavorites);
                progressBar.setVisibility(ProgressBar.GONE);
            }
        }
    };

    public void displayData() {

        progressBar.setVisibility(ProgressBar.VISIBLE);

//        final ArrayList<MovieFavorite> movie = new ArrayList<>();

        class GetFavorite extends AsyncTask<Void, Void, ArrayList<MovieFavorite>>{

            @Override
            protected ArrayList<MovieFavorite> doInBackground(Void... voids) {

                List<EntityMovie> entity = DatabaseMovie
                        .getInstance(getContext())
                        .getAppDatabase()
                        .movieDao()
                        .getAll();

                mainViewModel.setFavorite(language, entity);

//                for (int i = 0; i < entity.size(); i++){
//
//                    FavoriteAPI.GetFavorite service = FavoriteAPI
//                            .getFavorite()
//                            .create(FavoriteAPI.GetFavorite.class);
//
//                    Call<MovieFavorite> call = service.getMovie(entity.get(i).getMovieid(), MainActivity.API_KEY, language);
//                    call.enqueue(new Callback<MovieFavorite>() {
//
//                        @Override
//                        public void onResponse(@NonNull Call<MovieFavorite> call, @NonNull Response<MovieFavorite> response) {
//
//                            MovieFavorite movieFavorite = response.body();
//
//                            if (movieFavorite != null){
//
////                                Log.d("FavRespon", movieFavorite.getTitle());
////
////                                movie.add(movieFavorite);
////
////                                if (!movie.isEmpty()){
////                                    Log.d("FavRespon", "adaan");
////                                }
//
////                                data(movieFavorite);
//
//                                adapter.setData(movieFavorite);
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(@NonNull Call<MovieFavorite> call, @NonNull Throwable t) {
//
//                        }
//                    });
//
//                }
//
////                if (!movie.isEmpty()){
////                    Log.d("FavBackground", "adaan");
////                }else {
////                    Log.d("FavBackground", "kosong");
////                }


                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<MovieFavorite> movieFavorites) {
                super.onPostExecute(movieFavorites);

                progressBar.setVisibility(ProgressBar.GONE);

//                if (movieFavorites != null){
//
//                    for (int i =0; i<movieFavorites.size();i++){
//                        Log.d("Fav", "adaan");
//                    }
////                    Log.d("Fav", "adaan");
////
////                    Log.d("isi", movieFavorites.get(0).getTitle());
////
////                    adapter.setData(movieFavorites);
//
//                }else {
//                    Log.d("Fav", "kosong");
//                }

//                ArrayList<MovieFavorite> movie = new ArrayList<>();

//                if (!entityMovies.isEmpty()){
//
//                    for (int i = 0; i < entityMovies.size(); i++){
//
//                        if (!entityMovies.get(i).isCategory()){
//                            entityMovies.remove(i);
//                        }
//
//
//
//
//                    }
//
//                    showRecyclerList(entityMovies);
//                }else {
//                    errorLoad.setVisibility(TextView.VISIBLE);
//                }
            }
        }

        GetFavorite gt = new GetFavorite();
        gt.execute();

    }

    private void data(MovieFavorite moviess){
        movie.add(moviess);

        if (!movie.isEmpty()){
            Log.d("Fav", movie.get(1).getTitle());
        }

    }

    private void showRecyclerList(final List<EntityMovie> movieFavorite) {

//        FavoriteMovieAdapter adapter = new FavoriteMovieAdapter(getContext(), movieFavorite, language);
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

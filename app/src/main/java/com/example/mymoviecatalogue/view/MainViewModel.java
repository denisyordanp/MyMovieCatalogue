package com.example.mymoviecatalogue.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.mymoviecatalogue.database.EntityMovie;
import com.example.mymoviecatalogue.model.MovieFavorite;
import com.example.mymoviecatalogue.presenter.FavoriteAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private static final String API_KEY = "daed568873f1017055f76a70f110e0fb";
    private MutableLiveData<ArrayList<MovieFavorite>> movies = new MutableLiveData<>();
    private ArrayList<MovieFavorite> movie = new ArrayList<>();

    public void setFavorite(final String language, List<EntityMovie> entity){

        for (int i = 0; i < entity.size(); i++){

            FavoriteAPI.GetFavorite service = FavoriteAPI
                    .getFavorite()
                    .create(FavoriteAPI.GetFavorite.class);

            Call<MovieFavorite> call = service.getMovie(entity.get(i).getMovieid(), API_KEY, language);
            call.enqueue(new Callback<MovieFavorite>() {

                @Override
                public void onResponse(@NonNull Call<MovieFavorite> call, @NonNull Response<MovieFavorite> response) {

                    MovieFavorite movieFavorite = response.body();

                    if (movieFavorite != null){

                        movie.add(movieFavorite);

                        movies.postValue(movie);

                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieFavorite> call, @NonNull Throwable t) {

                }
            });

        }

    }

    public LiveData<ArrayList<MovieFavorite>> getFavorite(){
        return movies;
    }

}

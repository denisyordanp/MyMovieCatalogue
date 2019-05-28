package com.example.mymoviecatalogue.presenter;

import com.example.mymoviecatalogue.model.MovieFavorite;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class FavoriteAPI {

    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    public static Retrofit getFavorite() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public interface GetFavorite {
        @GET("movie/{movie_id}")
        Call<MovieFavorite> getMovie(@Path ("movie_id") int id, @Query("api_key") String apiKey, @Query("language") String language);

        @GET("tv/{movie_id}")
        Call<MovieFavorite> getTv(@Path("movie_id") int id, @Query("api_key") String apiKey, @Query("language") String language);
    }

}

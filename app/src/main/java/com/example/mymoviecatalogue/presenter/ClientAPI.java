package com.example.mymoviecatalogue.presenter;

import com.example.mymoviecatalogue.model.MovieResults;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ClientAPI {

    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/";

    public static Retrofit getClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public interface GetDataService {
        @GET("movie")
        Call<MovieResults> getMovie(@Query("api_key") String apiKey, @Query("language") String language);

        @GET("tv")
        Call<MovieResults> getTv(@Query("api_key") String apiKey, @Query("language") String language);
    }
}

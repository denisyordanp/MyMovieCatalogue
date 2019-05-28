package com.example.mymoviecatalogue.layout;

import android.content.Intent;
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

import com.example.mymoviecatalogue.model.Movie;
import com.example.mymoviecatalogue.model.MovieResults;
import com.example.mymoviecatalogue.presenter.CheckLanguage;
import com.example.mymoviecatalogue.presenter.ItemClickSupport;
import com.example.mymoviecatalogue.adapter.ListMovieAdapter;
import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.presenter.ClientAPI;
import com.example.mymoviecatalogue.view.MainView;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends Fragment implements MainView {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorLoad;
    private Button refresh;

    private ArrayList<Movie> movies = new ArrayList<>();

    private String language;
    private final String LIST_STATE_KEY = "list_key";
    private final String LIST_DATA_KEY = "data_key";
    private Parcelable savedRecycleViewState;

    public static MovieFragment newInstance() {
        return new MovieFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.pb_movie);
        errorLoad = view.findViewById(R.id.tv_movie_error);
        refresh = view.findViewById(R.id.btn_movie_refresh);
        recyclerView = view.findViewById(R.id.rv_movie_list);
        recyclerView.setHasFixedSize(true);

        language = CheckLanguage.getLanguage(getContext());

        if (savedInstanceState != null) {

            movies = savedInstanceState.getParcelableArrayList(LIST_DATA_KEY);
            savedRecycleViewState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            showRecyclerList(movies);

        } else {

            displayData(language);

        }
    }

    public void displayData(String language) {

        progressBar.setVisibility(ProgressBar.VISIBLE);

        ClientAPI.GetDataService service = ClientAPI
                .getClient()
                .create(ClientAPI.GetDataService.class);

        Call<MovieResults> call = service.getMovie(MainActivity.API_KEY, language);
        call.enqueue(new Callback<MovieResults>() {

            @Override
            public void onResponse(@NonNull Call<MovieResults> call, @NonNull Response<MovieResults> response) {

                if (response.body() != null) {
                    onSuccess(response.body().getResults());
                }

            }

            @Override
            public void onFailure(@NonNull Call<MovieResults> call, @NonNull Throwable t) {
                onError();
            }
        });

    }

    private void showRecyclerList(final ArrayList<Movie> mMovies) {

        ListMovieAdapter adapter = new ListMovieAdapter(getContext(), mMovies);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        if (savedRecycleViewState != null) {
            Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(savedRecycleViewState);
        }

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemCLicked(RecyclerView recyclerView, int position, View v) {
                showSelectedMovie(mMovies.get(position));
            }
        });
    }

    private void showSelectedMovie(Movie movie) {

        Movie mMovie = new Movie();
        mMovie.setTitle(movie.getTitle());
        mMovie.setRelease(movie.getRelease());
        mMovie.setVote(movie.getVote());
        mMovie.setDescription(movie.getDescription());
        mMovie.setPoster(movie.getPoster());
        mMovie.setId(movie.getId());

        Intent moveDetailMovie = new Intent(getActivity(), MovieDetailActivity.class);
        moveDetailMovie.putExtra(MovieDetailActivity.EXTRA_MOVIE, mMovie);
        moveDetailMovie.putExtra(MovieDetailActivity.EXTRA_CATEGORY, true);
        moveDetailMovie.putParcelableArrayListExtra(MovieDetailActivity.EXTRA_ALL_MOVIE, movies);
        startActivity(moveDetailMovie);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        Parcelable state = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, state);
        outState.putParcelableArrayList(LIST_DATA_KEY, movies);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onError() {
        progressBar.setVisibility(ProgressBar.GONE);
        errorLoad.setVisibility(TextView.VISIBLE);
        refresh.setVisibility(Button.VISIBLE);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorLoad.setVisibility(TextView.GONE);
                refresh.setVisibility(Button.GONE);
                displayData(language);
            }
        });
    }

    @Override
    public void onSuccess(ArrayList<Movie> movie) {
        progressBar.setVisibility(ProgressBar.GONE);
        movies.addAll(movie);
        showRecyclerList(movies);
    }

}

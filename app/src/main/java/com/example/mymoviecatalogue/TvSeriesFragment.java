package com.example.mymoviecatalogue;


import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class TvSeriesFragment extends Fragment {

    private ArrayList<MainModel.Movie> movies = new ArrayList<>();
    private String[] dataMovieTitle, dataMovieDescription, dataMovieDirectors, dataMovieRelease, dataMovieCategory;
    private TypedArray dataMoviePhoto;
    private RecyclerView recyclerView;

    public TvSeriesFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_series, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_tv_list);
        recyclerView.setHasFixedSize(true);

        prepare();
        addItem();
        showRecyclerList();

    }

    private void showRecyclerList(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ListMovieAdapter listMovieAdapter = new ListMovieAdapter(getContext());
        listMovieAdapter.setMovies(movies);
        recyclerView.setAdapter(listMovieAdapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemCLicked(RecyclerView recyclerView, int position, View v) {
                showSelectedMovie(movies.get(position));
            }
        });
    }

    private void showSelectedMovie(MainModel.Movie movie){

        MainModel.Movie mMovie = new MainModel.Movie();
        mMovie.setTitle(movie.getTitle());
        mMovie.setRelease(movie.getRelease());
        mMovie.setDirectors(movie.getDirectors());
        mMovie.setDescription(movie.getDescription());
        mMovie.setPhoto(movie.getPhoto());

        Intent moveDetailMovie = new Intent(getActivity(), MovieDetail.class);
        moveDetailMovie.putExtra(MovieDetail.EXTRA_MOVIE, mMovie);
        startActivity(moveDetailMovie);

    }

    private void addItem() {
        movies = new ArrayList<>();

        for (int i = 0; i < dataMovieTitle.length; i++) {
            if (dataMovieCategory[i].equals("Series")){
                MainModel.Movie movie = new MainModel.Movie();
                movie.setPhoto(dataMoviePhoto.getResourceId(i, -1));
                movie.setTitle(dataMovieTitle[i]);
                movie.setDescription(dataMovieDescription[i]);
                movie.setDirectors(dataMovieDirectors[i]);
                movie.setRelease(dataMovieRelease[i]);
                movie.setCategory(dataMovieCategory[i]);
                movies.add(movie);
            }
        }
    }

    private void prepare() {
        dataMovieTitle = getResources().getStringArray(R.array.data_movie_title);
        dataMovieRelease = getResources().getStringArray(R.array.data_movie_release);
        dataMovieDirectors = getResources().getStringArray(R.array.data_movie_directors);
        dataMovieDescription = getResources().getStringArray(R.array.data_movie_description);
        dataMoviePhoto = getResources().obtainTypedArray(R.array.data_movie_photo);
        dataMovieCategory = getResources().getStringArray(R.array.category);
    }
}

package com.example.mymoviecatalogue.layout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.adapter.HorizontalListMovieAdapter;
import com.example.mymoviecatalogue.model.Movie;
import com.example.mymoviecatalogue.presenter.ItemClickSupport;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class DetailFragment extends Fragment {

    public static String EXTRA_TITLE = "extra_title",
            EXTRA_NAME = "extra_name",
            EXTRA_DESCRIPTION = "extra_description",
            EXTRA_RELEASE = "extra_release",
            EXTRA_DIRECTORS = "extra_directors",
            EXTRA_AIR_DATE = "extra_air_date",
            EXTRA_PHOTO = "extra_photo",
            EXTRA_ALL_MOVIE = "extra_all_movie";

    private TextView txtDetailTitle, txtDetailRealese, txtDetailDirectors, txtDetailDescription;
    private ImageView imgDetailPhoto;
    private RecyclerView rvAllList;

    private CircularProgressDrawable progress;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtDetailDescription = view.findViewById(R.id.txt_detail_description);
        txtDetailDirectors = view.findViewById(R.id.txt_detail_directors);
        txtDetailRealese = view.findViewById(R.id.txt_detail_realese);
        txtDetailTitle = view.findViewById(R.id.txt_detail_title);
        imgDetailPhoto = view.findViewById(R.id.img_detail_photo);
        rvAllList = view.findViewById(R.id.rv_horizontal_movie_list);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().getString(EXTRA_TITLE) == null) {
                txtDetailTitle.setText(getArguments().getString(EXTRA_NAME));
                txtDetailRealese.setText(getArguments().getString(EXTRA_AIR_DATE));
            } else {
                txtDetailTitle.setText(getArguments().getString(EXTRA_TITLE));
                txtDetailRealese.setText(getArguments().getString(EXTRA_RELEASE));
            }
            txtDetailDescription.setText(getArguments().getString(EXTRA_DESCRIPTION));
            txtDetailDirectors.setText(getArguments().getString(EXTRA_DIRECTORS));

            progress = new CircularProgressDrawable(Objects.requireNonNull(getContext()));
            progress.setStrokeWidth(5f);
            progress.setCenterRadius(30f);
            progress.start();

            Picasso.Builder builder = new Picasso.Builder(getContext());
            builder.downloader(new OkHttp3Downloader(getContext()));
            builder.build().load(MainActivity.BASE_URL + getArguments().getString(EXTRA_PHOTO))
                    .error(R.drawable.ic_error_black_24dp)
                    .fit()
                    .centerCrop()
                    .placeholder(progress)
                    .into(imgDetailPhoto);

        }

        assert getArguments() != null;
        ArrayList<Movie> movies = getArguments().getParcelableArrayList(EXTRA_ALL_MOVIE);
        showRecyclerList(movies);


    }

    private void showRecyclerList(final ArrayList<Movie> mMovies) {

        HorizontalListMovieAdapter adapter = new HorizontalListMovieAdapter(getContext(), mMovies);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvAllList.setLayoutManager(layoutManager);
        rvAllList.setAdapter(adapter);

        ItemClickSupport.addTo(rvAllList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemCLicked(RecyclerView recyclerView, int position, View v) {
                assert mMovies != null;
                showSelectedMovie(mMovies.get(position));
            }
        });
    }

    private void showSelectedMovie(Movie movie) {

        assert getArguments() != null;
        if (getArguments().getString(EXTRA_TITLE) == null) {
            txtDetailTitle.setText(movie.getName());
            txtDetailRealese.setText(movie.getAirDate());
        } else {
            txtDetailTitle.setText(movie.getTitle());
            txtDetailRealese.setText(movie.getRelease());
        }
        txtDetailDescription.setText(movie.getDescription());
        txtDetailDirectors.setText(movie.getVote());

        Picasso.Builder builder = new Picasso.Builder(Objects.requireNonNull(getContext()));
        builder.downloader(new OkHttp3Downloader(getContext()));
        builder.build().load(MainActivity.BASE_URL + movie.getPoster())
                .error(R.drawable.ic_error_black_24dp)
                .fit()
                .centerCrop()
                .placeholder(progress)
                .into(imgDetailPhoto);

    }

}

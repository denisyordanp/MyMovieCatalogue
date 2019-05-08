package com.example.mymoviecatalogue.layout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.model.Movie;

import java.util.ArrayList;
import java.util.Objects;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_ALL_MOVIE = "extra_all_movie";

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        ArrayList<Movie> movies = getIntent().getParcelableArrayListExtra(EXTRA_ALL_MOVIE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DetailFragment detailFragment = new DetailFragment();

        Fragment fragment = fragmentManager.findFragmentByTag(DetailFragment.class.getSimpleName());
        if (!(fragment instanceof DetailFragment)) {

            Bundle bundle = new Bundle();
            bundle.putString(DetailFragment.EXTRA_TITLE, movie.getTitle());
            bundle.putString(DetailFragment.EXTRA_NAME, movie.getName());
            bundle.putString(DetailFragment.EXTRA_DESCRIPTION, movie.getDescription());
            bundle.putString(DetailFragment.EXTRA_RELEASE, movie.getRelease());
            bundle.putString(DetailFragment.EXTRA_AIR_DATE, movie.getAirDate());
            bundle.putString(DetailFragment.EXTRA_DIRECTORS, movie.getVote());
            bundle.putString(DetailFragment.EXTRA_PHOTO, movie.getPoster());
            bundle.putParcelableArrayList(DetailFragment.EXTRA_ALL_MOVIE, movies);
            detailFragment.setArguments(bundle);

            fragmentTransaction.add(R.id.frame_container, detailFragment, DetailFragment.class.getSimpleName());
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}

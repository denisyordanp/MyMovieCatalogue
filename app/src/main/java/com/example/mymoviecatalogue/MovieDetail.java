package com.example.mymoviecatalogue;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MovieDetail extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        MainModel.Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DetailFragment detailFragment = new DetailFragment();

        Fragment fragment = fragmentManager.findFragmentByTag(DetailFragment.class.getSimpleName());
        if (!(fragment instanceof DetailFragment)){

            Bundle bundle = new Bundle();
            bundle.putString(DetailFragment.EXTRA_TITLE, movie.getTitle());
            bundle.putString(DetailFragment.EXTRA_DESCRIPTION, movie.getDescription());
            bundle.putString(DetailFragment.EXTRA_RELEASE, movie.getRelease());
            bundle.putString(DetailFragment.EXTRA_DIRECTORS, movie.getDirectors());
            bundle.putInt(DetailFragment.EXTRA_PHOTO, movie.getPhoto());
            detailFragment.setArguments(bundle);

            fragmentTransaction.add(R.id.frame_container, detailFragment, DetailFragment.class.getSimpleName());
            fragmentTransaction.commit();
        }
    }
}

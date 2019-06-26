package com.example.mymoviecatalogue.layout;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.adapter.HorizontalListMovieAdapter;
import com.example.mymoviecatalogue.database.AppDatabase;
import com.example.mymoviecatalogue.database.FavoriteEntry;
import com.example.mymoviecatalogue.model.Movie;
import com.example.mymoviecatalogue.model.MovieFavorite;
import com.example.mymoviecatalogue.presenter.ItemClickSupport;
import com.example.mymoviecatalogue.view.AppExecutors;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.mymoviecatalogue.config.Config.BASE_IMG_URL;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_ALL_MOVIE = "extra_all_movie";
    public static final String EXTRA_FAVORITE = "extra_favorite";
    public static final String EXTRA_CATEGORY = "extra_category";

    private TextView txtDetailTitle, txtDetailRealese, txtDetailVote, txtDetailDescription;
    private ImageView imgDetailPhoto;
    private RecyclerView rvAllList;
    private Menu menu;

    private Movie movie;
    private MovieFavorite favorites;
    private boolean isMovie = true;
    private boolean isFavorite = false;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle("");

        txtDetailDescription = findViewById(R.id.txt_detail_description);
        txtDetailVote = findViewById(R.id.txt_detail_Vote);
        txtDetailRealese = findViewById(R.id.txt_detail_realese);
        txtDetailTitle = findViewById(R.id.txt_detail_title);
        imgDetailPhoto = findViewById(R.id.img_detail_photo);
        rvAllList = findViewById(R.id.rv_horizontal_movie_list);

        TextView txtDetailTtRealese = findViewById(R.id.txt_tt_detail_realese);
        TextView txtDetailTtVote = findViewById(R.id.txt_tt_detail_vote);
        TextView txtDetailTtOverview = findViewById(R.id.txt_overview);
        TextView txtDetailTtSeemore = findViewById(R.id.tv_title_more);

        txtDetailTtRealese.setText(getResources().getString(R.string.realese_date));
        txtDetailTtVote.setText(getResources().getString(R.string.vote));
        txtDetailTtSeemore.setText(getResources().getString(R.string.see_more));
        txtDetailTtOverview.setText(getResources().getString(R.string.overview));

        mDb = AppDatabase.getInstance(getApplicationContext());

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        isMovie = getIntent().getBooleanExtra(EXTRA_CATEGORY, true);
        favorites = getIntent().getParcelableExtra(EXTRA_FAVORITE);

        ArrayList<Movie> movies = getIntent().getParcelableArrayListExtra(EXTRA_ALL_MOVIE);

        if (movies != null && movie != null) {
            showMovie(movie);
            showRecyclerList(movies);
        } else {
            txtDetailTtSeemore.setVisibility(TextView.GONE);
            showFavorite(favorites);
            isFavorite = true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_menu, menu);

        if (isFavorite) {
            checkFavorite(favorites.getId());
        } else {
            checkFavorite(movie.getId());
        }

        this.menu = menu;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.opt_fav:

                if (isFavorite) {
                    deleteFavorite(favorites.getId());
                    onBackPressed();
                } else {
                    saveFavorite();
                    setMenuFavorite(true);
                }

                break;
        }

        return true;
    }

    private void saveFavorite() {

        String title;
        String date;
        if (isMovie) {
            title = movie.getTitle();
            date = movie.getRelease();
        } else {
            title = movie.getName();
            date = movie.getAirDate();
        }

        final FavoriteEntry favoriteEntry = new FavoriteEntry(movie.getId(), title, movie.getPoster(), date, isMovie);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.favoriteDao().insertFavorite(favoriteEntry);
            }
        });

        Toast.makeText(this, getResources().getString(R.string.favourite), Toast.LENGTH_SHORT).show();
    }

    private void deleteFavorite(final int id) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.favoriteDao().deleteFavoriteWithId(id);
            }
        });

        Toast.makeText(this, getResources().getString(R.string.unfavourite), Toast.LENGTH_SHORT).show();

    }

    @SuppressLint("StaticFieldLeak")
    private void checkFavorite(final int id) {

        new AsyncTask<Void, Void, List<FavoriteEntry>>() {

            @Override
            protected List<FavoriteEntry> doInBackground(Void... voids) {
                return mDb.favoriteDao().loadFavoriteById(id);
            }

            @Override
            protected void onPostExecute(List<FavoriteEntry> favoriteEntries) {
                super.onPostExecute(favoriteEntries);
                if (!favoriteEntries.isEmpty()) {
                    setMenuFavorite(true);
                } else {
                    setMenuFavorite(false);
                }
            }
        }.execute();

    }

    private void setMenuFavorite(boolean state) {
        if (state) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_full_24dp));
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
        }
    }

    private void showMovie(Movie sMovie) {

        this.movie = sMovie;

        if (sMovie.getTitle() == null) {
            isMovie = false;
        }

        if (isMovie) {
            txtDetailTitle.setText(sMovie.getTitle());
            txtDetailRealese.setText(sMovie.getRelease());
        } else {
            txtDetailTitle.setText(sMovie.getName());
            txtDetailRealese.setText(sMovie.getAirDate());
        }

        if (!sMovie.getDescription().isEmpty()) {
            txtDetailDescription.setText(sMovie.getDescription());
        } else {
            txtDetailDescription.setText(getResources().getString(R.string.not_available));
        }

        txtDetailVote.setText(sMovie.getVote());

        CircularProgressDrawable progress = new CircularProgressDrawable(this);
        progress.setStrokeWidth(5f);
        progress.setCenterRadius(30f);
        progress.start();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this));
        builder.build().load(BASE_IMG_URL + sMovie.getPoster())
                .error(R.drawable.ic_error_black_24dp)
                .fit()
                .centerCrop()
                .placeholder(progress)
                .into(imgDetailPhoto);

    }

    private void showFavorite(MovieFavorite sMovie) {

        if (sMovie.getTitle() == null) {
            isMovie = false;
        }

        if (isMovie) {
            txtDetailTitle.setText(sMovie.getTitle());
            txtDetailRealese.setText(sMovie.getRelease());
        } else {
            txtDetailTitle.setText(sMovie.getName());
            txtDetailRealese.setText(sMovie.getAirDate());
        }

        if (!sMovie.getOverview().isEmpty()) {
            txtDetailDescription.setText(sMovie.getOverview());
        } else {
            txtDetailDescription.setText(getResources().getString(R.string.not_available));
        }

        txtDetailVote.setText(sMovie.getVote());

        CircularProgressDrawable progress = new CircularProgressDrawable(this);
        progress.setStrokeWidth(5f);
        progress.setCenterRadius(30f);
        progress.start();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this));
        builder.build().load(BASE_IMG_URL + sMovie.getPosterPath())
                .error(R.drawable.ic_error_black_24dp)
                .fit()
                .centerCrop()
                .placeholder(progress)
                .into(imgDetailPhoto);

    }

    private void showRecyclerList(final ArrayList<Movie> mMovies) {

        HorizontalListMovieAdapter adapter = new HorizontalListMovieAdapter(this, mMovies);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvAllList.setLayoutManager(layoutManager);
        rvAllList.setAdapter(adapter);

        ItemClickSupport.addTo(rvAllList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemCLicked(RecyclerView recyclerView, int position, View v) {
                assert mMovies != null;
                showMovie(mMovies.get(position));
                checkFavorite(mMovies.get(position).getId());
            }
        });
    }

}

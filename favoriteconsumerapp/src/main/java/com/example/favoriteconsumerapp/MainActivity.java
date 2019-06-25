package com.example.favoriteconsumerapp;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.example.favoriteconsumerapp.adapter.FavoriteAdapter;

import static com.example.favoriteconsumerapp.database.DatabaseContract.MovieColumns.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FavoriteAdapter adapter;

    private final int LOAD_FAVOURITE_ID = 111;
    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.pb_fav_movie);
        recyclerView = findViewById(R.id.rv_fav_movie_list);

        adapter = new FavoriteAdapter(this);
        adapter.notifyDataSetChanged();

        showLoading(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(LOAD_FAVOURITE_ID, null, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoading(true);
        loaderManager.restartLoader(LOAD_FAVOURITE_ID, null, this);
    }

    private void showLoading(boolean state) {
        if (state) {
            progressBar.setVisibility(ProgressBar.VISIBLE);
        } else {
            progressBar.setVisibility(ProgressBar.GONE);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CursorLoader(MainActivity.this.getApplicationContext(), CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.setData(cursor);
        showLoading(false);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.setData(null);
    }
}

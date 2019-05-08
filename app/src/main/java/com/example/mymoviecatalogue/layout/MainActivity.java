package com.example.mymoviecatalogue.layout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.presenter.CheckLanguage;

public class MainActivity extends AppCompatActivity {

    int mode;
    public String language;
    public static final String API_KEY = "daed568873f1017055f76a70f110e0fb";
    public static final String BASE_URL = "https://image.tmdb.org/t/p/w500";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Bundle bundle = new Bundle();

            switch (item.getItemId()) {
                case R.id.navigation_movie:

                    Fragment fragment = new MovieFragment();
                    bundle.putString(MovieFragment.DEFAULT_LANGUAGE, language);
                    fragment.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_frame_container, fragment, fragment.getClass().getSimpleName())
                            .commit();

                    return true;

                case R.id.navigation_tv_series:

                    fragment = new TvSeriesFragment();
                    bundle.putString(TvSeriesFragment.DEFAULT_LANGUAGE, language);
                    fragment.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_frame_container, fragment, fragment.getClass().getSimpleName())
                            .commit();

                    return true;
            }

            mode = item.getItemId();
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        language = CheckLanguage.getLanguage(this);

        BottomNavigationView navigationItemView = findViewById(R.id.navigation);
        navigationItemView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            navigationItemView.setSelectedItemId(R.id.navigation_movie);
        } else {
            navigationItemView.setSelectedItemId(savedInstanceState.getInt("nav"));
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("nav", mode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.opt_language) {
            Intent intent = new Intent(MainActivity.this, LangugeActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}

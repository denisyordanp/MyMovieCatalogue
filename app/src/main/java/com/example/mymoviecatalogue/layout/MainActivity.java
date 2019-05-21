package com.example.mymoviecatalogue.layout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.presenter.CheckLanguage;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    int mode;
    public String language;
    public static final String API_KEY = "daed568873f1017055f76a70f110e0fb";
    public static final String BASE_URL = "https://image.tmdb.org/t/p/w500";

    private ViewPager viewPager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_movie:

                    viewPager.setCurrentItem(0);
                    initTitle(item.getTitle().toString());

                    return true;

                case R.id.navigation_tv_series:

                    viewPager.setCurrentItem(1);
                    initTitle(item.getTitle().toString());

                    return true;

                case R.id.navigation_favorite:

                    viewPager.setCurrentItem(2);
                    initTitle(item.getTitle().toString());

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

        viewPager = findViewById(R.id.view_pager);
        MenuPageAdapter adapter = new MenuPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() -1);

        BottomNavigationView navigationItemView = findViewById(R.id.navigation);
        navigationItemView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            navigationItemView.setSelectedItemId(R.id.navigation_movie);
        } else {
            navigationItemView.setSelectedItemId(savedInstanceState.getInt("nav"));
        }

    }

    private void initTitle(String title){
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
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

    private static class MenuPageAdapter extends FragmentPagerAdapter{

        MenuPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            switch (i){
                case 0:
                    return MovieFragment.newInstance();
                case 1:
                    return TvSeriesFragment.newInstance();
                case 2:
                    return FavoriteFragment.newInstance();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}

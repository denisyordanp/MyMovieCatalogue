package com.example.mymoviecatalogue.layout;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.pref.SettingPreference;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private String movieTitle, tvTitle, favoriteTitle;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_movie:

                    viewPager.setCurrentItem(0);
                    initTitle(movieTitle);

                    return true;

                case R.id.navigation_tv_series:

                    viewPager.setCurrentItem(1);
                    initTitle(tvTitle);

                    return true;

                case R.id.navigation_favourite:

                    viewPager.setCurrentItem(2);
                    initTitle(favoriteTitle);

                    return true;

            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPref();

        tvTitle = getResources().getString(R.string.tv_series);
        movieTitle = getResources().getString(R.string.movies);
        favoriteTitle = getResources().getString(R.string.favourite);

        Toolbar toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.view_pager);
        MenuPageAdapter adapter = new MenuPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() -1);

        setSupportActionBar(toolbar);

        BottomNavigationView navigationItemView = findViewById(R.id.navigation);
        navigationItemView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigationItemView.getMenu().getItem(0).setTitle(R.string.movies);
        navigationItemView.getMenu().getItem(1).setTitle(R.string.tv_series);
        navigationItemView.getMenu().getItem(2).setTitle(R.string.favourite);

        if (savedInstanceState == null) {
            navigationItemView.setSelectedItemId(R.id.navigation_movie);
            initTitle(movieTitle);
        } else {
            navigationItemView.setSelectedItemId(savedInstanceState.getInt("nav"));
        }

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

    private void checkPref(){
        SettingPreference settingPreference = new SettingPreference(this);
        Locale myLocale;

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        if (!settingPreference.getPrefLanguage().isEmpty()){

            String language = settingPreference.getPrefLanguage().substring(0,2);
            myLocale = new Locale(language);
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }
    }

    private void initTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

}

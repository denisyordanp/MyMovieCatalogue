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
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.pref.SettingPreference;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "daed568873f1017055f76a70f110e0fb";
    public static final String BASE_URL = "https://image.tmdb.org/t/p/w500";

    private ViewPager viewPager;
    private String actTitle;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_movie:

                    viewPager.setCurrentItem(0);
                    actTitle = item.getTitle().toString();
                    initTitle();

                    return true;

                case R.id.navigation_tv_series:

                    viewPager.setCurrentItem(1);
                    actTitle = item.getTitle().toString();
                    initTitle();

                    return true;

                case R.id.navigation_favorite:

                    viewPager.setCurrentItem(2);
                    actTitle = item.getTitle().toString();
                    initTitle();

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

        viewPager = findViewById(R.id.view_pager);
        MenuPageAdapter adapter = new MenuPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() -1);

        BottomNavigationView navigationItemView = findViewById(R.id.navigation);
        navigationItemView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigationItemView.getMenu().getItem(0).setTitle(R.string.movies);
        navigationItemView.getMenu().getItem(1).setTitle(R.string.tv_series);
        navigationItemView.getMenu().getItem(2).setTitle(R.string.favorite);

        if (savedInstanceState == null) {
            navigationItemView.setSelectedItemId(R.id.navigation_movie);
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

    private void initTitle() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(actTitle);
    }

}

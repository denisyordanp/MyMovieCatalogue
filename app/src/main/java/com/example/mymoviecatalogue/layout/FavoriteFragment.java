package com.example.mymoviecatalogue.layout;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymoviecatalogue.R;

public class FavoriteFragment extends Fragment {

    private static String movies, tv;

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getContext();
        assert context != null;
        movies = context.getResources().getString(R.string.movies);
        tv = context.getResources().getString(R.string.tv_series);

        ViewPager viewPager = view.findViewById(R.id.vp_favorite);
        FavoritePageAdapter adapter = new FavoritePageAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() -1);
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private static class FavoritePageAdapter extends FragmentPagerAdapter{

        FavoritePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            switch (i){
                case 0:
                    return MovieFavoriteFragment.newInstance();

                case 1:
                    return TvFavoriteFragment.newInstance();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0:
                    return movies;

                case 1:
                    return tv;

            }

            return null;
        }
    }
}

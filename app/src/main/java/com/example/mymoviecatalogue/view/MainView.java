package com.example.mymoviecatalogue.view;

import android.support.annotation.Nullable;

import com.example.mymoviecatalogue.model.Movie;

import java.util.ArrayList;

public interface MainView {
    void onError();

    void onSuccess(@Nullable ArrayList<Movie> movie);
}

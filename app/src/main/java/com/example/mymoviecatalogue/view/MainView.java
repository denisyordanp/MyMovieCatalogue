package com.example.mymoviecatalogue.view;

import com.example.mymoviecatalogue.model.Movie;

import java.util.ArrayList;

public interface MainView {
    void onError();
    void onSuccess(ArrayList<Movie> movie);
}

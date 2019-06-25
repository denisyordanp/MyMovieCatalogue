package com.example.favoriteconsumerapp.database;

import android.database.Cursor;

import com.example.favoriteconsumerapp.model.FavoriteEntity;

import java.util.ArrayList;

import static com.example.favoriteconsumerapp.database.DatabaseContract.MovieColumns.CATEGORY;
import static com.example.favoriteconsumerapp.database.DatabaseContract.MovieColumns.DATE;
import static com.example.favoriteconsumerapp.database.DatabaseContract.MovieColumns.POSTER;
import static com.example.favoriteconsumerapp.database.DatabaseContract.MovieColumns.TITLE;

public class MappingHelper {

    public static ArrayList<FavoriteEntity> mapCursorToArrayList(Cursor cursor) {

        ArrayList<FavoriteEntity> movieList = new ArrayList<>();

        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String poster = cursor.getString(cursor.getColumnIndexOrThrow(POSTER));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE));
            boolean category = cursor.getInt(cursor.getColumnIndexOrThrow(CATEGORY)) > 0;
            movieList.add(new FavoriteEntity(title, poster, date, category));
        }

        return movieList;
    }
}

package com.example.mymoviecatalogue.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favoritetable ORDER BY id ASC")
    LiveData<List<FavoriteEntry>> loadAllFavorite();

    @Query("SELECT * FROM favoritetable ORDER BY id ASC")
    List<FavoriteEntry> loadFavorite();

    @Insert
    void insertFavorite(FavoriteEntry favoriteEntry);

    @Query("DELETE FROM favoritetable WHERE movieid = :movie_id")
    void deleteFavoriteWithId(int movie_id);

    @Query("SELECT * FROM favoritetable WHERE movieid = :id")
    List<FavoriteEntry> loadFavoriteById(int id);
}

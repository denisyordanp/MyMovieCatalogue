package com.example.mymoviecatalogue.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM entitymovie")
    List<EntityMovie> getAll();

    @Query("SELECT * FROM entitymovie WHERE movieid = :id")
    List<EntityMovie> check(int id);

    @Insert
    void insert(EntityMovie entityMovie);

    @Delete
    void delete(EntityMovie entityMovie);

//    @Query("DELETE FROM entitymovie WHERE id = :id")
//    void delete(int id);

}

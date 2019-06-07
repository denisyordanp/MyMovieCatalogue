package com.example.mymoviecatalogue.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favoritetable")
public class FavoriteEntry {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "movieid")
    private int movieid;

    @ColumnInfo(name = "category")
    private boolean category;

    @Ignore
    public FavoriteEntry(int movieid, boolean category) {
        this.movieid = movieid;
        this.category = category;
    }

    FavoriteEntry(int id, int movieid, boolean category) {
        this.id = id;
        this.movieid = movieid;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieid() {
        return movieid;
    }

    public void setMovieid(int movieid) {
        this.movieid = movieid;
    }

    public boolean isCategory() {
        return category;
    }

    public void setCategory(boolean category) {
        this.category = category;
    }
}
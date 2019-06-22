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

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "poster")
    private String poster;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "category")
    private boolean category;

    @Ignore
    public FavoriteEntry(int movieid, String title, String poster, String date, boolean category) {
        this.movieid = movieid;
        this.title = title;
        this.poster = poster;
        this.date = date;
        this.category = category;
    }

    FavoriteEntry(int id, int movieid, String title, String poster, String date, boolean category) {
        this.id = id;
        this.movieid = movieid;
        this.title = title;
        this.poster = poster;
        this.date = date;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isCategory() {
        return category;
    }

    public void setCategory(boolean category) {
        this.category = category;
    }
}
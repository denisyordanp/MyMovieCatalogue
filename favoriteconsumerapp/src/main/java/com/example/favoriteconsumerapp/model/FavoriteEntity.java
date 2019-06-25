package com.example.favoriteconsumerapp.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.favoriteconsumerapp.database.DatabaseContract;

import static com.example.favoriteconsumerapp.database.DatabaseContract.getColumnBool;
import static com.example.favoriteconsumerapp.database.DatabaseContract.getColumnString;

public class FavoriteEntity implements Parcelable {

    private String poster, title, date;
    private boolean isMovie;

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isMovie() {
        return isMovie;
    }

    public void setMovie(boolean movie) {
        isMovie = movie;
    }

    public FavoriteEntity() {
    }

    public FavoriteEntity(String title, String poster, String date, boolean isMovie) {
        this.title = title;
        this.poster = poster;
        this.date = date;
        this.isMovie = isMovie;
    }

    public static FavoriteEntity fromContentValues(ContentValues values) {
        final FavoriteEntity favorite = new FavoriteEntity();
        if (values.containsKey(DatabaseContract.MovieColumns.TITLE))
            favorite.setTitle(values.getAsString(DatabaseContract.MovieColumns.TITLE));
        if (values.containsKey(DatabaseContract.MovieColumns.POSTER))
            favorite.setTitle(values.getAsString(DatabaseContract.MovieColumns.POSTER));
        if (values.containsKey(DatabaseContract.MovieColumns.DATE))
            favorite.setTitle(values.getAsString(DatabaseContract.MovieColumns.DATE));
        if (values.containsKey(DatabaseContract.MovieColumns.CATEGORY))
            favorite.setTitle(values.getAsString(DatabaseContract.MovieColumns.CATEGORY));

        return favorite;
    }

    public FavoriteEntity(Cursor cursor) {
        this.title = getColumnString(cursor, DatabaseContract.MovieColumns.TITLE);
        this.date = getColumnString(cursor, DatabaseContract.MovieColumns.DATE);
        this.poster = getColumnString(cursor, DatabaseContract.MovieColumns.POSTER);
        this.isMovie = getColumnBool(cursor, DatabaseContract.MovieColumns.CATEGORY);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.poster);
        dest.writeString(this.title);
        dest.writeString(this.date);
        dest.writeByte(this.isMovie ? (byte) 1 : (byte) 0);
    }

    protected FavoriteEntity(Parcel in) {
        this.poster = in.readString();
        this.title = in.readString();
        this.date = in.readString();
        this.isMovie = in.readByte() != 0;
    }

    public static final Creator<FavoriteEntity> CREATOR = new Creator<FavoriteEntity>() {
        @Override
        public FavoriteEntity createFromParcel(Parcel source) {
            return new FavoriteEntity(source);
        }

        @Override
        public FavoriteEntity[] newArray(int size) {
            return new FavoriteEntity[size];
        }
    };
}

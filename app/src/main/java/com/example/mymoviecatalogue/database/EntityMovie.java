package com.example.mymoviecatalogue.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class EntityMovie implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "movieid")
    private int movieid;

    @ColumnInfo(name = "category")
    private boolean category;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.movieid);
        dest.writeByte(this.category ? (byte) 1 : (byte) 0);
    }

    public EntityMovie() {
    }

    protected EntityMovie(Parcel in) {
        this.id = in.readInt();
        this.movieid = in.readInt();
        this.category = in.readByte() != 0;
    }

    public static final Creator<EntityMovie> CREATOR = new Creator<EntityMovie>() {
        @Override
        public EntityMovie createFromParcel(Parcel source) {
            return new EntityMovie(source);
        }

        @Override
        public EntityMovie[] newArray(int size) {
            return new EntityMovie[size];
        }
    };
}

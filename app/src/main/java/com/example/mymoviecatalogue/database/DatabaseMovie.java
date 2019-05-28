package com.example.mymoviecatalogue.database;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseMovie {

    private Context context;
    private static DatabaseMovie mInstance;

    private AppDatabase appDatabase;

    private DatabaseMovie(Context context){

        this.context = context;
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "FavoriteDatabase").build();

    }

    public static synchronized DatabaseMovie getInstance(Context context){

        if (mInstance == null){
            mInstance = new DatabaseMovie(context);
        }

        return mInstance;
    }

    public AppDatabase getAppDatabase(){
        return appDatabase;
    }
}

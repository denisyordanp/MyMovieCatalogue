package com.example.mymoviecatalogue.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.mymoviecatalogue.database.AppDatabase;
import com.example.mymoviecatalogue.database.FavoriteEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<FavoriteEntry>> favorite;

    public MainViewModel(Application application) {
        super(application);

        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        favorite = database.favoriteDao().loadAllFavorite();

    }

    public LiveData<List<FavoriteEntry>> getFavorite() {
        return favorite;
    }
}

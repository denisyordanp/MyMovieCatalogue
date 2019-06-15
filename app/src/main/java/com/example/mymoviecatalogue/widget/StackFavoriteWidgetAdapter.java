package com.example.mymoviecatalogue.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.database.AppDatabase;
import com.example.mymoviecatalogue.database.FavoriteEntry;
import com.example.mymoviecatalogue.layout.MainActivity;
import com.example.mymoviecatalogue.model.MovieFavorite;
import com.example.mymoviecatalogue.presenter.CheckLanguage;
import com.example.mymoviecatalogue.presenter.ClientAPI;
import com.example.mymoviecatalogue.view.AppExecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StackFavoriteWidgetAdapter implements RemoteViewsService.RemoteViewsFactory {

    private AppDatabase mDb;
    private ArrayList<MovieFavorite> movieFavoritesAll = new ArrayList<>();
    private ArrayList<MovieFavorite> mWidgetItems = new ArrayList<>();
    private List<FavoriteEntry> entries;
    private final Context context;
    private String language;

    StackFavoriteWidgetAdapter(Context context) {
        this.context = context;
    }

    private void setData(MovieFavorite favorite) {
        movieFavoritesAll.add(favorite);
        for (int i = 0; i < movieFavoritesAll.size(); i++) {
            Log.d("Widget-array favorite", movieFavoritesAll.get(i).getTitle());
        }
    }

    private void setDataEntity(List<FavoriteEntry> favorite) {
        entries.addAll(favorite);
        for (int i = 0; i < entries.size(); i++) {
            Log.d("Widget-array favorite", Integer.toString(entries.get(i).getMovieid()));
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCreate() {
        language = CheckLanguage.getLanguage(context);
        mDb = AppDatabase.getInstance(context);

        if (movieFavoritesAll.isEmpty()) {
            Log.d("Widget-ondata", "kosong");
        } else {
            Log.d("Widget-ondata", "adaan");
        }


        new AsyncTask<Context, Void, List<FavoriteEntry>>() {
            @Override
            protected List<FavoriteEntry> doInBackground(Context... contexts) {
                List<FavoriteEntry> favoriteEntries = null;
                mDb.favoriteDao().loadFavorite();
                return favoriteEntries;
            }

            @Override
            protected void onPostExecute(List<FavoriteEntry> favoriteEntries) {
                super.onPostExecute(favoriteEntries);

                if (favoriteEntries != null) {
                    Log.d("Widget-favorite", "adaan");
                    entries.addAll(favoriteEntries);
                }
            }
        }.execute(context);

//        entries = mDb.favoriteDao().loadFavorite();

//        AppExecutors.getInstance().networkIO().execute(new Runnable() {
//            @Override
//            public void run() {
//
//                List<FavoriteEntry> entity = thisMovie(mDb.favoriteDao().loadFavorite());
//
//                if (!entity.isEmpty()){
////                    favoriteEntries = entity;
//                    setDataEntity(entity);
//
//                    for (int i = 0; i < entity.size(); i++){
//
//                        ClientAPI.GetFavorite service = ClientAPI
//                                .getClient()
//                                .create(ClientAPI.GetFavorite.class);
//
//                        Call<MovieFavorite> call = service.getMovie(entity.get(i).getMovieid(), MainActivity.API_KEY, language);
//                        call.enqueue(new Callback<MovieFavorite>() {
//
//                            ArrayList<MovieFavorite> movieFavoritesAll = new ArrayList<>();
//
//                            @Override
//                            public void onResponse(@NonNull Call<MovieFavorite> call, @NonNull Response<MovieFavorite> response) {
//
//                                MovieFavorite movieFavorite = response.body();
//
//                                Log.d("Widget", "onRespon");
//
//                                if (movieFavorite != null){
//
//                                    setData(movieFavorite);
//                                    onDataSetChanged();
////                                    movieFavoritesAll.add(movieFavorite);
////                                    Log.d("Widget", movieFavoritesAll.get(0).getTitle());
//
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(@NonNull Call<MovieFavorite> call, @NonNull Throwable t) {
//
//                            }
//                        });
//                    }
//                }else {
//                    Log.d("Widget", "kosong");
//                }
//            }
//        });
    }

    @Override
    public void onDataSetChanged() {
//        if (entries.isEmpty()){
//            Log.d("Widget-ondata", "kosong");
//        }else {
//            Log.d("Widget-ondata", "adaan");
//        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Log.d("Widget-getCount", "getCount");
        return entries.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Log.d("Widget", "getView");

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);
//        rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));

        Bitmap bmp = null;
        try {
            bmp = Glide.with(context)
                    .asBitmap()
                    .load(MainActivity.BASE_URL + movieFavoritesAll.get(position).getPosterPath())
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            rv.setImageViewBitmap(R.id.imageView, bmp);
            Log.d("Widget", "mashok");
        } catch (InterruptedException | ExecutionException e) {
            Log.d("Widget", "error");
        }

        Bundle extras = new Bundle();
        extras.putInt(FavoriteMovieWidget.EXTRA_ITEM, position);
        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private List<FavoriteEntry> thisMovie(List<FavoriteEntry> entity) {

        for (int i = 0; i < entity.size(); i++) {
            if (!entity.get(i).isCategory()) {
                entity.remove(entity.get(i));
            }
        }

        return entity;
    }

}

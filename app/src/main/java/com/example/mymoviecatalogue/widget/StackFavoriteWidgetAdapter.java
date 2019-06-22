package com.example.mymoviecatalogue.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.database.AppDatabase;
import com.example.mymoviecatalogue.database.MovieContract;
import com.example.mymoviecatalogue.layout.MainActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StackFavoriteWidgetAdapter implements RemoteViewsService.RemoteViewsFactory {

    private AppDatabase mDb;
    private List<Bitmap> mWidgetItems = new ArrayList<>();
    private final Context context;
    private Cursor cursor;
    private int mAppWIdgetId;

    StackFavoriteWidgetAdapter(Context context, Intent intent) {
        this.context = context;
        mAppWIdgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        mDb = AppDatabase.getInstance(context);
    }

    @Override
    public void onDataSetChanged() {

        cursor = mDb.favoriteDao().loadFavoriteCursor();
        Binder.clearCallingIdentity();

        while (cursor.moveToNext()) {
            Bitmap b;
            try {
                b = Picasso.with(context).load(MainActivity.BASE_URL + MovieContract.getColumnString(cursor, MovieContract.MovieColumns.POSTER)).get();
                mWidgetItems.add(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        cursor.close();
    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));

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

}

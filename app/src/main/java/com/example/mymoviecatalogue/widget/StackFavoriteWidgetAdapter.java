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
import com.example.mymoviecatalogue.database.FavoriteEntry;
import com.example.mymoviecatalogue.database.MovieContract;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.mymoviecatalogue.config.Config.BASE_IMG_URL;

public class StackFavoriteWidgetAdapter implements RemoteViewsService.RemoteViewsFactory {

    private List<Bitmap> mWidgetItems = new ArrayList<>();
    private List<FavoriteEntry> favoriteEntryList = new ArrayList<>();

    private Context mContext;
    private Cursor cursor;

    private int mAppWIdgetId;

    StackFavoriteWidgetAdapter(Context context, Intent intent) {
        mContext = context;
        mAppWIdgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        getFavourite(mContext);

//        for (FavoriteEntry favoriteEntry : favoriteEntryList){
//            Bitmap b;
//            try {
//                b = Picasso.with(mContext).load(BASE_IMG_URL + favoriteEntry.getPoster()).get();
//                mWidgetItems.add(b);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    private void getFavourite(Context mContext) {
        final long token = Binder.clearCallingIdentity();
        getResolver(mContext);
        Binder.restoreCallingIdentity(token);
    }

    private void getResolver(Context context) {
        cursor = context.getContentResolver().query(MovieContract.MovieColumns.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                favoriteEntryList.add(new FavoriteEntry(
                        MovieContract.getColumnInt(cursor, MovieContract.MovieColumns.MV_ID),
                        MovieContract.getColumnString(cursor, MovieContract.MovieColumns.TITLE),
                        MovieContract.getColumnString(cursor, MovieContract.MovieColumns.POSTER),
                        MovieContract.getColumnString(cursor, MovieContract.MovieColumns.DATE),
                        MovieContract.getColumnInt(cursor, MovieContract.MovieColumns.CATEGORY) > 0
                ));
            }
        }

    }

    @Override
    public void onDestroy() {
        cursor.close();
    }

    @Override
    public int getCount() {
        return favoriteEntryList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        Bitmap b;
        try {
            b = Picasso.with(mContext).load(BASE_IMG_URL + favoriteEntryList.get(position).getPoster()).get();
            rv.setImageViewBitmap(R.id.imageView, b);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));

        Bundle extras = new Bundle();
        extras.putString(FavoriteMovieWidget.EXTRA_ITEM, favoriteEntryList.get(position).getTitle());
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

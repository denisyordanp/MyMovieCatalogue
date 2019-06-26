package com.example.mymoviecatalogue.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.mymoviecatalogue.database.AppDatabase;
import com.example.mymoviecatalogue.database.FavoriteDao;

import java.util.Objects;

import static com.example.mymoviecatalogue.config.Config.AUTHORITY;
import static com.example.mymoviecatalogue.config.Config.Database.TB_NAME;
import static com.example.mymoviecatalogue.config.Config.SCHEME;

public class FavoriteProvider extends ContentProvider {

    private static final int CODE_FAVOURITE = 1;

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TB_NAME)
            .build();
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private FavoriteDao favoriteDao;

    static {
        MATCHER.addURI(AUTHORITY, TB_NAME, CODE_FAVOURITE);
    }

    @Override
    public boolean onCreate() {
        AppDatabase database = AppDatabase.getInstance(Objects.requireNonNull(getContext()).getApplicationContext());
        favoriteDao = database.favoriteDao();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int code = MATCHER.match(uri);
        Cursor cursor;
        if (code == CODE_FAVOURITE) {
            cursor = favoriteDao.loadFavoriteCursor();
            cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
            return cursor;
        }
        throw new IllegalArgumentException("Unknown Uri: " + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        if (MATCHER.match(uri) == CODE_FAVOURITE) {
            return "vnd.android.cursor.dir/" + AUTHORITY + "." + TB_NAME;
        }
        throw new IllegalArgumentException("Unknown Uri: " + uri);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

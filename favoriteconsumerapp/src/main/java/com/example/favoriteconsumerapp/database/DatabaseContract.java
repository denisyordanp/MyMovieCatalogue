package com.example.favoriteconsumerapp.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    private static final String AUTHORITY = "com.example.mymoviecatalogue.provider.FavoriteProvider";
    private static final String SCHEME = "content";

    public static final class MovieColumns implements BaseColumns {

        public static final String DB_NAME = "FavoriteDatabase";
        public static final String TB_NAME = "favoritetable";
        public static final String MV_ID = "movieid";
        public static final String TITLE = "title";
        public static final String POSTER = "poster";
        public static final String DATE = "date";
        public static final String CATEGORY = "category";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TB_NAME)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static boolean getColumnBool(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName)) > 0;
    }

}

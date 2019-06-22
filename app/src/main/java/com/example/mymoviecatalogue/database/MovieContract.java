package com.example.mymoviecatalogue.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String AUTHORITY = "com.example.mymoviecatalogue";

    public static final class MovieColumns implements BaseColumns {

        public static final String DB_NAME = "FavoriteDatabase";
        public static final String TB_NAME = "favoritetable";
        public static final String TITLE = "title";
        public static final String POSTER = "poster";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
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

}

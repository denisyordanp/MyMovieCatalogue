package com.example.mymoviecatalogue.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.example.mymoviecatalogue.config.Config.AUTHORITY;
import static com.example.mymoviecatalogue.config.Config.SCHEME;

public class MovieContract {

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static final class MovieColumns implements BaseColumns {

        public static final String MV_ID = "movieid";
        public static final String TITLE = "title";
        public static final String POSTER = "poster";
        public static final String DATE = "date";
        public static final String CATEGORY = "category";
        static final String TB_NAME = "favoritetable";
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TB_NAME)
                .build();
    }

}

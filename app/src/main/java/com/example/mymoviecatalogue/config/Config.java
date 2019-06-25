package com.example.mymoviecatalogue.config;

public class Config {

    public static final String API_KEY = "daed568873f1017055f76a70f110e0fb";
    public static final String BASE_IMG_URL = "https://image.tmdb.org/t/p/w500";
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final int NOTIFICATION_ID = 1001;
    public static final String NOTIFICATION_CHANNEL_ID = "11001";
    public static final String NOTIFICATION_CHANNEL_NAME = "11001";
    public static final String AUTHORITY = "com.example.mymoviecatalogue.provider.FavoriteProvider";
    public static final String SCHEME = "content";

    public static class Database {
        public static final String DB_NAME = "FavoriteDatabase";
        public static final String TB_NAME = "favoritetable";
        public static final String MV_ID = "movieid";
        public static final String TITLE = "title";
        public static final String POSTER = "poster";
        public static final String DATE = "date";
        public static final String CATEGORY = "category";
    }
}

package com.example.mymoviecatalogue.presenter;

import android.content.Context;

public class CheckLanguage {

    private static String language;

    public static String getLanguage(Context context) {
        switch (context.getResources().getConfiguration().locale.getLanguage()) {
            case "en":
                language = "en-US";
                break;

            case "in":
                language = "id-ID";
                break;
        }

        return language;
    }

}

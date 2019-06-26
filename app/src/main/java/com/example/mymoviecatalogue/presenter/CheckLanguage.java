package com.example.mymoviecatalogue.presenter;

import android.content.Context;

import com.example.mymoviecatalogue.pref.SettingPreference;

public class CheckLanguage {

    private static String language;

    public static String getLanguage(Context context) {

        SettingPreference settingPreference = new SettingPreference(context);
        String prefLanguage = settingPreference.getPrefLanguage();

        if (prefLanguage.isEmpty()) {
            switch (context.getResources().getConfiguration().locale.getLanguage()) {
                case "en":
                    language = "en-US";
                    break;

                case "in":
                    language = "id-ID";
                    break;
            }
        } else {
            language = prefLanguage;
        }

        return language;
    }

}

package com.example.mymoviecatalogue.pref;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingPreference {

    private static final String PREF_SETTING = "setting";
    private static final String PREF_LANGUAGE = "language";
    private static final String PREF_DAILY_REMINDER = "daily_reminder";
    private static final String PREF_TODAY_REMINDER = "today_reminder";

    private final SharedPreferences preferences;

    public SettingPreference(Context context){
        preferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE);
    }

    public void setPrefLanguage(String language){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_LANGUAGE, language);
        editor.apply();
    }

    public String getPrefLanguage(){
        return preferences.getString(PREF_LANGUAGE, "");
    }

    public void setPrefDailyReminder(boolean state) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_DAILY_REMINDER, state);
        editor.apply();
    }

    public Boolean getPrefDailyReminder() {
        return preferences.getBoolean(PREF_DAILY_REMINDER, false);
    }

    public void setPrefTodayReminder(boolean state) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_TODAY_REMINDER, state);
        editor.apply();
    }

    public Boolean getPrefTodayReminder() {
        return preferences.getBoolean(PREF_TODAY_REMINDER, false);
    }
}

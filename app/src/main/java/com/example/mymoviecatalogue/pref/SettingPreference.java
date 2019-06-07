package com.example.mymoviecatalogue.pref;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingPreference {

    private static final String PREF_SETTING = "setting";
    private static final String PREF_LANGUAGE = "language";

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

}

package com.banasiak.android.webviewapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by variable on 1/7/15.
 */
public class Settings {

    private static final String TAG = Settings.class.getSimpleName();
    
    private static final String KEY_URL = "url";
    
    private static final String KEY_URL_DEF = "http://www.banasiak.com";
    
    
    public static String getUrlPref(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_URL, KEY_URL_DEF);
    }  
    
    public static void setUrlPref(final Context context, final String url) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = settings.edit();
        editor.putString(KEY_URL, url);
        editor.apply();
    }
    
}

package com.banasiak.android.webviewapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by variable on 1/7/15.
 */
public class SettingsActivity extends PreferenceActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();
    
    private static final String KEY_URL = "url";
    private static final String KEY_URL_DEF = "http://www.embeddedlog.com";

    public static final String KEY_ALLOW_CONTENT_ACCESS = "allow_content_access";
    public static final String KEY_ALLOW_FILE_ACCESS = "allow_file_access";
    public static final String KEY_BLOCK_NETWORK_IMAGE = "block_network_image";
    public static final String KEY_BLOCK_NETWORK_LOADS = "block_network_loads";
    public static final String KEY_BUILTIN_ZOOM_CONTROLS = "builtin_zoom_controls";
    public static final String KEY_DISPLAY_ZOOM_CONTROLS = "display_zoom_controls";
    public static final String KEY_DOM_STORAGE = "dom_storage";
    public static final String KEY_GEOLOCATION_ENABLED = "geolocation_enabled";
    public static final String KEY_JAVASCRIPT_CAN_OPEN_WINDOWS_AUTOMATICALLY = "javascript_can_open_windows_automatically";
    public static final String KEY_JAVASCRIPT_ENABLED = "javascript_enabled";
    public static final String KEY_LOADS_IMAGES_AUTOMATICALLY = "loads_images_automatically";
    public static final String KEY_SAVE_FORM_DATA = "save_form_data";
    public static final String KEY_ALLOW_UNIVERSAL_ACCESS_FROM_FILE_URLS = "allow_universal_access_from_file_urls";
    public static final String KEY_ALLOW_FILE_ACCESS_FROM_FILE_URLS = "allow_file_access_from_file_urls";

    public static boolean getBoolPref(final Context context, final String key, final boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static String getUrlPref(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_URL, KEY_URL_DEF);
    }  
    
    public static void setUrlPref(final Context context, final String url) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = settings.edit();
        editor.putString(KEY_URL, url);
        editor.apply();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}

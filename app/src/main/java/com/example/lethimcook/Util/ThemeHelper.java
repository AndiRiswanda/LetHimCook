package com.example.lethimcook.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeHelper {
    private static final String PREFERENCES_NAME = "theme_prefs";
    private static final String KEY_THEME_MODE = "theme_mode";

    // Theme constants
    public static final int MODE_SYSTEM = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
    public static final int MODE_LIGHT = AppCompatDelegate.MODE_NIGHT_NO;
    public static final int MODE_DARK = AppCompatDelegate.MODE_NIGHT_YES;

    public static void applyTheme(int themeMode) {
        AppCompatDelegate.setDefaultNightMode(themeMode);
    }

    public static void saveThemeMode(Context context, int themeMode) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        preferences.edit().putInt(KEY_THEME_MODE, themeMode).apply();
    }

    public static int getThemeMode(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        // Default to system theme on Android 10+ and light theme on older versions
        int defaultMode = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ?
                MODE_SYSTEM : MODE_LIGHT;
        return preferences.getInt(KEY_THEME_MODE, defaultMode);
    }
}
package com.example.data.local

import android.content.Context
import com.example.ui.theme.AppThemeMode

object ThemePrefsHelper {
    private const val PREFS_NAME = "cappuzo_theme_prefs"
    private const val KEY_THEME_MODE = "app_theme_mode"

    fun getThemeMode(context: Context): AppThemeMode {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val id = prefs.getString(KEY_THEME_MODE, AppThemeMode.CLASSICAL_GOLD.id)
        return when (id) {
            AppThemeMode.OLED_BLACK.id -> AppThemeMode.OLED_BLACK
            else -> AppThemeMode.CLASSICAL_GOLD
        }
    }

    fun setThemeMode(context: Context, mode: AppThemeMode) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_THEME_MODE, mode.id).apply()
    }
}

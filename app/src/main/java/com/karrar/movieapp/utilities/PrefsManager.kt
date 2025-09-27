package com.karrar.movieapp.utilities

import android.content.Context

object PrefsManager {

    private const val PREFS_NAME = "app_settings"
    private const val KEY_LANGUAGE = "language"
    private const val KEY_SELECTED_NAV_ITEM = "selected_nav_item"

    fun saveLanguage(context: Context, lang: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, lang).apply()
    }

    fun getLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, "en") ?: "en"
    }

    fun saveSelectedNavItem(context: Context, itemId: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_SELECTED_NAV_ITEM, itemId).apply()
    }

    fun getSelectedNavItem(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_SELECTED_NAV_ITEM, 0)
    }
}
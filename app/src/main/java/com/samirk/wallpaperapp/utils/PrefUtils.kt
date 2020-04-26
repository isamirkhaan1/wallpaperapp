package com.samirk.wallpaperapp.utils

import android.content.Context
import androidx.preference.PreferenceManager

class PrefUtils(context: Context) {

    private val pref = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = pref.edit()

    companion object {
        private const val PREF_TOKEN = "TOKEN"
        private const val PREF_THEME = "THEME"

        private const val DEFAULT_THEME = "color"
    }

    fun getToken() = pref.getString(PREF_TOKEN, null)

    fun getTheme() = pref.getString(PREF_THEME, DEFAULT_THEME)

    fun setToken(token: String) {
        editor.putString(PREF_TOKEN, token)
        editor.commit()
    }
}
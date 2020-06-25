package com.samirk.wallpaperapp.utils

import android.content.Context
import androidx.preference.PreferenceManager

class PrefUtils(context: Context) {

    private val pref = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = pref.edit()

    companion object {

        private const val PREF_TOKEN = "TOKEN"
        private const val PREF_THEME = "THEME"
        private const val PREF_USER_ID = "USER_ID"
    }

    //  User unique ID - unique per app install not per user
    var userId: Long
        get() = pref.getLong(PREF_USER_ID, Constants.DEFAULT_USER_ID)
        set(_userId) {
            editor.putLong(PREF_USER_ID, _userId)
            commit()
        }

    //  Firebase token - device ID
    var token: String
        get() = pref.getString(PREF_TOKEN, Constants.DEFAULT_TOKEN)!!
        set(_token) {
            editor.putString(PREF_TOKEN, _token)
            commit()
        }

    //  Current selected theme of user
    var theme: String
        get() = pref.getString(PREF_THEME, Constants.DEFAULT_THEME.name)!!
        set(_theme) {
            editor.putString(PREF_THEME, _theme)
            commit()
        }

    fun isUserCreated() = userId != Constants.DEFAULT_USER_ID

    fun isTokenGenerated() = token != Constants.DEFAULT_TOKEN

    private fun commit() = editor.commit()
}
package com.samirk.wallpaperapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class PrefUtils private constructor(context: Context) :
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val pref = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = pref.edit()

    private lateinit var changeListener: SharedPreferences.OnSharedPreferenceChangeListener

    companion object {

        private const val PREF_TOKEN = "TOKEN"
        private const val PREF_THEME = "THEME"
        private const val PREF_USER_ID = "USER_ID"
        private const val PREF_CURR_WALLPAPER_URL = "WALLPAPER_URL"

        //singleton
        private var INSTANCE: PrefUtils? = null
        fun getInstance(context: Context): PrefUtils =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PrefUtils(context = context)
            }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        //invoke callback only on theme value
        //now don't ask why
        if (key == PREF_THEME)
            changeListener.onSharedPreferenceChanged(sharedPreferences, key)
    }

    fun registerPrefChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener
    ) {
        changeListener = listener
        pref.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterPrefChangeListener() {
        if (changeListener != null)
            pref.unregisterOnSharedPreferenceChangeListener(changeListener)
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
        get() = pref.getString(PREF_THEME, Constants.EMPTY_STRING)!!
        set(_theme) {
            editor.putString(PREF_THEME, _theme.toLowerCase())
            commit()
        }

    //  Current wallpaper url
    var currWallpaperUrl: String
        get() = pref.getString(PREF_CURR_WALLPAPER_URL, Constants.EMPTY_STRING)!!
        set(_url) {
            editor.putString(PREF_CURR_WALLPAPER_URL, _url)
            commit()
        }

    fun isUserCreated() = userId != Constants.DEFAULT_USER_ID

    fun isTokenGenerated() = token != Constants.DEFAULT_TOKEN

    fun downloadOnlyWithWifi() = pref.getBoolean(Constants.PREF_WIFI_ONLY, false)

    fun getDailyNewWallpaper() = pref.getBoolean(Constants.PREF_DAILY_NEW_WALLPAPER, true)

    private fun commit() = editor.commit()
}
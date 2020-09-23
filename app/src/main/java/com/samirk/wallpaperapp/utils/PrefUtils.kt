package com.samirk.wallpaperapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import java.util.*

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
        private const val PREF_LAST_UPDATE = "LAST_UPDATE"
        private const val PREF_1ST_TIME = "1ST_TIME"

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
        pref.unregisterOnSharedPreferenceChangeListener(changeListener)
    }

    var firstTimeUser : Boolean
    get() = pref.getBoolean(PREF_1ST_TIME, true)
    set(value) {
        editor.putBoolean(PREF_1ST_TIME, false)
        commit()
    }

    //  User unique ID - unique per app install not per user
    var userId: String
        get() = pref.getString(PREF_USER_ID, Constants.EMPTY_STRING)!!
        set(_userId) {
            editor.putString(PREF_USER_ID, _userId)
            commit()

            Analytics.getInstance().setUserId(_userId.toString())
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
            editor.putString(PREF_THEME, _theme.toLowerCase(Locale.ENGLISH))
            commit()

            Analytics.getInstance().setUserProperty(Analytics.PROP_THEME, _theme)
        }

    //  Current wallpaper url
    var currWallpaperUrl: String
        get() = pref.getString(PREF_CURR_WALLPAPER_URL, Constants.EMPTY_STRING)!!
        set(_url) {
            editor.putString(PREF_CURR_WALLPAPER_URL, _url)
            commit()
        }

    //  Latest wallpaper updated on
    var wallpaperLastUpdated: Long
        get() = pref.getLong(PREF_LAST_UPDATE, 0L)
        set(millis) {
            editor.putLong(PREF_LAST_UPDATE, millis)
            commit()
        }

    private var userFeedback: String
        get() = pref.getString(Constants.PREF_FEEDBACK, Constants.EMPTY_STRING)!!
        set(value) {
            editor.putString(Constants.PREF_FEEDBACK, value)
            commit()

            if(value != Constants.EMPTY_STRING)
                Analytics.getInstance().logEvent(Analytics.EVENT_FEEDBACK_SUBMIT)
        }

    /**
     * This is very odd thing,
     * p.s. I'm using preferenceEditText for user feedback
     * It's the easiest I could find
     */
    fun clearUserFeedback() {
        userFeedback = Constants.EMPTY_STRING
    }

    fun isUserCreated() = userId != Constants.EMPTY_STRING

    fun isTokenGenerated() = token != Constants.DEFAULT_TOKEN

    fun downloadOnlyWithWifi() = pref.getBoolean(Constants.PREF_WIFI_ONLY, false)

    fun getDailyNewWallpaper() = pref.getBoolean(Constants.PREF_DAILY_NEW_WALLPAPER, true)

    private fun commit() = editor.commit()
}
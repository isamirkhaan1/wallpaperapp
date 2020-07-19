package com.samirk.wallpaperapp.utils

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase

/**
 * Helper class for FirebaseAnalytics
 * - DEBUG analytics aren't counted
 */
class Analytics private constructor() {

    companion object {

        const val EVENT_SETTINGS_OPEN = "settings_opp"
        const val EVENT_APP_OPEN = "app_open"
        const val EVENT_THEME_CHANGE = "theme_change"
        const val EVENT_WIFI_SETTINGS_UPDATE = "wifi_only"
        const val EVENT_DAILY_WALLPAPER_SETTINGS = "daily_new_wallpaper"
        const val EVENT_FEEDBACK_CLICK = "feedback_click"
        const val EVENT_FEEDBACK_SUBMIT = "feedback_submit"
        const val EVENT_WALLPAPER_CHANGE = "wallpaper_change"
        const val EVENT_APP_INTRO = "app_intro"

        const val PROP_THEME = "theme"
        const val PROP_DAILY_WALLPAPER = "daily_wallpaper_enabled"
        const val PROP_WIFI_ONLY = "wifi_only"

        //singleton
        private val INSTANCE: Analytics? = null
        fun getInstance(): Analytics =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Analytics()
            }
    }

    var analytics: FirebaseAnalytics = Firebase.analytics

    fun logEvent(event: String, value: String = Constants.EMPTY_STRING) {

        if (BuildConfig.DEBUG)
            return

        analytics.logEvent(event) {
            param(FirebaseAnalytics.Param.VALUE, value)
        }
    }

    fun setUserProperty(property: String, value: String) {

        if (BuildConfig.DEBUG)
            return

        analytics.setUserProperty(property, value)
    }

    fun setUserId(id: String) {

        if (BuildConfig.DEBUG)
            return

        analytics.setUserId(id)
    }

}
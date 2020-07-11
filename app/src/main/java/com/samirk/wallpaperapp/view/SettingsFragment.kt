package com.samirk.wallpaperapp.view

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.samirk.wallpaperapp.R
import com.samirk.wallpaperapp.utils.FirestoreUtils
import com.samirk.wallpaperapp.utils.isDeviceConnected
import com.samirk.wallpaperapp.utils.isWifiConnected
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    companion object {

        const val PREF_WIFI_ONLY = "wifi_only"
        const val PREF_DAILY_WALLPAPER = "daily_new_wallpaper"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val switchWifiOnly = findPreference<SwitchPreferenceCompat>(PREF_WIFI_ONLY)
        val switchDailyWallpapers = findPreference<SwitchPreferenceCompat>(PREF_DAILY_WALLPAPER)

        switchWifiOnly!!.onPreferenceChangeListener = this
        switchDailyWallpapers!!.onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {

        if (preference == null)
            return false

        when (preference.key) {
            PREF_WIFI_ONLY -> {
                Timber.d("$PREF_WIFI_ONLY value is changed to $newValue")

                /*
                * if ONLY_WIFI is turned-off
                *   - Check for connectivity
                *   - If connected, fetch latest wallpaper image
                * else if value is turned-on
                *   - Check for WiFi connection (non-metered)
                *   - If connected, fetch latest wallpaper image
                * */

                if (newValue!! == false && isDeviceConnected(requireContext()))
                    FirestoreUtils(requireContext()).fetchTodayWallpaper(theme = null)
                else if (newValue!! == true && isWifiConnected(requireContext()))
                    FirestoreUtils(requireContext()).fetchTodayWallpaper(theme = null)

            }
            PREF_DAILY_WALLPAPER -> {
                Timber.d("$PREF_WIFI_ONLY value is changed to $newValue")

                if (newValue!! == true)
                    FirestoreUtils(requireContext()).fetchTodayWallpaper(theme = null)
            }
        }

        return true
    }
}

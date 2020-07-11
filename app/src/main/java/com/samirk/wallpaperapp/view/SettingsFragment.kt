package com.samirk.wallpaperapp.view

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.samirk.wallpaperapp.R
import com.samirk.wallpaperapp.utils.*

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    lateinit var prefUtils: PrefUtils

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        prefUtils = PrefUtils.getInstance(requireContext())

        findPreference<SwitchPreferenceCompat>(Constants.PREF_WIFI_ONLY)!!
            .onPreferenceChangeListener = this

        findPreference<SwitchPreferenceCompat>(Constants.PREF_DAILY_NEW_WALLPAPER)!!
            .onPreferenceChangeListener = this

    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {

        handleChangeInSettings()
        return true
    }

    private fun handleChangeInSettings() {
        NetworkUtils(requireContext()).handleOnConnectionAvailability()
    }

}

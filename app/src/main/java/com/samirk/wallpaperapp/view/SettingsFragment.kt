package com.samirk.wallpaperapp.view

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.samirk.wallpaperapp.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}

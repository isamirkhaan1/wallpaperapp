package com.samirk.wallpaperapp.view

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.samirk.wallpaperapp.R
import com.samirk.wallpaperapp.utils.Constants
import com.samirk.wallpaperapp.utils.FirestoreUtils
import com.samirk.wallpaperapp.utils.NetworkUtils
import com.samirk.wallpaperapp.utils.PrefUtils

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    lateinit var prefUtils: PrefUtils

    lateinit var etFeedback: EditTextPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        prefUtils = PrefUtils.getInstance(requireContext())

        etFeedback = findPreference<EditTextPreference>(Constants.PREF_FEEDBACK)!!
        etFeedback.onPreferenceChangeListener = this

        findPreference<SwitchPreferenceCompat>(Constants.PREF_WIFI_ONLY)!!
            .onPreferenceChangeListener = this

        findPreference<SwitchPreferenceCompat>(Constants.PREF_DAILY_NEW_WALLPAPER)!!
            .onPreferenceChangeListener = this

    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {

        if (preference is EditTextPreference)
            sendFeedback(text = newValue as String)
        else
        //  This delay make this function act like onChanged()
            Handler().postDelayed({
                handleChangeInSettings()
            }, 50L)

        return true
    }

    private fun handleChangeInSettings() {
        NetworkUtils(requireContext()).handleOnConnectionAvailability()
    }

    private fun sendFeedback(text: String) {

        if (text.isEmpty())
            return

        FirestoreUtils(requireContext()).addUserFeedback(text)

        Toast.makeText(
            requireContext(),
            "Thank you for your feedback <3", Toast.LENGTH_SHORT
        ).show()
    }

}

package com.samirk.wallpaperapp.view

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.samirk.wallpaperapp.R
import com.samirk.wallpaperapp.utils.*
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    lateinit var prefUtils: PrefUtils

    lateinit var etFeedback: EditTextPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        Analytics.getInstance().logEvent(Analytics.EVENT_SETTINGS_OPEN)

        prefUtils = PrefUtils.getInstance(requireContext())

        etFeedback = findPreference<EditTextPreference>(Constants.PREF_FEEDBACK)!!
        etFeedback.onPreferenceChangeListener = this
        etFeedback.setOnPreferenceClickListener {
            Analytics.getInstance().logEvent(Analytics.EVENT_FEEDBACK_CLICK)
            false
        }

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

                val event = if (preference!!.key == Constants.PREF_DAILY_NEW_WALLPAPER)
                    Analytics.EVENT_DAILY_WALLPAPER_SETTINGS
                else
                    Analytics.EVENT_WIFI_SETTINGS_UPDATE
                Analytics.getInstance().logEvent(event, newValue as String)

                val prop = if (preference!!.key == Constants.PREF_DAILY_NEW_WALLPAPER)
                    Analytics.PROP_DAILY_WALLPAPER
                else
                    Analytics.PROP_WIFI_ONLY
                Analytics.getInstance().setUserProperty(prop, newValue as String)

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

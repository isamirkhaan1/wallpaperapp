package com.samirk.wallpaperapp.view

import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.samirk.wallpaperapp.BuildConfig
import com.samirk.wallpaperapp.R
import com.samirk.wallpaperapp.utils.*
import com.samirk.wallpaperapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.home_fragment.*
import timber.log.Timber
import java.util.*

class HomeFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var viewModel: HomeViewModel
    private lateinit var prefUtils: PrefUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        prefUtils = PrefUtils.getInstance(requireContext())

        initUi()
    }

    override fun onResume() {
        super.onResume()

        prefUtils.registerPrefChangeListener(this)
    }

    override fun onPause() {
        super.onPause()

        prefUtils.unregisterPrefChangeListener()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        //  This callback means theme value has been updated
        //  i.e. we can stop loading scree
        showLoading(false)
    }

    /**
     * init UI
     */
    private fun initUi() {

        /*
        * For more responsiveness, every image and corresponding text click-listeners are set
        * */

        img_settings.setOnClickListener(SettingsClickListener())
        tv_settings.setOnClickListener(SettingsClickListener())

        val white = Constants.Theme.WHITE.name.toLowerCase(Locale.ENGLISH)
        img_white_theme.setOnClickListener(ThemeClickListener(theme = white))
        tv_white_theme.setOnClickListener(ThemeClickListener(theme = white))

        val black = Constants.Theme.BLACK.name.toLowerCase(Locale.ENGLISH)
        img_black_theme.setOnClickListener(ThemeClickListener(theme = black))
        tv_black_theme.setOnClickListener(ThemeClickListener(theme = black))

        val color = Constants.Theme.COLOR.name.toLowerCase(Locale.ENGLISH)
        img_color_theme.setOnClickListener(ThemeClickListener(theme = color))
        tv_color_theme.setOnClickListener(ThemeClickListener(theme = color))


        //change progressBar color in older versions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            view_loading.indeterminateDrawable.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.darker_gray
                ), PorterDuff.Mode.SRC_IN
            )
    }

    private fun showLoading(show: Boolean) {

        if (show)
            view_loading.visibility = View.VISIBLE
        else
            view_loading.visibility = View.GONE

        img_settings.isEnabled = !show
        tv_settings.isEnabled = !show
        img_white_theme.isEnabled = !show
        tv_white_theme.isEnabled = !show
        img_black_theme.isEnabled = !show
        tv_black_theme.isEnabled = !show
        img_color_theme.isEnabled = !show
        tv_color_theme.isEnabled = !show

    }

    /**
     *  Click listener for settings icon and text
     */
    private inner class SettingsClickListener : View.OnClickListener {
        override fun onClick(v: View?) {
            findNavController().navigate(R.id.action_home_to_settings)
        }
    }

    /**
     *  Click listener for all theme images and corresponding text
     */
    private inner class ThemeClickListener(val theme: String) : View.OnClickListener {
        override fun onClick(v: View?) {

            if (!isDeviceConnected(context = requireContext())) {
                Toast.makeText(
                    requireContext(),
                    "Device is not connected, please try again",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            //  Theme isn't really updated
            if (theme == prefUtils.theme) {
                Toast.makeText(
                    requireContext(),
                    "theme ${theme.toUpperCase(Locale.ENGLISH)} is already selected",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            showLoading(show = true)

            FirestoreUtils(context = context!!).updateTheme(theme = theme)
        }

    }
}

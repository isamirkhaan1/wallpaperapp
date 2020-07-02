package com.samirk.wallpaperapp.view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.samirk.wallpaperapp.R
import com.samirk.wallpaperapp.utils.Constants
import com.samirk.wallpaperapp.utils.FirestoreUtils
import com.samirk.wallpaperapp.utils.PrefUtils
import com.samirk.wallpaperapp.utils.isDeviceConnected
import com.samirk.wallpaperapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.home_fragment.*

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

        val white = Constants.Theme.WHITE.name.toLowerCase()
        img_white_theme.setOnClickListener(ThemeClickListener(theme = white))
        tv_white_theme.setOnClickListener(ThemeClickListener(theme = white))

        val black = Constants.Theme.BLACK.name.toLowerCase()
        img_black_theme.setOnClickListener(ThemeClickListener(theme = black))
        tv_black_theme.setOnClickListener(ThemeClickListener(theme = black))

        val color = Constants.Theme.COLOR.name.toLowerCase()
        img_color_theme.setOnClickListener(ThemeClickListener(theme = color))
        tv_color_theme.setOnClickListener(ThemeClickListener(theme = color))
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

        /*
        val alpha = if (show) 0.4f else 1f
        img_settings.alpha = alpha
        img_white_theme.alpha = alpha
        img_black_theme.alpha = alpha
        img_color_theme.alpha = alpha*/
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
                    "theme ${theme.toUpperCase()} is already selected",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            showLoading(show = true)

            //TODO create listener for pref.theme, to identified end time
            FirestoreUtils(context = context!!).updateTheme(theme = theme)
        }

    }
}

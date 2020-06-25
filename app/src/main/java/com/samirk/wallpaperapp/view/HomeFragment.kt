package com.samirk.wallpaperapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.samirk.wallpaperapp.R
import com.samirk.wallpaperapp.utils.Constants
import com.samirk.wallpaperapp.utils.FirestoreUtils
import com.samirk.wallpaperapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        initUi()
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

        val white = Constants.Theme.WHITE.name
        img_white_theme.setOnClickListener(ThemeClickListener(theme = white))
        tv_white_theme.setOnClickListener(ThemeClickListener(theme = white))

        val black = Constants.Theme.BLACK.name
        img_black_theme.setOnClickListener(ThemeClickListener(theme = black))
        tv_black_theme.setOnClickListener(ThemeClickListener(theme = black))

        val color = Constants.Theme.COLOR.name
        img_color_theme.setOnClickListener(ThemeClickListener(theme = color))
        tv_color_theme.setOnClickListener(ThemeClickListener(theme = color))
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
            //TODO start loading
            FirestoreUtils(context = context!!).updateTheme(theme = theme)
        }

    }
}

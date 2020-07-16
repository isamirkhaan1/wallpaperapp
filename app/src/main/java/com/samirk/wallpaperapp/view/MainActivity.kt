package com.samirk.wallpaperapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.samirk.wallpaperapp.R
import com.samirk.wallpaperapp.utils.Analytics

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()
        setupNavController()

        Analytics.getInstance().logEvent(Analytics.EVENT_APP_OPEN)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            navController,
            appBarConfiguration
        ) || super.onSupportNavigateUp()
    }

    private fun showActionBar(show: Boolean) {

        //WTF moment
        if (supportActionBar == null) return

        if (show)
            supportActionBar!!.show()
        else
            supportActionBar!!.hide()
    }

    private fun setupActionBar() {

        //  Update back icon
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        bindActionBarWithNavController()
    }

    private fun setupNavController() {

        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.homeFragment) {
                showActionBar(show = false)
            } else if (destination.id == R.id.settingsFragment) {
                showActionBar(show = true)
            }
        }
    }

    /**
     * Bind actionbar with nav controller
     */
    private fun bindActionBarWithNavController() {

        navController = findNavController(R.id.fragment_nav)
        appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }
}


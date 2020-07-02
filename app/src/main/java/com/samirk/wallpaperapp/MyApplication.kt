package com.samirk.wallpaperapp

import android.app.Application
import android.os.Build
import com.samirk.wallpaperapp.utils.FirestoreUtils
import com.samirk.wallpaperapp.utils.listenToNetworkChanges
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        /*
        *   Add user to firestore
        *   FirestoreUtils class manage to add user ONLY ONCE
        * */
        FirestoreUtils(context = this).addUser()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            listenToNetworkChanges(context = this)
        }
    }
}
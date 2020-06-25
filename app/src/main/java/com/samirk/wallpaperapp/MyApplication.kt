package com.samirk.wallpaperapp

import android.app.Application
import com.samirk.wallpaperapp.utils.FirestoreUtils
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
    }
}
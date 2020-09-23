package com.samirk.wallpaperapp

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.multidex.MultiDex
import com.samirk.wallpaperapp.utils.FirebaseUtils
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
        *   FirebaseUtils class manage to add user ONLY ONCE
        * */
        FirebaseUtils(context = this).addUser()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            listenToNetworkChanges(context = this)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        MultiDex.install(this)
    }
}
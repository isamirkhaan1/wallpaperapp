package com.samirk.wallpaperapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.samirk.wallpaperapp.utils.isDeviceConnected
import timber.log.Timber

class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (isDeviceConnected(context))
            Timber.d("device is connected")
        else
            Timber.e("device is NOT connected")

    }
}

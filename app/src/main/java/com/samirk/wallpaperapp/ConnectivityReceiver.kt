package com.samirk.wallpaperapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.samirk.wallpaperapp.utils.*

class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action != "android.net.conn.CONNECTIVITY_CHANGE")
            return

        //make sure internet is connected
        if (isDeviceConnected(context))
            NetworkUtils(context).handleOnConnectionAvailability()
    }
}

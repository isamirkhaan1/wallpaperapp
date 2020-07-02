package com.samirk.wallpaperapp

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.samirk.wallpaperapp.utils.Constants
import com.samirk.wallpaperapp.utils.NOTIFICATION_ID
import com.samirk.wallpaperapp.utils.createNotification

/**
 * Foreground Service for downloading image
 */
class WallpaperService : Service() {

    companion object {
        const val EXTRA_URL = "URL"
    }

    override fun onCreate() {
        super.onCreate()

        //start foreground service
        val notification = createNotification(context = applicationContext)
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent == null || intent.extras == null) {
            stopForeground(true)
            return START_NOT_STICKY
        }

        val url = intent.extras!!.getString(EXTRA_URL, Constants.EMPTY_STRING)
        downloadImg(context = applicationContext, url = url)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()

        //  I'm not sure if this step is necessary though
        stopForeground(true)
    }
}

fun initService(context: Context, intent: Intent) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else {
        context.startService(intent)
    }
}

fun stopService(context: Context, intent: Intent) {
    context.stopService(intent)
}
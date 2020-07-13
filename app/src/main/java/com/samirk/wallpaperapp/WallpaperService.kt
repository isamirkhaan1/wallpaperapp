package com.samirk.wallpaperapp

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import com.samirk.wallpaperapp.utils.Constants
import com.samirk.wallpaperapp.utils.NotificationUtils
import com.samirk.wallpaperapp.utils.PrefUtils
import com.samirk.wallpaperapp.utils.createNotification
import timber.log.Timber

/**
 * Foreground Service for downloading image
 */
class WallpaperService : Service() {

    companion object {
        const val EXTRA_URL = "URL"

        /**
         * start WallpaperService
         */
        fun start(context: Context, url: String) {

            //if URL is old, then do nothing
            if (!isUrlNew(context, url)) {
                Timber.d("URL is old, image already downloaded")
                return
            }

            val intent = getIntent(context, url)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        /**
         * stop WallpaperService
         */
        fun stop(context: Context) {
            context.stopService(getIntent(context, null))
        }

        /**
         *
         */
        private fun getIntent(context: Context, url: String?) =
            Intent(context, WallpaperService::class.java).also {

                val bundle = Bundle().apply {
                    putString(EXTRA_URL, url)
                }
                it.putExtras(bundle)
            }

        /**
         * Make sure to not download same image again
         */
        private fun isUrlNew(context: Context, url: String): Boolean {
            val prefUtils = PrefUtils.getInstance(context)
            return prefUtils.currWallpaperUrl != url
        }
    }

    override fun onCreate() {
        super.onCreate()

        //start foreground service
        val notification = createNotification(context = applicationContext)
        startForeground(NotificationUtils.NOTIFICATION_ID, notification)
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
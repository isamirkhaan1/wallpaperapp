package com.samirk.wallpaperapp

import android.content.Intent
import android.os.Bundle
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.samirk.wallpaperapp.utils.*

class FirebaseService : FirebaseMessagingService() {


    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        //get data
        val data = p0.data.withDefault {
            Constants.EMPTY_STRING
        }
        val imgUrl = data["url"]!!

        val pref = PrefUtils.getInstance(context = applicationContext)
        saveImgUrlLocally(pref = pref, url = imgUrl)

        //daily update is disabled by user
        if (!pref.getDailyNewWallpaper())
            return

        //'Download only with WiFi' is enabled but WiFi is not connected
        if (pref.downloadOnlyWithWifi() && !isWifiConnected(context = applicationContext))
            return

        //make sure internet is connected
        if (!isDeviceConnected(context = applicationContext))
            return

        //start downloading service
        Intent(applicationContext, WallpaperService::class.java).also {

            val bundle = Bundle().apply {
                putString(WallpaperService.EXTRA_URL, imgUrl)
            }
            it.putExtras(bundle)

            initService(it)
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        //update token locally and on firestore
        FirestoreUtils(context = applicationContext).updateToken(token = p0)
    }

    private fun saveImgUrlLocally(pref: PrefUtils, url: String) {
        pref.currWallpaperUrl = url
    }

    private fun initService(intent: Intent) {
        com.samirk.wallpaperapp.initService(context = applicationContext, intent = intent)

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }*/
    }
}
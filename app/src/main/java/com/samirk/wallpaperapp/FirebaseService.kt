package com.samirk.wallpaperapp

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.samirk.wallpaperapp.utils.*

class FirebaseService : FirebaseMessagingService() {

    private val pref = PrefUtils(context = applicationContext)

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        //get data
        val data = p0.data.withDefault {
            Constants.EMPTY_STRING
        }
        val imgUrl = data["url"]!!

        saveImgUrlLocally(url = imgUrl)

        //daily update is disabled by user
        if (!pref.getDailyNewWallpaper())
            return

        //'Download only with WiFi' is enabled but WiFi is not connected
        if (pref.downloadOnlyWithWifi() && !isWifiConnected(context = applicationContext))
            return

        //make sure internet is connected
        if (!isDeviceConnected(context = applicationContext))
            return

        downloadImg(context = applicationContext, url = imgUrl)
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        //update token locally and on firestore
        FirestoreUtils(context = applicationContext).updateToken(token = p0)
    }

    private fun saveImgUrlLocally(url: String) {
        pref.currWallpaperUrl = url
    }
}
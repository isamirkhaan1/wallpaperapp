package com.samirk.wallpaperapp

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.samirk.wallpaperapp.utils.FirestoreUtils
import com.samirk.wallpaperapp.utils.PrefUtils

class FirebaseService : FirebaseMessagingService() {

    private val pref = PrefUtils(context = applicationContext)

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        if(!pref.getDailyNewWallpaper())
            return

        if(pref.downloadOnlyWithWifi()){

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
}
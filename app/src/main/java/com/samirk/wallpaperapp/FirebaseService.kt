package com.samirk.wallpaperapp

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.samirk.wallpaperapp.utils.FirestoreUtils

class FirebaseService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
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
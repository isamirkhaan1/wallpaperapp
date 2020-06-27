package com.samirk.wallpaperapp.utils

import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber

class FirebaseUtils(context: Context) {

    private val pref = PrefUtils(context = context)

    fun subscribe(theme: String) {
        FirebaseMessaging.getInstance()
            .subscribeToTopic(theme)
            .addOnCompleteListener(SubscribeListener(topic = theme))
    }

    fun unsubscribe(theme: String) {
        FirebaseMessaging.getInstance()
            .unsubscribeFromTopic(theme)
            .addOnCompleteListener(UnsubscribeListener(topic = theme))
    }

    fun unsubscribeToAll() {
        FirebaseInstanceId.getInstance().deleteInstanceId()
    }

    fun isSubscribedToAnyTopic() = pref.isUserCreated()

    private fun updateThemeLocally(theme: String) {
        pref.theme = theme
    }

    private inner class SubscribeListener(private val topic: String) : OnCompleteListener<Void> {
        override fun onComplete(p0: Task<Void>) {

            if (p0.isSuccessful) {
                Timber.d("topic %s is successfully subscribed", topic)

                updateThemeLocally(theme = topic)
            } else {
                //TODO check internet connectivity
                subscribe(theme = topic)
            }

        }
    }

    private inner class UnsubscribeListener(private val topic: String) : OnCompleteListener<Void> {
        override fun onComplete(p0: Task<Void>) {

            if (p0.isSuccessful) {
                Timber.d("topic %s is now un-subscribed", topic)

            } else {
                //TODO check internet connectivity
                unsubscribe(theme = topic)
            }
        }
    }
}
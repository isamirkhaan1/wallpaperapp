package com.samirk.wallpaperapp.utils

import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber

class FirebaseUtils(private val context: Context) {

    private val pref = PrefUtils.getInstance(context = context)

    fun addUser() {

        if(isUserCreated()) return

        val auth = FirebaseAuth.getInstance()
        auth.signInAnonymously()
            .addOnCompleteListener(NewUserListener(auth))
    }


    private fun isUserCreated(): Boolean {
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser != null
    }

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

    fun getTodayWallpaper(theme: String) {

    }

    private fun updateThemeLocally(theme: String) {
        pref.theme = theme
    }

    private inner class NewUserListener(private val auth : FirebaseAuth) : OnCompleteListener<AuthResult>{
        override fun onComplete(p0: Task<AuthResult>) {

            if(p0.isSuccessful){
             FirestoreUtils(context).addUser(auth.currentUser!!.uid)
            }
        }

    }

    private inner class SubscribeListener(private val topic: String) : OnCompleteListener<Void> {
        override fun onComplete(p0: Task<Void>) {

            if (p0.isSuccessful) {
                Timber.d("topic %s is successfully subscribed", topic)

                updateThemeLocally(theme = topic)
            }

        }
    }

    private inner class UnsubscribeListener(private val topic: String) : OnCompleteListener<Void> {
        override fun onComplete(p0: Task<Void>) {

            if (p0.isSuccessful) {
                Timber.d("topic %s is now un-subscribed", topic)
            }
        }
    }
}
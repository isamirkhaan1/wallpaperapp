package com.samirk.wallpaperapp.utils

import android.content.Context
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreUtils(private val context: Context) {

    private val firestore = Firebase.firestore

    companion object {
        private const val COLLECTION_TODAY: String = "today"
        private const val COLLECTION_OLD: String = "old"


        private const val COLLECTION_USERS: String = "users"
        private const val COLLECTION_USERS_KEY_THEME: String = "theme"
    }

    fun setFirebaseToken(token: String) {

        val pref = PrefUtils(context = context)

        /*
        * if token is 1st time created
        *   than create document for on same name
        *  else if token get updated
        *   than replace old document name with the new token name
        * */
        val doc = if (pref.getToken().isNullOrEmpty())
            token
        else
            pref.getToken()!!


        firestore.collection(COLLECTION_USERS)
            .document(doc)
            .set(hashMapOf(COLLECTION_USERS_KEY_THEME to pref.getTheme()))
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }

        //save new token locally
        pref.setToken(token = token)
    }

    fun getDataFromIntent(){

    }
}
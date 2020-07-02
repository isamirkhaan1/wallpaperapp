package com.samirk.wallpaperapp.utils

import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class FirestoreUtils(context: Context) {

    private val firestore = Firebase.firestore
    private val pref = PrefUtils(context = context)
    private val firebaseUtils = FirebaseUtils(context = context)

    companion object {

        private const val COLLECTION_TODAY: String = "today"
        private const val COLLECTION_OLD: String = "old"
        private const val COLLECTION_USERS: String = "users"

        private const val KEY_THEME: String = "theme"
        private const val KEY_TOKEN: String = "token"
    }

    /**
     * -    Add new user to firestore,
     * -    Users are uniquely identify by time (milliseconds in long)
     */
    fun addUser() {

        //  Create user ONLY once
        if (pref.isUserCreated()) return

        val currTheme =
            if (pref.theme == Constants.EMPTY_STRING)
                Constants.DEFAULT_THEME.name.toLowerCase()
            else
                pref.theme

        val data = hashMapOf<String, Any>(
            KEY_THEME to currTheme,
            KEY_TOKEN to pref.token
        )

        val uniqueId = getCurrentTimeMillis()
        val completeListener = AddUserListener(uniqueId = uniqueId)
        updateUserCollection(
            uniqueId = uniqueId.toString(),
            hashMap = data, completeListener = completeListener
        )
    }

    /**
     *      Update token on firestore
     */
    fun updateToken(token: String) {

        /*
        *   If user is not created and firebase token is generated
        *   then save the token locally
        * */
        if (!pref.isUserCreated()) {
            updateTokenLocally(token = token)
            return
        }

        val data = hashMapOf<String, Any>(
            KEY_TOKEN to token
        )

        val completeListener = UpdateTokenListener(token = token)
        updateUserCollection(
            pref.userId.toString(),
            data, completeListener
        )
    }

    /**
     *  Update theme on firebase
     */
    fun updateTheme(theme: String) {

        //  Theme should not be updated anywhere before user creation
        if (!pref.isUserCreated()) return

        val data = hashMapOf<String, Any>(
            KEY_THEME to theme
        )

        val completeListener = UpdateThemeListener(theme = theme)
        updateUserCollection(
            pref.userId.toString(),
            data, completeListener
        )
    }

    /**
     * General method for modification in user collection on firestore
     */
    private fun updateUserCollection(
        uniqueId: String, hashMap: HashMap<String, Any>,
        completeListener: OnCompleteListener<Void>
    ) {

        val doc = firestore.collection(COLLECTION_USERS).document(uniqueId)

        /*
        *   Use set method for adding new user
        *   And update method for theme and token modification
         */
        if (completeListener is AddUserListener)
            doc.set(hashMap).addOnCompleteListener(completeListener)
        else
            doc.update(hashMap).addOnCompleteListener(completeListener)
    }

    private fun addUserLocally(userId: Long) {
        pref.userId = userId

        //save default theme as well
        updateThemeLocally(Constants.DEFAULT_THEME.name.toLowerCase())
    }

    private fun updateTokenLocally(token: String) {
        pref.token = token
    }

    private fun updateThemeLocally(theme: String) {

        //1st update firebase subscription, so user can receive new theme notifications
        updateThemeSubscription(theme = theme)
    }

    private fun updateThemeSubscription(theme: String) {

        //unsubscribe from old theme notifications
        firebaseUtils.unsubscribe(pref.theme)

        firebaseUtils.subscribe(theme)
    }

    /**
     *  Listener for adding user to firestore
     */
    private inner class AddUserListener(private val uniqueId: Long) : OnCompleteListener<Void> {

        override fun onComplete(p0: Task<Void>) {

            if (p0.isSuccessful) {
                Timber.d("User added to firestore")

                addUserLocally(uniqueId)

                //  After creating user, now update user device ID on firestore
                if (pref.token != Constants.DEFAULT_TOKEN) updateToken(pref.token)
            } else {
                Timber.e(p0.exception)
            }
        }
    }

    /**
     *  Listener for updating token on firestore
     */
    private inner class UpdateTokenListener(private val token: String) : OnCompleteListener<Void> {

        override fun onComplete(p0: Task<Void>) {

            if (p0.isSuccessful) {
                Timber.d("token updated to firestore")

                updateTokenLocally(token = token)
            } else {
                Timber.e(p0.exception)
            }
        }
    }

    /**
     *  Listener for updating theme on firestore
     */
    private inner class UpdateThemeListener(private val theme: String) : OnCompleteListener<Void> {

        override fun onComplete(p0: Task<Void>) {

            if (p0.isSuccessful) {
                Timber.d("theme updated to firestore")

                updateThemeLocally(theme = theme)
            } else {
                Timber.e(p0.exception)
            }
        }
    }
}
package com.samirk.wallpaperapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi
import timber.log.Timber

//  Connectivity listener for >=N
//  For <N, broadcast receiver is registered in manifest

@RequiresApi(Build.VERSION_CODES.N)
fun listenToNetworkChanges(context: Context) {
    val cm = getConnectivityManager(context)

    cm.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Timber.d("Device is connected")

            NetworkUtils(context).handleOnConnectionAvailability()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Timber.d("Connection is lost")
        }
    })
}

fun isDeviceConnected(context: Context): Boolean {

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        isDeviceConnectedApi23(context = context)
    } else {
        isDeviceConnectedBelowApi23(context = context)
    }
}

fun isWifiConnected(context: Context): Boolean {

    if (!isDeviceConnected(context))
        return false

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        isWifiConnectedApi23(context = context)
    } else {
        isWifiConnectedBelowApi23(context = context)
    }
}

private fun isWifiConnectedBelowApi23(context: Context): Boolean {
    return getConnectivityManager(context = context).isActiveNetworkMetered
}

@RequiresApi(Build.VERSION_CODES.M)
private fun isWifiConnectedApi23(context: Context): Boolean {

    val cm = getConnectivityManager(context = context)
    val network = cm.activeNetwork

    return (cm!!.getNetworkCapabilities(network)
        .hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED))
}

@RequiresApi(Build.VERSION_CODES.M)
private fun isDeviceConnectedApi23(context: Context): Boolean {
    val cm = getConnectivityManager(context = context)
    val network = cm.activeNetwork ?: return false

    return cm!!.getNetworkCapabilities(network)
        .hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

private fun isDeviceConnectedBelowApi23(context: Context): Boolean {
    val cm = getConnectivityManager(context = context)
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

    return activeNetwork?.isConnectedOrConnecting == true
}

private fun getConnectivityManager(context: Context): ConnectivityManager {
    return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}

class NetworkUtils(private val context: Context) {

    fun handleOnConnectionAvailability() {

        val pref = PrefUtils.getInstance(context)

        //If daily update is off, then do nothing
        if (!pref.getDailyNewWallpaper())
            return

        //if user wants update only on non-metered connection
        // and if it's not available then do nothing
        if (pref.downloadOnlyWithWifi())
            if (!isWifiConnected(context))
                return

        // If latest wallpaper is updated today, then don't update it again
        val shouldWallpaperBeUpdated = !(TimeUtils().isToday(pref.wallpaperLastUpdated))
        if (shouldWallpaperBeUpdated)
            FirestoreUtils(context).fetchTodayWallpaperUrl(pref.theme)

    }
}
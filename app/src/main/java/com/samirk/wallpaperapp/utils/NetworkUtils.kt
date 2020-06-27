package com.samirk.wallpaperapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi

fun isDeviceConnected(context: Context): Boolean {

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        isDeviceConnectedApi23(context = context)
    } else {
        isDeviceConnectedBelowApi23(context = context)
    }
}

fun isWifiConnected(context: Context): Boolean {
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

    val cm = getConnectivityManager(context = context) ?: return false
    val network = cm.activeNetwork ?: return false

    return !(cm.getNetworkCapabilities(network)
        .hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED))
}

@RequiresApi(Build.VERSION_CODES.M)
private fun isDeviceConnectedApi23(context: Context): Boolean {
    val cm = getConnectivityManager(context = context) ?: return false
    val network = cm.activeNetwork ?: return false

    return cm.getNetworkCapabilities(network)
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
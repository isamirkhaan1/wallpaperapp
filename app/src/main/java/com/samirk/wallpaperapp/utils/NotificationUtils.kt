package com.samirk.wallpaperapp.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.samirk.wallpaperapp.R

const val NOTIFICATION_ID = 1000

private const val CHANNEL_ID = "10001"

fun createNotification(context: Context): Notification? {

    createNotificationChannel(context)

    return NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher_round)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setContentTitle(context.getString(R.string.tit_notification_tray))
        .setContentText(context.getString(R.string.msg_notification_tray))
        .build()
}

private fun createNotificationChannel(context: Context) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = context.getString(R.string.app_name)
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(CHANNEL_ID, name, importance)

        getNotificationManager(context).createNotificationChannel(channel)
    }
}

private fun getNotificationManager(context: Context) = NotificationManagerCompat.from(context)

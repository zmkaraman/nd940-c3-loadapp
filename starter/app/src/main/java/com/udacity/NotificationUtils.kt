package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat


private val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, detailPendingIntent: PendingIntent) {

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.download_notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(detailPendingIntent)
        .setAutoCancel(true)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.detail),
            detailPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)


    notify(NOTIFICATION_ID, builder.build())

}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}
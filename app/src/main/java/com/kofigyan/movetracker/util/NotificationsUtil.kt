package com.kofigyan.movetracker.util

import android.app.*
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.kofigyan.movetracker.R
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NotificationsUtil @Inject constructor(
    private val context: Context,
    private val notificationManager: NotificationManager
) {

    init {
        cancelAlertNotification()
    }

    private val vibrationFlow = longArrayOf(0, 400, 200, 400)

    fun createOngoingNotification(
        service: Service,
        title: String,
        text: String,
        pendingIntent: PendingIntent?
    ) {
        createOngoingNotificationChannel()
        service.startForeground(
            ONGOING_NOTIFICATION_ID,
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ONGOING_ID)
                .setContentTitle(title)
                .setOngoing(true)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .build()
        )
    }

    fun createAlertNotification(id: Int, title: String, text: String,
                                pendingIntent: PendingIntent? = null) {
        val notificationBuilder = NotificationCompat.Builder(context , NOTIFICATION_CHANNEL_ALERTS_ID)
            .setContentTitle(title)
            .setAutoCancel(true)
            .setContentText(text)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                NOTIFICATION_CHANNEL_ALERTS_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH)
                .apply { vibrationPattern = vibrationFlow }
                .let { notificationManager.createNotificationChannel(it) }
        } else {
            notificationBuilder
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(vibrationFlow)
        }

        notificationManager.notify(id, notificationBuilder.build())
    }

    private fun createOngoingNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                NOTIFICATION_CHANNEL_ONGOING_ID, NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_MIN)
                .let { channel ->
                    notificationManager.createNotificationChannel(channel)
                }
        }
    }

    fun cancelAlertNotification() {
        notificationManager.cancel(ALERT_GPS_NOTIFICATION_ID)
        notificationManager.cancel(ALERT_PERMISSION_NOTIFICATION_ID)
    }
}
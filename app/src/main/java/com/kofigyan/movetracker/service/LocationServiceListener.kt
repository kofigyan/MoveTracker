package com.kofigyan.movetracker.service

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build

class LocationServiceListener(
    private val context: Context,
    private val serviceIntent: Intent
) : LocationSubscription {


    override fun subscribe() {
        context.startService(serviceIntent)
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun subscribeForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            subscribe()
        }
    }


    override fun unsubscribe() {
        context.stopService(serviceIntent)
    }


}
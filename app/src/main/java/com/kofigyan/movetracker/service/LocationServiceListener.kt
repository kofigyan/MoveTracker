package com.kofigyan.movetracker.service

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build

class LocationServiceListener(
    private val context: Context,
    private val serviceIntent: Intent
) : LocationSubscription {


    @TargetApi(Build.VERSION_CODES.O)
    override fun subscribe() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }

    override fun unsubscribe() {
        context.stopService(serviceIntent)
    }

}
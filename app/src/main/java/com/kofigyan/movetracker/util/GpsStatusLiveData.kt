package com.kofigyan.movetracker.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import com.kofigyan.movetracker.R

open class GpsStatusLiveData(private val context: Context) : LiveData<GpsStatus>() {

    override fun onActive() {
        registerReceiver()
        checkAndPublishGpsStatus()
    }

    override fun onInactive() {
        unregisterReceiver()
    }


    private fun checkAndPublishGpsStatus() = if (isGpsEnabled()) {
        postValue(GpsStatus.Enabled())
    } else {
        postValue(GpsStatus.Disabled())
    }


    private fun isGpsEnabled() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.getSystemService(LocationManager::class.java)
            .isProviderEnabled(LocationManager.GPS_PROVIDER)
    } else {
        try {
            Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.LOCATION_MODE
            ) != Settings.Secure.LOCATION_MODE_OFF
        } catch (e: Settings.SettingNotFoundException) {
            false
        }
    }

    private val gpsStateChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) = checkAndPublishGpsStatus()
    }

    private fun registerReceiver() = context.registerReceiver(
        gpsStateChangeReceiver,
        IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
    )

    private fun unregisterReceiver() = context.unregisterReceiver(gpsStateChangeReceiver)
}


sealed class GpsStatus {

    data class Enabled(@StringRes val message: Int = R.string.gps_status_enabled) : GpsStatus()

    data class Disabled(@StringRes val message: Int = R.string.gps_status_disabled) : GpsStatus()
}
package com.kofigyan.movetracker.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.kofigyan.movetracker.R
import com.kofigyan.movetracker.api.FusedLocationApi
import com.kofigyan.movetracker.ui.MainActivity
import com.kofigyan.movetracker.util.*
import dagger.android.AndroidInjection
import javax.inject.Inject

class LocationUpdateService : LifecycleService() {

    @Inject
    lateinit var notificationsUtil: NotificationsUtil

    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    @Inject
    lateinit var fusedLocationClientApi: FusedLocationApi

    private var gpsIsEnabled = false

    private var permissionIsGranted = false

    private lateinit var gpsAndPermissionStatusLiveData: LiveData<Pair<PermissionStatus, GpsStatus>>


    private var pairObserver = Observer<Pair<PermissionStatus, GpsStatus>> { pair ->
        pair?.let {
            handlePermissionStatus(pair.first)
            handleGpsStatus(pair.second)
            stopServiceIfNeeded()
        }
    }


    private fun handleGpsStatus(status: GpsStatus) {
        when (status) {
            is GpsStatus.Enabled -> {
                gpsIsEnabled = true
                registerForLocationTracking()
            }
            is GpsStatus.Disabled -> {
                gpsIsEnabled = false
                showGpsIsDisabledNotification()
            }
        }
    }

    private fun handlePermissionStatus(status: PermissionStatus) {
        when (status) {
            is PermissionStatus.Granted -> {
                permissionIsGranted = true
                registerForLocationTracking()
            }
            is PermissionStatus.Denied -> {
                permissionIsGranted = false
                showPermissionIsMissingNotification()
            }
        }
    }


    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()

        fusedLocationClientApi.setLocationsEventId()

        gpsAndPermissionStatusLiveData = with(application) {
            PermissionStatusLiveData(this).combineLatestWith(GpsStatusLiveData(this))
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        showOnGoingNotification()
        startObservingGpsAndPermissionStatus()

        return Service.START_STICKY
    }

    private fun registerForLocationTracking() {
        if (permissionIsGranted && gpsIsEnabled) {
            sharedPreferencesUtil.locationTrackingState = true
            fusedLocationClientApi.startLocationUpdate(this)
        }
    }


    private fun startObservingGpsAndPermissionStatus() = gpsAndPermissionStatusLiveData
        .observe(this, pairObserver)


    private fun showOnGoingNotification() {

        notificationsUtil.cancelAlertNotification()

        sharedPreferencesUtil.serviceRunningState = true

        Intent(this, MainActivity::class.java)
            .let { PendingIntent.getActivity(this, 0, it, 0) }
            .let { pendingIntent ->
                notificationsUtil.createOngoingNotification( //starts foreground Service with startForeground
                    this,
                    getString(R.string.notif_location_tracking_title),
                    getString(R.string.notif_in_progress),
                    pendingIntent
                )
            }
    }

    private fun showPermissionIsMissingNotification() {

        val resultIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        notificationsUtil.createAlertNotification(
            ALERT_PERMISSION_NOTIFICATION_ID,
            getString(R.string.permission_required_title),
            getString(R.string.permission_required_body),
            pendingIntent
        )
    }


    private fun showGpsIsDisabledNotification() {
        val resultIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        notificationsUtil.createAlertNotification(
            ALERT_GPS_NOTIFICATION_ID,
            getString(R.string.gps_required_title),
            getString(R.string.gps_required_body),
            pendingIntent
        )
    }

    private fun eitherPermissionOrGpsIsDisabled() = gpsIsEnabled.not() || permissionIsGranted.not()

    private fun stopServiceIfNeeded() {
        if (eitherPermissionOrGpsIsDisabled()) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        sharedPreferencesUtil.serviceRunningState = false

    }


}
package com.kofigyan.movetracker.repository

import android.app.Application
import android.content.Intent
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.kofigyan.movetracker.api.FusedLocationApi
import com.kofigyan.movetracker.db.dao.LocationDao
import com.kofigyan.movetracker.service.LocationServiceListener
import com.kofigyan.movetracker.service.LocationSyncWorker
import com.kofigyan.movetracker.service.LocationUpdateService
import com.kofigyan.movetracker.util.NotificationsUtil
import com.kofigyan.movetracker.util.SharedPreferencesUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    application: Application,
    private val locationDao: LocationDao,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val notificationsUtil: NotificationsUtil,
    fusedLocationApi: FusedLocationApi,
    private val workManager: WorkManager
) {

    val locationUpdate = fusedLocationApi.locationUpdate

    suspend fun loadLocationsById(eventCreatorId: String) =
        locationDao.loadLocationsById(eventCreatorId)

    fun isTracking() =
        sharedPreferencesUtil.locationTrackingState && sharedPreferencesUtil.serviceRunningState

    fun getLocationEventId() = sharedPreferencesUtil.locationEventId

    val locationUpdateServiceListener = LocationServiceListener(
        application, Intent(
            application,
            LocationUpdateService::class.java
        )
    )

    fun stopLocationTracking() {
        locationUpdateServiceListener.unsubscribe()
        notificationsUtil.cancelAlertNotification()
    }

    fun syncLocationData() {
        workManager.enqueue(OneTimeWorkRequest.from(LocationSyncWorker::class.java))
    }


}
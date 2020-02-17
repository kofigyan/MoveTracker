package com.kofigyan.movetracker.repository

import android.app.Application
import android.content.Intent
import androidx.lifecycle.LiveData
import com.kofigyan.movetracker.db.dao.EventDao
import com.kofigyan.movetracker.db.dao.LocationDao
import com.kofigyan.movetracker.model.Event
import com.kofigyan.movetracker.model.EventWithLocations
import com.kofigyan.movetracker.model.Location
import com.kofigyan.movetracker.service.LocationServiceListener
import com.kofigyan.movetracker.service.LocationSyncService
import com.kofigyan.movetracker.service.LocationUpdateService
import com.kofigyan.movetracker.util.NotificationsUtil
import com.kofigyan.movetracker.util.SharedPreferencesUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerRepository @Inject constructor(
    application: Application,
    private val eventDao: EventDao,
    private val locationDao: LocationDao,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val notificationsUtil: NotificationsUtil
) {

    val allEventsWithLocations: LiveData<List<EventWithLocations>> =
        eventDao.getEventsWithLocations()


    suspend fun insertEvent(event: Event) = eventDao.insert(event)


    suspend fun insertLocation(location: Location) = locationDao.insert(location)


    suspend fun loadLocationsById(eventCreatorId: String) =
        locationDao.loadLocationsById(eventCreatorId)

    fun isTracking() =
        sharedPreferencesUtil.getLocationTrackingState() && sharedPreferencesUtil.getServiceRunningState()

    fun getLocationEventId() = sharedPreferencesUtil.getLocationEventId()

    val locationUpdateServiceListener = LocationServiceListener(
        application, Intent(
            application,
            LocationUpdateService::class.java
        )
    )

    val locationSyncServiceListener = LocationServiceListener(
        application, Intent(
            application,
            LocationSyncService::class.java
        )
    )

    fun stopLocationTracking() {
        locationUpdateServiceListener.unsubscribe()
        notificationsUtil.cancelAlertNotification()
    }


}
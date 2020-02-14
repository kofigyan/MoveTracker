package com.kofigyan.movetracker.api

import android.app.Application
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.kofigyan.movetracker.model.Location
import com.kofigyan.movetracker.repository.TrackerRepository
import com.kofigyan.movetracker.util.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
import com.kofigyan.movetracker.util.SharedPreferencesUtil
import com.kofigyan.movetracker.util.UPDATE_INTERVAL_IN_MILLISECONDS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Singleton
class FusedLocationApi @Inject constructor(
    val application: Application,
    private val repository: TrackerRepository,
    private val sharedPreferencesUtil: SharedPreferencesUtil
) : CoroutineScope {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    private val _locationUpdate: MutableLiveData<Location> = MutableLiveData()
    val locationUpdate: LiveData<Location>
        get() = _locationUpdate

    lateinit var coroutineJob: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob


    fun setup() {

        coroutineJob = Job()

        sharedPreferencesUtil.setLocationEventId(UUID.randomUUID().toString())

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(application.applicationContext)

        locationRequest = LocationRequest.create().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            smallestDisplacement = 1F
        }

    }

    fun startLocationUpdate() {

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest, locationCallback,
                Looper.myLooper()
            )
            sharedPreferencesUtil.setLocationTrackingState(true)
        } catch (unlikely: SecurityException) {
            sharedPreferencesUtil.setLocationTrackingState(false)
            error("Error when registerLocationUpdates()")
        }
    }

    fun stopLocationUpdate() {
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            sharedPreferencesUtil.setLocationTrackingState(false)
        } catch (unlikely: SecurityException) {
            sharedPreferencesUtil.setLocationTrackingState(true)
            error("Error when unregisterLocationUpdated()")
        }
    }


    private var locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            val location = locationResult?.lastLocation
            val eventLocationId = sharedPreferencesUtil.getLocationEventId()

            if (location != null && eventLocationId != null) {
                val newLocation = Location(
                    UUID.randomUUID().toString(),
                    eventLocationId,
                    location.longitude,
                    location.latitude
                )

                _locationUpdate.postValue(newLocation)

                launch {
                    repository.insertLocation(
                        newLocation
                    )
                }


            }


        }
    }


    fun cancelCoroutine() {
        coroutineJob.cancel()
    }


}
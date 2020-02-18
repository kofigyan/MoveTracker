package com.kofigyan.movetracker.api

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kofigyan.movetracker.db.dao.LocationDao
import com.kofigyan.movetracker.model.Location
import com.kofigyan.movetracker.util.SharedPreferencesUtil
import com.kofigyan.movetracker.util.locationFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Singleton
class FusedLocationApi @Inject constructor(
    val application: Application,
    private val locationDao: LocationDao,
    private val sharedPreferencesUtil: SharedPreferencesUtil
) : CoroutineScope {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
    }


    fun cancelCoroutine() {
        coroutineJob.cancel()
    }

    fun startLocationUpdate(owner: LifecycleOwner) {
        fusedLocationClient.locationFlow()
            .conflate()
            .catch { e ->
                Timber.e("FuseLocationApi: %s", e.message)
            }
            .asLiveData()
            .observe(owner, Observer { location ->
                saveAndUpdateLocation(location)
            })
    }


    private fun saveAndUpdateLocation(location: android.location.Location) {
        val eventLocationId = sharedPreferencesUtil.getLocationEventId()

        eventLocationId?.let {
            val newLocation = Location(
                UUID.randomUUID().toString(),
                it,
                location.longitude,
                location.latitude
            )

            _locationUpdate.postValue(newLocation)

            launch {
                locationDao.insert(
                    newLocation
                )
            }

        }
    }

}
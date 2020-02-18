package com.kofigyan.movetracker.api

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.kofigyan.movetracker.db.dao.LocationDao
import com.kofigyan.movetracker.model.Location
import com.kofigyan.movetracker.util.SharedPreferencesUtil
import com.kofigyan.movetracker.util.locationFlow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Singleton
class FusedLocationApi @Inject constructor(
    private val locationDao: LocationDao,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val fusedLocationClient: FusedLocationProviderClient
) : CoroutineScope {

    private val _locationUpdate: MutableLiveData<Location> = MutableLiveData()
    val locationUpdate: LiveData<Location>
        get() = _locationUpdate

    private val handler = CoroutineExceptionHandler { _, throwable ->

    }

    lateinit var coroutineJob: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + handler + coroutineJob


    fun setup() {

        coroutineJob = SupervisorJob()

        sharedPreferencesUtil.setLocationEventId(UUID.randomUUID().toString())
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
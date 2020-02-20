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
import com.kofigyan.movetracker.util.liveScope
import com.kofigyan.movetracker.util.locationFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FusedLocationApi @Inject constructor(
    private val locationDao: LocationDao,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val fusedLocationClient: FusedLocationProviderClient
)   {

    private val _locationUpdate: MutableLiveData<Location> = MutableLiveData()
    val locationUpdate: LiveData<Location>
        get() = _locationUpdate


    fun setLocationsEventId() = sharedPreferencesUtil.setLocationEventId(UUID.randomUUID().toString())

    fun startLocationUpdate(owner: LifecycleOwner) {
        fusedLocationClient.locationFlow()
            .conflate()
            .catch { e ->
                Timber.e("FuseLocationApi: %s", e.message)
            }
            .asLiveData()
            .observe(owner, Observer { location ->
                saveAndUpdateLocation(location,owner)
            })
    }

    private fun saveAndUpdateLocation(location: android.location.Location, owner: LifecycleOwner) {
        val eventLocationId = sharedPreferencesUtil.getLocationEventId()

        eventLocationId?.let {
            val newLocation = Location(
                UUID.randomUUID().toString(),
                it,
                location.longitude,
                location.latitude
            )

            _locationUpdate.postValue(newLocation)

            owner.liveScope {
                locationDao.insert(
                    newLocation
                )
            }

        }
    }

}
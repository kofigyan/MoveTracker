package com.kofigyan.movetracker.util

import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

fun createLocationRequest() = LocationRequest.create().apply {
    interval = UPDATE_INTERVAL_IN_MILLISECONDS
    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
    smallestDisplacement = 1F
}


fun FusedLocationProviderClient.locationFlow() = callbackFlow<Location> {
    val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            result ?: return
            for (location in result.locations) {
                offer(location)
            }
        }
    }

    requestLocationUpdates(
        createLocationRequest(),
        callback,
        Looper.getMainLooper()
    ).addOnFailureListener { e ->
        close(e)
    }

    awaitClose {
        removeLocationUpdates(callback)
    }
}

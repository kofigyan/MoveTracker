package com.kofigyan.movetracker.util


import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.*
/**
 * Lifecycle aware connectivity checker that exposes the network connected status via a LiveData.
 *
 *The loss of connectivity when the activity is resumed should be a blocker for the user
 * (since we can't get feed items) - in onResume, we should get the connectivity status. If we
 * are NOT connected then we register a listener and wait to be notified. Only once we are
 * connected, we stop listening to connectivity.¬
 */

class ConnectivityChecker(private val connectivityManager: ConnectivityManager) :
    LifecycleObserver {

    private var monitoringConnectivity = false

    private var _connectedStatus = MutableLiveData<Boolean>()
    val connectedStatus: LiveData<Boolean>
        get() = _connectedStatus


    private val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _connectedStatus.postValue(true)

            connectivityManager.unregisterNetworkCallback(this)
            monitoringConnectivity = false
        }

        override fun onLost(network: Network) {
            _connectedStatus.postValue(false)
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopMonitoringConnectivity() {
        if (monitoringConnectivity) {
            connectivityManager.unregisterNetworkCallback(connectivityCallback)
            monitoringConnectivity = false
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startMonitoringConnectivity() {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        val connected = activeNetworkInfo != null && activeNetworkInfo.isConnected
        _connectedStatus.postValue(connected)

        if (!connected) {
            // we don't have internet connection, so we listen to notification on network status
            connectivityManager.registerNetworkCallback(
                NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build(),
                connectivityCallback
            )
            monitoringConnectivity = true
        }
    }


}
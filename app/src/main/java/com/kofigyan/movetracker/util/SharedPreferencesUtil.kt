package com.kofigyan.movetracker.util

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SharedPreferencesUtil @Inject constructor(private val pref: SharedPreferences) {


    fun setLocationTrackingState(value: Boolean) {
        pref.edit()
            .putBoolean(KEY_LOCATION_UPDATES_REQUESTED, value)
            .apply()
    }


    fun getLocationTrackingState(): Boolean {
        return pref
            .getBoolean(KEY_LOCATION_UPDATES_REQUESTED, false)
    }


    fun setServiceRunningState(value: Boolean) {
        pref.edit()
            .putBoolean(KEY_SERVICE_RUNNING_STATE, value)
            .apply()
    }


    fun getServiceRunningState(): Boolean {
        return pref
            .getBoolean(KEY_SERVICE_RUNNING_STATE, false)
    }

    fun setLocationEventId(value: String) {
        pref.edit()
            .putString(KEY_LOCATION_EVENT_ID, value)
            .apply()
    }


    fun getLocationEventId(): String? {
        return pref
            .getString(KEY_LOCATION_EVENT_ID, " ")
    }

    fun clear() {
        pref.edit().clear().apply()
    }


    companion object {
        const val KEY_SERVICE_RUNNING_STATE = "service_running_state"
        const val KEY_LOCATION_UPDATES_REQUESTED = "location-updates-requested"
        const val KEY_LOCATION_EVENT_ID = "location-event-id"
    }
}
package com.kofigyan.movetracker.util

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


@Singleton
class SharedPreferencesUtil @Inject constructor(private val pref: SharedPreferences) {

    var locationTrackingState: Boolean by pref.boolean(KEY_LOCATION_UPDATES_REQUESTED)
    var serviceRunningState: Boolean by pref.boolean(KEY_SERVICE_RUNNING_STATE)
    var locationEventId: String? by pref.string(KEY_LOCATION_EVENT_ID, "")

    private fun SharedPreferences.boolean(
        key: String,
        defaultValue: Boolean = false
    ): ReadWriteProperty<Any, Boolean> =
        PreferenceProperty(
            sharedPreferences = this,
            key = key,
            defaultValue = defaultValue,
            getter = SharedPreferences::getBoolean,
            setter = SharedPreferences.Editor::putBoolean
        )

   private fun SharedPreferences.string(
        key: String,
        defaultValue: String = ""
    ): PreferenceProperty<String?> =
        PreferenceProperty(
            sharedPreferences = this,
            key = key,
            defaultValue = defaultValue,
            getter = SharedPreferences::getString,
            setter = SharedPreferences.Editor::putString
        )


    fun clear() {
        pref.edit().clear().apply()
    }


    companion object {
        const val KEY_SERVICE_RUNNING_STATE = "service_running_state"
        const val KEY_LOCATION_UPDATES_REQUESTED = "location-updates-requested"
        const val KEY_LOCATION_EVENT_ID = "location-event-id"
    }
}


class PreferenceProperty<T>(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defaultValue: T,
    private val getter: SharedPreferences.(String, T) -> T,
    private val setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
) : ReadWriteProperty<Any, T> {

    override fun getValue(thisRef: Any, property: KProperty<*>): T =
        sharedPreferences.getter(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) =
        sharedPreferences.edit()
            .setter(key, value)
            .apply()
}
package com.kofigyan.movetracker.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kofigyan.movetracker.R
import com.kofigyan.movetracker.model.Event
import com.kofigyan.movetracker.model.Resource
import com.kofigyan.movetracker.repository.EventRepository
import com.kofigyan.movetracker.repository.LocationRepository
import com.kofigyan.movetracker.util.DialogMessage
import com.kofigyan.movetracker.util.EventWrapper
import com.kofigyan.movetracker.util.NonNullGpsStatusLiveData
import com.kofigyan.movetracker.util.NonNullPermissionStatusLiveData
import com.kofigyan.movetracker.viewmodel.base.BaseViewModel
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

class LocationViewModel @Inject constructor(
    application: Application,
    private val locationRepository: LocationRepository,
    private val eventRepository: EventRepository
) : BaseViewModel(application) {


    val locationUpdate = locationRepository.locationUpdate

    val permissionStatusLiveData = NonNullPermissionStatusLiveData(application)

    val gpsStatusLiveData = NonNullGpsStatusLiveData(application)

    private val _eventState: MutableLiveData<Resource<String>> = MutableLiveData()
    val eventState: LiveData<Resource<String>>
        get() = _eventState

    init {
        _eventState.value = Resource.ready(R.string.event_text_start)
    }


    private fun validateAndStoreEvent(event: Event, id: String) = viewModelScope.launch {
        val locations = locationRepository.loadLocationsById(id)
        if (locations.size > 1) {
            eventRepository.insertEvent(event)
            dispatchSyncDataDialog()
        } else motionStateMessageDispatcher.value = R.string.msg_still_motion_state
    }

    private fun dispatchSyncDataDialog() {
        dialogMessageDispatcher.value = DialogMessage(
            R.string.sync_location_title,
            R.string.sync_location_body,
            positiveText = R.string.dialog_postive_yes,
            negativeText = R.string.dialog_postive_no,
            action = ::startLocationDataSyncing
        )
    }

    private fun startLocationDataSyncing() = locationRepository.syncLocationData()


    private fun stopTracking() {
        _eventState.value = Resource.ended(R.string.event_text_start)
        val locationEventId = locationRepository.getLocationEventId()
        locationEventId?.let {
            validateAndStoreEvent(
                Event(locationEventId, OffsetDateTime.now()), locationEventId
            )
        }
        locationRepository.stopLocationTracking()
    }

    private fun startTracking() {
        _eventState.value = Resource.started(R.string.event_text_stop)
        locationRepository.locationUpdateServiceListener.subscribe()
    }

    fun respondToTrackingAction() =
        if (locationRepository.isTracking().not()) startTracking() else stopTracking()

    fun navigateToAllEventsActivity(itemId: String) {
        _navigateToAllEvents.value =
            EventWrapper(itemId)
    }

    private fun exitAppOkAction() {
        locationRepository.stopLocationTracking()
        exitAppDispatcher.value = R.string.event_text_stop
    }

    fun onExitAppButtonClicked() {
        if (locationRepository.isTracking()) {
            dialogMessageDispatcher.value = DialogMessage(
                R.string.tracking_discard_title, R.string.tracking_discard_message,
                action = ::exitAppOkAction
            )
        } else exitAppDispatcher.value = R.string.event_text_stop
    }

}
package com.kofigyan.movetracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kofigyan.movetracker.model.EventWithLocations
import com.kofigyan.movetracker.model.ViewState
import com.kofigyan.movetracker.repository.TrackerRepository
import javax.inject.Inject

class EventViewModel @Inject constructor(
    repository: TrackerRepository
) : ViewModel() {


    val allEventsWithLocations: LiveData<List<EventWithLocations>> =
        repository.allEventsWithLocations


    val viewState: LiveData<ViewState> =
        Transformations.map(allEventsWithLocations) { eventLocations: List<EventWithLocations> ->
            when (eventLocations.size) {
                0 -> ViewState.EMPTY
                else -> ViewState.LOADED
            }

        }

}
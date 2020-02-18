package com.kofigyan.movetracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kofigyan.movetracker.model.EventWithLocations

import com.kofigyan.movetracker.model.ViewState.NoData
import com.kofigyan.movetracker.model.ViewState.HasData
import com.kofigyan.movetracker.model.ViewState
import com.kofigyan.movetracker.repository.EventRepository
import javax.inject.Inject

class EventViewModel @Inject constructor(
    eventRepository: EventRepository
) : ViewModel() {


    val allEventsWithLocations: LiveData<List<EventWithLocations>> =
        eventRepository.allEventsWithLocations


    val viewState: LiveData<ViewState> =
        Transformations.map(allEventsWithLocations) { eventLocations: List<EventWithLocations> ->
            when (eventLocations.size) {
                0 -> NoData
                else -> HasData
            }

        }

}
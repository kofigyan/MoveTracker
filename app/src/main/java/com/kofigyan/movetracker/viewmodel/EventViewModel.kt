package com.kofigyan.movetracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kofigyan.movetracker.db.dao.EventDao
import com.kofigyan.movetracker.model.EventWithLocations
import com.kofigyan.movetracker.model.ViewState
import com.kofigyan.movetracker.model.ViewState.HasData
import com.kofigyan.movetracker.model.ViewState.NoData
import javax.inject.Inject

class EventViewModel @Inject constructor(
    private val eventDao: EventDao
) : ViewModel() {

    val allEventsWithLocations = liveData {
        emit(eventDao.getEventsWithLocations())
    }


    val viewState: LiveData<ViewState> =
        Transformations.map(allEventsWithLocations) { eventLocations: List<EventWithLocations> ->
            when (eventLocations.size) {
                0 -> NoData
                else -> HasData
            }

        }

}
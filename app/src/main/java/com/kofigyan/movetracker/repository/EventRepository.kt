package com.kofigyan.movetracker.repository

import androidx.lifecycle.LiveData
import com.kofigyan.movetracker.db.dao.EventDao
import com.kofigyan.movetracker.model.Event
import com.kofigyan.movetracker.model.EventWithLocations
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(private val eventDao: EventDao) {

    val allEventsWithLocations: LiveData<List<EventWithLocations>> =
        eventDao.getEventsWithLocations()

    suspend fun insertEvent(event: Event) = eventDao.insert(event)


}
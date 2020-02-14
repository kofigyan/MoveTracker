package com.kofigyan.movetracker.repository

import androidx.lifecycle.LiveData
import com.kofigyan.movetracker.db.dao.EventDao
import com.kofigyan.movetracker.db.dao.LocationDao
import com.kofigyan.movetracker.model.Event
import com.kofigyan.movetracker.model.EventWithLocations
import com.kofigyan.movetracker.model.Location
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerRepository @Inject constructor(
    private val eventDao: EventDao,
    private val locationDao: LocationDao
) {

    val allEventsWithLocations: LiveData<List<EventWithLocations>> =
        eventDao.getEventsWithLocations()


    suspend fun insertEvent(event: Event) = eventDao.insert(event)


    suspend fun insertLocation(location: Location) = locationDao.insert(location)


    suspend fun loadLocationsById(eventCreatorId: String) = locationDao.loadLocationsById(eventCreatorId)

}
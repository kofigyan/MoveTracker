package com.kofigyan.movetracker.repository

import com.kofigyan.movetracker.db.dao.EventDao
import com.kofigyan.movetracker.model.Event
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(private val eventDao: EventDao) {

    suspend fun insertEvent(event: Event) = eventDao.insert(event)

}
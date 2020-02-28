package com.kofigyan.movetracker.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kofigyan.movetracker.model.Event
import com.kofigyan.movetracker.model.EventWithLocations

@Dao
interface EventDao {

    @Transaction
    @Query("SELECT * FROM Event")
    suspend fun getEventsWithLocations():  List<EventWithLocations>

    @Insert
    suspend fun insert(event: Event)

    @Update
    suspend fun update(event: Event)

    @Query("DELETE FROM Event")
    suspend fun deleteAll()
}
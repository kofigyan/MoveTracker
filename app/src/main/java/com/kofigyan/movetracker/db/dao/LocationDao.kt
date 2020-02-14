package com.kofigyan.movetracker.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kofigyan.movetracker.model.Location

@Dao
interface LocationDao {

    @Insert
    suspend fun insert(location: Location)

    @Update
    suspend fun update(location: Location)

    @Query("DELETE FROM Location")
    suspend fun deleteAll()

    @Query("SELECT * FROM Location WHERE eventCreatorId = :eventCreatorId")
    suspend fun loadLocationsById(eventCreatorId: String):Array<Location>

}
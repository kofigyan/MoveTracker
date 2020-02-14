package com.kofigyan.movetracker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kofigyan.movetracker.db.dao.EventDao
import com.kofigyan.movetracker.db.dao.LocationDao
import com.kofigyan.movetracker.model.Event
import com.kofigyan.movetracker.model.Location
import com.kofigyan.movetracker.db.converter.TrackerTypeConverters


@Database(entities = [Event::class, Location::class], version = 1, exportSchema = false)
@TypeConverters(TrackerTypeConverters::class)
abstract class TrackerRoomDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
    abstract fun locationDao(): LocationDao

}
package com.kofigyan.movetracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Location(
    @PrimaryKey val locationId: String,
    val eventCreatorId: String,
    val longitude: Double,
    val latitude: Double
){
    override fun toString(): String {
        return "$latitude  $longitude"
    }
}
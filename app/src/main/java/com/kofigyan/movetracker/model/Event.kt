package com.kofigyan.movetracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

@Entity
data class Event(@PrimaryKey val eventId: String, val dateCreated:  OffsetDateTime? = null)
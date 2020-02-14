package com.kofigyan.movetracker.model

import androidx.room.Embedded
import androidx.room.Relation

data class EventWithLocations(@Embedded val event: Event,
                            @Relation(
                                parentColumn = "eventId",
                                entityColumn = "eventCreatorId"
                            )
                            val locations: List<Location>)
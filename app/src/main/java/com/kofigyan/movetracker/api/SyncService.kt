package com.kofigyan.movetracker.api

import com.kofigyan.movetracker.model.EventWithLocations
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SyncService {

    @FormUrlEncoded
    @POST("/events")
    suspend fun syncLocationData(@Body  event: EventWithLocations):  ApiResponse<EventWithLocations>
}
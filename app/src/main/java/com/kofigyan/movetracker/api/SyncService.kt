package com.kofigyan.movetracker.api

import androidx.lifecycle.LiveData
import com.kofigyan.movetracker.model.EventWithLocations
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SyncService {

    @FormUrlEncoded
    @POST("/events")
    fun syncLocationData(@Body  event: EventWithLocations): LiveData<ApiResponse<EventWithLocations>>
}
package com.example.mysubmissionandro.data.remote.retrofit

import com.example.mysubmissionandro.data.remote.response.DetailEventResponse
import com.example.mysubmissionandro.data.remote.response.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("events")
    suspend fun getUpcomingEvents(@Query("active") active: Int = 1): EventResponse

    @GET("events")
    suspend fun getFinishedEvents(@Query("active") active: Int = 0): EventResponse

    @GET("events/{id}")
    suspend fun getDetailEvent(@Path("id") id: String): DetailEventResponse

}
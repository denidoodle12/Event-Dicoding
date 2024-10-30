package com.example.mysubmissionandro.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mysubmissionandro.data.local.Result
import com.example.mysubmissionandro.data.remote.response.Event
import com.example.mysubmissionandro.data.remote.response.ListEventsItem
import com.example.mysubmissionandro.data.remote.retrofit.ApiService

class EventRepository (
    private val apiService: ApiService
) {
    private val _upcomingEvents = MutableLiveData<Result<List<ListEventsItem>>>()
    val upcomingEvents: LiveData<Result<List<ListEventsItem>>> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<Result<List<ListEventsItem>>>()
    val finishedEvents: LiveData<Result<List<ListEventsItem>>> = _finishedEvents

    private val _detailEvents = MutableLiveData<Result<Event>>()
    val detailEvents: LiveData<Result<Event>> = _detailEvents

    suspend fun getEventsById(id: String){
        _detailEvents.postValue(Result.Loading)
        try {
            val response = apiService.getDetailEvent(id)
            _detailEvents.postValue(Result.Success(response.event))
        } catch (e: Exception){
            _detailEvents.postValue(Result.Error("Error: ${e.message}"))
        }
    }

    suspend fun getUpcomingEvents() {
        _upcomingEvents.postValue(Result.Loading)
        try {
            val response = apiService.getUpcomingEvents(active = 1)
            _upcomingEvents.postValue(Result.Success(response.listEvents))
        } catch (e: Exception){
            _upcomingEvents.postValue(Result.Error("Error: ${e.message}"))
        }
    }

    suspend fun searchFavEvents(query: String) {
        _finishedEvents.postValue(Result.Loading)
        try {
            val response = apiService.getFinishedEvents(active = 0)
            val filteredEvents = response.listEvents.filter { it.name.contains(query, ignoreCase = true) }
            _finishedEvents.postValue(Result.Success(filteredEvents))
        } catch (e: Exception) {
            _finishedEvents.postValue(Result.Error("Error: ${e.message}"))
        }
    }

    suspend fun getFinishedEvents() {
        _finishedEvents.postValue(Result.Loading)
        try {
            val response = apiService.getFinishedEvents(active = 0)
            _finishedEvents.postValue(Result.Success(response.listEvents))
        } catch (e: Exception){
            _finishedEvents.postValue(Result.Error("Error: ${e.message}"))
        }
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService)
            }.also { instance = it }
    }
}
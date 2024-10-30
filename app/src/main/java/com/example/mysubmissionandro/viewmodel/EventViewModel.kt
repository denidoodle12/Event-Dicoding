package com.example.mysubmissionandro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysubmissionandro.data.remote.EventRepository
import kotlinx.coroutines.launch

class EventViewModel(private val eventRepository: EventRepository) : ViewModel() {
    val upcomingEvents = eventRepository.upcomingEvents
    val finishedEvents = eventRepository.finishedEvents
    val detailEvents = eventRepository.detailEvents

    fun getEventsById(id: String) {
        viewModelScope.launch {
            eventRepository.getEventsById(id)
        }
    }

    fun getUpcomingEvents() {
        viewModelScope.launch {
            eventRepository.getUpcomingEvents()
        }
    }

    fun searchFavEvents(query: String) {
        viewModelScope.launch {
            eventRepository.searchFavEvents(query)
        }
    }

    fun getFinishedEvents() {
        viewModelScope.launch {
            eventRepository.getFinishedEvents()
        }
    }
}
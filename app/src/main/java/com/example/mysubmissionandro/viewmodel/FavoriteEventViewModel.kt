package com.example.mysubmissionandro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysubmissionandro.data.local.FavoriteEventRepository
import com.example.mysubmissionandro.data.local.entity.EventEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteEventViewModel(private val repository: FavoriteEventRepository) : ViewModel() {

    val favoriteEvents: LiveData<List<EventEntity>> = repository.getAllFavoriteEvent()

    fun getFavoriteEventById(id: String): LiveData<EventEntity> {
        return repository.getFavoriteEventById(id)
    }

    fun getAllFavoriteEvent(): LiveData<List<EventEntity>> {
        return repository.getAllFavoriteEvent()
    }

    fun addToFavorite(event: EventEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addToFavorite(event)
        }
    }

    fun removeToFavorite(event: EventEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteToFavorite(event)
        }
    }
}

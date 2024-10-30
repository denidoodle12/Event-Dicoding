package com.example.mysubmissionandro.data.local

import androidx.lifecycle.LiveData
import com.example.mysubmissionandro.data.local.entity.EventEntity
import com.example.mysubmissionandro.data.local.room.EventDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteEventRepository(private val eventDao: EventDao) {

    fun getFavoriteEventById(id: String): LiveData<EventEntity> {
        return eventDao.getFavoriteEventById(id)
    }

    fun getAllFavoriteEvent(): LiveData<List<EventEntity>> {
        return eventDao.getAllFavoriteEvent()
    }

    suspend fun deleteToFavorite(event: EventEntity) {
        withContext(Dispatchers.IO) {
            eventDao.deleteToFavoriteEvent(event)
        }
    }

    suspend fun addToFavorite(event: EventEntity) {
        withContext(Dispatchers.IO) {
            eventDao.insertToFavoriteEvent(event)
        }
    }

    companion object {
        @Volatile
        private var instance: FavoriteEventRepository? = null
        fun getInstance(
            eventDao: EventDao
        ): FavoriteEventRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteEventRepository(eventDao)
            }.also { instance = it }
    }
}

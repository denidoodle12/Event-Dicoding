package com.example.mysubmissionandro.di

import android.content.Context
import com.example.mysubmissionandro.data.local.FavoriteEventRepository
import com.example.mysubmissionandro.data.remote.EventRepository
import com.example.mysubmissionandro.data.local.room.EventDatabase
import com.example.mysubmissionandro.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getDatabase(context)
        return EventRepository.getInstance(apiService)
    }
    fun provideFavoriteRepository(context: Context): FavoriteEventRepository {
        val database = EventDatabase.getDatabase(context)
        return FavoriteEventRepository.getInstance(database.eventsDao())
    }
}
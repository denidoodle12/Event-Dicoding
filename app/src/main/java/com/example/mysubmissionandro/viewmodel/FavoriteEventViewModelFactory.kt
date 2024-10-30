package com.example.mysubmissionandro.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mysubmissionandro.data.local.FavoriteEventRepository
import com.example.mysubmissionandro.di.Injection

class FavoriteEventViewModelFactory(private val favoriteEventRepository: FavoriteEventRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteEventViewModel::class.java)) {
            return FavoriteEventViewModel(favoriteEventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: FavoriteEventViewModelFactory? = null
        fun getInstance(context: Context): FavoriteEventViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: FavoriteEventViewModelFactory(Injection.provideFavoriteRepository(context))
            }
    }
}

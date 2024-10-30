package com.example.mysubmissionandro.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mysubmissionandro.data.local.entity.EventEntity

@Dao
interface EventDao {

    @Query("SELECT * FROM FavoriteEvent")
    fun getAllFavoriteEvent() : LiveData<List<EventEntity>>

    @Query("SELECT * FROM FavoriteEvent WHERE id = :id")
    fun getFavoriteEventById(id: String): LiveData<EventEntity>

    @Delete
    suspend fun deleteToFavoriteEvent(favoriteEvent: EventEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToFavoriteEvent(favoriteEvent: EventEntity)

}

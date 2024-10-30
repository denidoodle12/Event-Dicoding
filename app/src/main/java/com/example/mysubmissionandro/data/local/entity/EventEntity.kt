package com.example.mysubmissionandro.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "FavoriteEvent")
@Parcelize
data class EventEntity (
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id:String,

    @field:ColumnInfo(name = "name")
    val name: String,

    @field:ColumnInfo(name = "mediaCover")
    val mediaCover: String,
) : Parcelable
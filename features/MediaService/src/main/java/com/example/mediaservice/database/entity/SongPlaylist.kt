package com.example.mediaservice.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SongPlaylist(
    @PrimaryKey(autoGenerate = true)
    val id: Long
)
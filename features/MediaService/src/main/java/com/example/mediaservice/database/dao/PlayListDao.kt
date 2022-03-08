package com.example.mediaservice.database.dao

import androidx.room.*
import com.example.mediaservice.repository.models.Playlist

@Dao
interface PlayListDao {

    @Query("SELECT * FROM playlist")
    suspend fun findAll() : List<Playlist>

    @Insert
    suspend fun insert(playlist: Playlist)

    @Update
    suspend fun update(playlist: Playlist)

    @Delete
    suspend fun delete(playlist: Playlist)
}
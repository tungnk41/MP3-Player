package com.example.mediaservice.database.dao

import androidx.room.*
import com.example.mediaservice.repository.models.Playlist

@Dao
interface PlayListDao {

    @Query("SELECT * FROM playlist WHERE userId = :userId")
    suspend fun findAll(userId: Long) : List<Playlist>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlist: Playlist) : Long

    @Update
    suspend fun update(playlist: Playlist)

    @Delete
    suspend fun delete(playlist: Playlist)
}
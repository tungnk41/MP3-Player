package com.example.mediaservice.database.dao

import androidx.room.*
import com.example.mediaservice.repository.models.Playlist
import com.example.mediaservice.repository.models.entity.Favorite

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite WHERE userId = :userId AND deviceId = :deviceId")
    suspend fun findAll(userId: Long, deviceId: Long) : List<Favorite>

    @Query("SELECT * FROM favorite WHERE userId = :userId AND deviceId = :deviceId AND songId = :songId")
    suspend fun findBySongId(userId: Long, deviceId: Long, songId: Long) : Favorite

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: Favorite) : Long

    @Update
    suspend fun update(favorite: Favorite)
}
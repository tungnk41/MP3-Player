package com.example.mediaservice.database.dao

import androidx.room.*
import com.example.mediaservice.repository.models.Playlist
import com.example.mediaservice.repository.models.entity.Favorite

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite WHERE userId = :userId")
    suspend fun findAllByUserId(userId: Long) : List<Favorite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: Favorite) : Long

    @Update
    suspend fun update(favorite: Favorite)
}
package com.example.mediaservice.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.mediaservice.database.entity.Favorite

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite WHERE userId = :userId")
    suspend fun findAllFavoriteByUserId(userId: Long) : List<Favorite>
}
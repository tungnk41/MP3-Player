package com.example.mediaservice.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mediaservice.repository.models.entity.SongPlaylist

@Dao
interface SongPlaylistDao {

    @Query("SELECT * FROM song_playlist WHERE playlistId = :playListId")
    suspend fun findAllByPlaylistId(playListId: Long) : List<SongPlaylist>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(song: SongPlaylist) : Long
}
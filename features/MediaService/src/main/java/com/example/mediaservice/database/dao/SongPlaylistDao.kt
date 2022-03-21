package com.example.mediaservice.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.mediaservice.repository.models.entity.SongPlaylist

@Dao
interface SongPlaylistDao {

    @Query("SELECT * FROM song_playlist WHERE playlistId = :playListId")
    suspend fun findAllByPlaylistId(playListId: Long) : List<SongPlaylist>
}
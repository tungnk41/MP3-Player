package com.example.mediaservice.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.mediaservice.database.entity.SongPlaylist
import com.example.mediaservice.repository.models.Song

@Dao
interface SongPlaylistDao {

    @Query("SELECT * FROM song_playlist WHERE playlistId = :playListId")
    suspend fun findAllSongPlaylistByPlaylistId(playListId: Long) : List<SongPlaylist>
}
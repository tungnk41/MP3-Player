package com.example.mediaservice.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mediaservice.database.dao.FavoriteDao
import com.example.mediaservice.database.dao.PlayListDao
import com.example.mediaservice.database.dao.SongPlaylistDao
import com.example.mediaservice.repository.models.entity.Favorite
import com.example.mediaservice.repository.models.entity.SongPlaylist
import com.example.mediaservice.repository.models.Playlist

@Database(entities = [Playlist::class, SongPlaylist::class, Favorite::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun playListDao() : PlayListDao
    abstract fun songPlaylistDao() : SongPlaylistDao
    abstract fun favoriteDao() : FavoriteDao
}
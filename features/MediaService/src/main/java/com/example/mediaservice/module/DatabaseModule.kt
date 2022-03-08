package com.example.mediaservice.module

import android.content.Context
import androidx.room.Room
import com.example.mediaservice.const.DATABASE_NAME
import com.example.mediaservice.database.Database
import com.example.mediaservice.database.dao.PlayListDao
import com.example.mediaservice.database.dao.SongPlaylistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun providePlaylistDao(database: Database) : PlayListDao {
        return database.playListDao()
    }

    @Provides
    fun provideSongPlaylistDao(database: Database) : SongPlaylistDao {
        return database.songPlaylistDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            DATABASE_NAME
        ).build()
    }
}
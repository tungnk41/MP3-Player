package com.example.mediaplayerservice.module

import android.content.ComponentName
import android.content.Context
import com.example.mediaplayerservice.MediaPlayerService
import com.example.mediaplayerservice.MediaServiceConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ServiceConnectorModule {

    @Provides
    fun provideMediaServiceConnection(@ApplicationContext context : Context): MediaServiceConnection =
        MediaServiceConnection(context, ComponentName(context, MediaPlayerService::class.java))
}
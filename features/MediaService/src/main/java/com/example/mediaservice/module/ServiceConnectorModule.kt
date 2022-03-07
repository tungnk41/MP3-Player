package com.example.mediaservice.module

import android.content.ComponentName
import android.content.Context
import com.example.mediaservice.MediaPlayerService
import com.example.mediaservice.MediaServiceConnection
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
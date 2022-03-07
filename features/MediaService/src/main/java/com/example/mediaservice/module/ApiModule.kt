package com.example.musicplayer.di

import com.example.mediaplayerservice.network.MediaApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {


    /***************************************************************************/

    @Provides
    @Singleton
    fun provideMediaApi(retrofit: Retrofit): MediaApiInterface =
        retrofit.create(MediaApiInterface::class.java)
}
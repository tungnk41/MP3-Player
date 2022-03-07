package com.example.mediaservice.player

import android.content.Context
import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import com.example.mediaservice.extensions.mediaUri
import com.example.mediaservice.extensions.toExoPlayerMediaItem
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheWriter
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

class MediaPlayer(private val context : Context) {
    private lateinit var currentPlayer: ExoPlayer
    private lateinit var playListMediaSource: List<MediaSource>
    private lateinit var playListMediaMetadataCompat: List<MediaMetadataCompat>
    private var currentPlayingIndex: Int = 0
    private var playerStateListener : Player.Listener? = null
    private val databaseProvider = StandaloneDatabaseProvider(context)

    private val cache = SimpleCache(
        File(context.cacheDir, "CacheFolder"),
        LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024),
        databaseProvider
    )

    private val dataSourceFactory = DefaultDataSource.Factory(context)
    private var cacheDataSourceFactory = CacheDataSource.Factory()
        .setUpstreamDataSourceFactory(dataSourceFactory)
        .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        .setCache(cache)

    init {
        initExoPlayer()
    }


    fun setPlayerStateListener(listener : Player.Listener) {
        playerStateListener = listener
        playerStateListener?.let { currentPlayer.addListener(it) }

    }

    fun setPlayList(list : List<MediaMetadataCompat>) {
        playListMediaMetadataCompat = list
        playListMediaSource = list.map { mediaMetadataCompat ->
            if(mediaMetadataCompat.mediaUri?.startsWith("content://") == true) {
                ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaMetadataCompat.toExoPlayerMediaItem())
            }
            else {
                ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                    .createMediaSource(mediaMetadataCompat.toExoPlayerMediaItem())
            }
        }
        currentPlayer.setMediaSources(playListMediaSource)
    }

    fun playListMediaSource() = playListMediaSource

    fun playListMediaMetadataCompat() = playListMediaMetadataCompat

    fun playAtIndex(index : Int) {
        currentPlayer.prepare()
        currentPlayer.play()
    }

    fun getExoPlayerInstance() : Player {
        return currentPlayer
    }

    fun getDuration() : Long {
        return currentPlayer.duration
    }

    fun play() {
        currentPlayer.play()
    }
    fun pause() {
        currentPlayer.pause()
    }
    fun next() {
        currentPlayer.seekToNextMediaItem()
    }
    fun prev() {
        currentPlayer.seekToPreviousMediaItem()
    }

    fun release(){
        playerStateListener?.let { currentPlayer.removeListener(it) }
        currentPlayer.release()
        databaseProvider.close()

    }

    fun stop() {
        currentPlayer.stop()
    }

    fun currentPosition() : Long {
        return currentPlayer.currentPosition
    }

    fun isPlaying() : Boolean {
        return currentPlayer.isPlaying
    }

    private fun initExoPlayer() {
        val attributes = AudioAttributes.Builder()
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

        currentPlayer = ExoPlayer.Builder(context).build().apply {
            setAudioAttributes(attributes, true)
            setHandleAudioBecomingNoisy(true)
            setPauseAtEndOfMediaItems(false)
        }
    }

    fun playAtUrl(url : String) {
        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
        currentPlayer.setMediaSource(mediaSource)
        currentPlayer.playWhenReady = true
        currentPlayer.seekTo(0, 0)
        currentPlayer.prepare()
        currentPlayer.play()
    }

    fun prefetchRemoteData() {
        playListMediaSource.forEach {
            it.mediaItem.localConfiguration?.uri?.let { uri ->
                Log.d("TAG", "prefetchRemoteData: $uri")
                if(!uri.toString().startsWith("content://")){
                    val dataSpec = DataSpec.Builder()
                        .setUri(uri)
                        .setPosition(0)
                        .setLength(500*1024) //cache 500kb from 0s
                        .build()
                    CacheWriter(
                        cacheDataSourceFactory.createDataSource(),
                        dataSpec,
                        null,
                        CacheWriter.ProgressListener { requestLength, bytesCached, newBytesCached ->
                            val downloadPercentage = (bytesCached * 100.0 / requestLength)
                            Log.d("TAG", "prefetchRemoteData: $downloadPercentage")
                        }
                    ).cache()
                }
            }
        }
    }
}
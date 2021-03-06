package com.example.mediaservice.player

import android.content.Context
import android.net.Uri
import com.example.mediaservice.repository.models.Song
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
import timber.log.Timber
import timber.log.Timber.d
import java.io.File

class MediaPlayer(private val context : Context) {
    private lateinit var currentPlayer: ExoPlayer
    private var listMediaSource: List<MediaSource>? = null
    private var listSong: MutableList<Song> = mutableListOf()
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

    fun setPlayList(list : List<Song>) {
        listSong.clear()
        listSong.addAll(list)
        listMediaSource = list.map { mediaMetadataCompat ->
            if(mediaMetadataCompat.mediaUri?.startsWith("content://")) {
                ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaMetadataCompat.toExoPlayerMediaItem())
            }
            else {
                ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                    .createMediaSource(mediaMetadataCompat.toExoPlayerMediaItem())
            }
        }
        currentPlayer.setMediaSources(listMediaSource!!)
        currentPlayer.prepare()
    }

    fun listSong() = listSong.toList()

    fun playAtIndex(index : Int) {
        currentPlayer.seekTo(index,0)
        currentPlayer.playWhenReady = true
    }

    fun getExoPlayerInstance() : Player {
        return currentPlayer
    }

    fun getDuration() : Long {
        return currentPlayer.duration
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

    fun isPlaying() : Boolean {
        return currentPlayer.isPlaying
    }

    fun currentPosition() : Long {
        return currentPlayer.currentPosition
    }

    fun currentIndex(): Int{
        return currentPlayer.currentMediaItemIndex
    }

    fun currentItem() : Song?{
        if(currentIndex()>=0 && currentIndex() < listSong.size) {
            return listSong[currentIndex()]
        }
        return null
    }

    fun updateCurrentItem(song: Song) {
        if(listSong.size > 0 && currentIndex() < listSong.size) {
            listSong[currentIndex()] = song
        }
    }

    private fun initExoPlayer() {
        val attributes = AudioAttributes.Builder()
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

        currentPlayer = ExoPlayer.Builder(context).build().apply {
            setAudioAttributes(attributes, true)
            setHandleAudioBecomingNoisy(true)
            pauseAtEndOfMediaItems = false
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
        listMediaSource?.forEach {
            it.mediaItem.localConfiguration?.uri?.let { uri ->
                Timber.d("prefetchRemoteData: $uri")
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
                            d("prefetchRemoteData: $downloadPercentage")
                        }
                    ).cache()
                }
            }
        }
    }
}
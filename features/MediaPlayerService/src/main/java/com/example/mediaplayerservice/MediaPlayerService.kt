package com.example.mediaplayerservice

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.example.mediaplayerservice.const.*
import com.example.mediaplayerservice.extensions.toBrowserMediaItem
import com.example.mediaplayerservice.media.MediaPlayer
import com.example.mediaplayerservice.notification.MediaNotification
import com.example.musicplayer.repository.AlbumRepository.AlbumRepository
import com.example.musicplayer.repository.ArtistRepository.ArtistRepository
import com.example.musicplayer.repository.GenreRepository.GenreRepository
import com.example.musicplayer.repository.SongRepository.SongRepository
import com.example.musicplayer.repository.models.MediaIdExtra
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class MediaPlayerService : MediaBrowserServiceCompat() {
    companion object {
        val TAG = "MediaPlayerService"
    }

    private lateinit var currentPlayer: MediaPlayer
    private lateinit var mediaSession: MediaSessionCompat
    private var mediaItems: List<MediaBrowserCompat.MediaItem> = listOf()
    private var isForegroundService = false
    private lateinit var mediaNotification: MediaNotification
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)

    @Inject
    lateinit var songRepository: SongRepository
    @Inject
    lateinit var albumRepository: AlbumRepository
    @Inject
    lateinit var artistRepository: ArtistRepository
    @Inject
    lateinit var genreRepository: GenreRepository

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
        currentPlayer = MediaPlayer(this)
        currentPlayer.setPlayerStateListener(playerStateListener)
        initMediaSession()

        mediaNotification = MediaNotification(
            this,
            mediaSession.sessionToken,
            PlayerNotificationListener()
        )

        // ExoPlayer will manage the MediaSession for us.
        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlaybackPreparer(MediaPlaybackPreparer())
        mediaSessionConnector.setQueueNavigator(object : TimelineQueueNavigator(mediaSession) {
            override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
                val currentPlaylist = currentPlayer.playListMediaMetadataCompat()
                if (windowIndex < currentPlaylist.size) {
                    return currentPlaylist[windowIndex].description
                }
                return MediaDescriptionCompat.Builder().build()
            }
        })
        mediaSessionConnector.setPlayer(currentPlayer.getExoPlayerInstance())

        contentResolver.registerContentObserver(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            false,
            object : ContentObserver(null) {
                override fun onChange(selfChange: Boolean, uri: Uri?) {
                    Log.d(TAG, "onChange: " + uri)
                }
            })

//        serviceScope.launch {
//            while (true){
//                delay(1000)
//                Log.d(TAG, "onCreate: " + currentPlayer.getDuration())
//            }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return START_STICKY
        Log.d(TAG, "onStartCommand: action" + intent?.action)
        return START_STICKY
    }


    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        mediaSession.run {
            isActive = false
            release()
        }
        serviceJob.cancel()
        currentPlayer.release()
    }


    private fun reload(parentId: String) {
        this.notifyChildrenChanged(parentId)
    }

    private fun initMediaSession() {
        mediaSession = MediaSessionCompat(this, "MediaPlayerService").apply {
            val sessionIntent =
                application.packageManager.getLaunchIntentForPackage(application.packageName)
            val sessionActivityPendingIntent =
                PendingIntent.getActivity(application, 0, sessionIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            setSessionActivity(sessionActivityPendingIntent)
            isActive = true
        }
        sessionToken = mediaSession.sessionToken
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        if (clientPackageName == APP_PACKAGE_NAME) {
            return BrowserRoot(MediaIdExtra(TYPE_MEDIA_ROOT, null).toString(), null)
        }
        return null
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>,
    ) {
        val mediaIdExtra = MediaIdExtra().getDataFromString(parentId)
        val mediaParentId = mediaIdExtra.id
        val mediaParentType = mediaIdExtra.mediaType //TYPE_SONG,TYPE_ALBUM ...
        val mediaParentDataType = mediaIdExtra.dataType //LOCAL_DATA or REMOTE_DATA

        //allow calling result.sendResult from another thread
        result.detach()
        serviceScope.launch {
            try {
                if (mediaParentType == TYPE_MEDIA_ROOT) {
                    mediaItems = getRootMediaBrowserData()
                } else {
                    when (mediaParentType) {
                        TYPE_ALL_SONGS -> {
                            val listSong = songRepository.getAllSong(mediaParentDataType)
                            mediaItems = listSong.map { it.toBrowserMediaItem(TYPE_SONG,mediaParentDataType) }
                            withContext(Dispatchers.Main){
                                currentPlayer.setPlayList(listSong)
                            }
                        }
                        TYPE_ALL_ALBUMS -> {
                            mediaItems = albumRepository.getAllAlbum(mediaParentDataType).map { it.toBrowserMediaItem(TYPE_ALBUM,mediaParentDataType) }
                        }
                        TYPE_ALL_ARTISTS -> {
                            mediaItems = artistRepository.getAllArtist(mediaParentDataType).map {it.toBrowserMediaItem(TYPE_ARTIST,mediaParentDataType)}
                        }
                        TYPE_ALL_GENRES -> {
                            mediaItems = genreRepository.getAllGenre(mediaParentDataType).map {it.toBrowserMediaItem(TYPE_GENRE,mediaParentDataType)}
                        }
//                            TYPE_ALL_PLAYLISTS -> {
//                                 songRepository.getLocalListSong().map { it.toMediaMetaItem() }.toMutableList()
//                            }
//                            TYPE_SONG -> {
//                                if (mediaParentDataType == LOCAL_DATA) {
//
//                                } else {
//
//                                }
//                            }
                        TYPE_ALBUM -> {
                            mediaItems = songRepository.getAllSongFromAlbum(mediaParentId?.toLong() ?: -1, mediaParentDataType).map { it.toBrowserMediaItem(TYPE_SONG,mediaParentDataType) }
                        }
                        TYPE_ARTIST -> {
                            mediaItems = songRepository.getAllSongFromArtist(mediaParentId?.toLong() ?: -1, mediaParentDataType).map { it.toBrowserMediaItem(TYPE_SONG,mediaParentDataType) }
                        }
                        TYPE_GENRE -> {
                            mediaItems = songRepository.getAllSongFromGenre(mediaParentId?.toLong() ?: -1, mediaParentDataType).map { it.toBrowserMediaItem(TYPE_SONG,mediaParentDataType) }
                        }
//                            TYPE_PLAYLIST -> {
//                                 songRepository.getLocalListSong().map { it.toMediaMetaItem() }.toMutableList()
//                            }
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "onLoadChildren: $e")
            }
            result.sendResult(mediaItems.toMutableList())
        }
    }

    private fun getRootMediaBrowserData() : List<MediaBrowserCompat.MediaItem> {
        return listOf(
            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder().setTitle("Songs").build(),
                MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
            ),
            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder().setTitle("Albums").build(),
                MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
            ),
            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder().setTitle("Artists").build(),
                MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
            ),
            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder().setTitle("Genres").build(),
                MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
            ),
            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder().setTitle("Playlists").build(),
                MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
            ),
        )
    }
    /* Callback*/
    private inner class PlayerNotificationListener : PlayerNotificationManager.NotificationListener {
        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            if (ongoing && !isForegroundService) {
                ContextCompat.startForegroundService(
                    applicationContext,
                    Intent(applicationContext, this@MediaPlayerService.javaClass)
                )

                startForeground(notificationId, notification)
                isForegroundService = true
            }
        }

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            stopForeground(true)
            isForegroundService = false
            stopSelf()
        }
    }

    private inner class MediaPlaybackPreparer : MediaSessionConnector.PlaybackPreparer {

        override fun getSupportedPrepareActions(): Long =
            PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH or
                    PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH

        override fun onPrepare(playWhenReady: Boolean) {
//            return onPrepareFromMediaId(
//                recentSong.mediaId!!,
//                playWhenReady,
//                recentSong.description.extras
//            )
        }

        override fun onPrepareFromMediaId(
            mediaId: String,
            playWhenReady: Boolean,
            extras: Bundle?
        ) {
            Log.d(TAG, "onPrepareFromMediaId: ")
            currentPlayer.playAtIndex(2)
//            mediaSource.whenReady {
//                val itemToPlay: MediaMetadataCompat? = mediaSource.find { item ->
//                    item.id == mediaId
//                }
//                if (itemToPlay == null) {
//                    Log.w(TAG, "Content not found: MediaID=$mediaId")
//
//                } else {
//
//                    val playbackStartPositionMs =
//                        extras?.getLong(MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS, C.TIME_UNSET)
//                            ?: C.TIME_UNSET
//
//                    preparePlaylist(
//                        buildPlaylist(itemToPlay),
//                        itemToPlay,
//                        playWhenReady,
//                        playbackStartPositionMs
//                    )
//                }
//            }
        }


        override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) {
//            mediaSource.whenReady {
//                val metadataList = mediaSource.search(query, extras ?: Bundle.EMPTY)
//                if (metadataList.isNotEmpty()) {
//                    preparePlaylist(
//                        metadataList,
//                        metadataList[0],
//                        playWhenReady,
//                        playbackStartPositionMs = C.TIME_UNSET
//                    )
//                }
//            }
        }

        override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) = Unit

        override fun onCommand(
            player: Player,
            command: String,
            extras: Bundle?,
            cb: ResultReceiver?
        ) = false

    }

    private val playerStateListener = object : Player.Listener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE -> {

                }
                Player.STATE_BUFFERING -> {

                }
                Player.STATE_READY -> {
                    mediaNotification.showNotificationForPlayer(currentPlayer.getExoPlayerInstance())

                    if(!playWhenReady) {
                        stopForeground(false)
                        isForegroundService = false
                    }
                }
                Player.STATE_ENDED -> {
//
                }
                else -> {
                    mediaNotification.hideNotification()
                }
            }
        }
    }
}
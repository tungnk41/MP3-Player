package com.example.mediaservice

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
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.example.mediaservice.extensions.*
import com.example.mediaservice.utils.*
import com.example.mediaservice.notification.MediaNotification
import com.example.mediaservice.player.MediaPlayer
import com.example.mediaservice.repository.AlbumRepository.AlbumRepository
import com.example.mediaservice.repository.ArtistRepository.ArtistRepository
import com.example.mediaservice.repository.FavoriteRepository.FavoriteRepository
import com.example.mediaservice.repository.GenreRepository.GenreRepository
import com.example.mediaservice.repository.PlaylistRepository.PlaylistRepository
import com.example.mediaservice.repository.SongRepository.SongRepository
import com.example.mediaservice.repository.models.MediaIdExtra
import com.example.mediaservice.repository.models.Playlist
import com.example.mediaservice.repository.models.entity.Favorite
import com.example.mediaservice.session.UserSessionInfo
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber.d
import javax.inject.Inject

@AndroidEntryPoint
class MediaPlayerService : MediaBrowserServiceCompat() {

    private lateinit var currentPlayer: MediaPlayer
    private lateinit var mediaSession: MediaSessionCompat
    private var mediaItems: List<MediaBrowserCompat.MediaItem> = listOf()
    private var isForegroundService = false
    private lateinit var mediaNotification: MediaNotification
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private val serviceJob = SupervisorJob()
    private val serviceExceptionHandler = CoroutineExceptionHandler{ _, _ ->
        d("Service exception")
    }
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob + serviceExceptionHandler)

    private var cachedlocalListSong: List<MediaMetadataCompat> = listOf()
    private var cachedremoteListSong: List<MediaMetadataCompat> = listOf()
    private var cachedplaylistListSong: List<MediaMetadataCompat> = listOf()
    private var currentFocusListSong: List<MediaMetadataCompat> = listOf()
    private var isNeedToApplyNewListSong = false
    private var currenParentMediaIdExtra: MediaIdExtra? = null


    @Inject
    lateinit var songRepository: SongRepository
    @Inject
    lateinit var albumRepository: AlbumRepository
    @Inject
    lateinit var artistRepository: ArtistRepository
    @Inject
    lateinit var genreRepository: GenreRepository
    @Inject
    lateinit var playlistRepository: PlaylistRepository
    @Inject
    lateinit var favoriteRepository: FavoriteRepository
    @Inject
    lateinit var userSessionInfo: UserSessionInfo

    override fun onCreate() {
        super.onCreate()
        d( "onCreate: ")
        currentPlayer = MediaPlayer(this)
        currentPlayer.setPlayerStateListener(playerStateListener)
        userSessionInfo.create(userId = 1, deviceId = 1)
        initMediaSession()

        mediaNotification = MediaNotification(
            this,
            mediaSession.sessionToken,
            PlayerNotificationListener()
        )

        // ExoPlayer will manage the MediaSession for us.
        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlaybackPreparer(MediaPlaybackPreparer())
        mediaSessionConnector.setMediaMetadataProvider(object : MediaSessionConnector.MediaMetadataProvider{
            override fun getMetadata(player: Player): MediaMetadataCompat {
                val duration = currentPlayer.getDuration()
                val builder = MediaMetadataCompat.Builder(currentPlayer.currentItem())
                if(duration > 0) {
                    builder.duration = duration
                }
                return builder.build()
            }
        })
        mediaSessionConnector.setQueueNavigator(object : TimelineQueueNavigator(mediaSession) {
            override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
                val currentPlaylist = currentPlayer.playListMediaMetadataCompat()
                if (currentPlaylist != null && windowIndex < currentPlaylist.size) {
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
                    d( "onChange: " + uri)
                }
            })
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return START_STICKY
        d("onStartCommand: action" + intent?.action)
        return START_NOT_STICKY
    }


    override fun onDestroy() {
        d( "onDestroy: ")
        mediaSession.run {
            isActive = false
            release()
        }
        serviceJob.cancel()
        currentPlayer.release()
    }


    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        d( "onTaskRemoved: ")
        if(!currentPlayer.isPlaying()) {
            currentPlayer.stop()
        }
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
        if (clientPackageName == CLIENT_PACKAGE_NAME) {
            return BrowserRoot(MediaIdExtra(mediaType = MediaType.TYPE_MEDIA_ROOT).toString(), null)
        }
        return null
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>,
    ) {
        val mediaIdExtra = MediaIdExtra.getDataFromString(parentId)
        val parentId = mediaIdExtra.id
        val parentMediaType = mediaIdExtra.mediaType //MediaType.TYPE_SONG,MediaType.TYPE_ALBUM ...
        val parentDataSource = mediaIdExtra.dataSource //DataSource.LOCAL or REMOTE

        currenParentMediaIdExtra = mediaIdExtra
        mediaItems = listOf()
        //allow calling result.sendResult from another thread
        result.detach()
        serviceScope.launch {
            try {
                if (parentMediaType == MediaType.TYPE_MEDIA_ROOT) {
                    mediaItems = getRootMediaBrowserData()
                } else {
                    when (parentMediaType) {
                        MediaType.TYPE_ALL_SONGS -> {
                            val listSong = songRepository.findAll(parentDataSource)
                            mediaItems = listSong.map { it.toBrowserMediaItem() }
                            cacheListSong(parentMediaType,parentDataSource,listSong)
                        }
                        MediaType.TYPE_ALL_ALBUMS -> {
                            mediaItems = albumRepository.findAll(parentDataSource).map { it.toBrowserMediaItem() }
                        }
                        MediaType.TYPE_ALL_ARTISTS -> {
                            mediaItems = artistRepository.findAll(parentDataSource).map {it.toBrowserMediaItem()}
                        }
                        MediaType.TYPE_ALL_GENRES -> {
                            mediaItems = genreRepository.findAll(parentDataSource).map {it.toBrowserMediaItem()}
                        }
                        MediaType.TYPE_ALL_PLAYLISTS -> {
                            mediaItems = playlistRepository.findAll(userSessionInfo.userId).map { it.toBrowserMediaItem() }
                        }
                        MediaType.TYPE_ALBUM -> {
                            val listSong = songRepository.findAllByAlbumId(parentId ?: -1, parentDataSource)
                            mediaItems = listSong.map { it.toBrowserMediaItem() }
                            cacheListSong(parentMediaType,parentDataSource,listSong)
                        }
                        MediaType.TYPE_ARTIST -> {
                            val listSong = songRepository.findAllByArtistId(parentId ?: -1, parentDataSource)
                            mediaItems = listSong.map { it.toBrowserMediaItem() }
                            cacheListSong(parentMediaType,parentDataSource,listSong)
                        }
                        MediaType.TYPE_GENRE -> {
                            val listSong = songRepository.findAllByGenreId(parentId ?: -1, parentDataSource)
                            mediaItems = listSong.map { it.toBrowserMediaItem() }
                            cacheListSong(parentMediaType,parentDataSource,listSong)
                        }
                        MediaType.TYPE_PLAYLIST -> {
                            val listSong = songRepository.findAllByPlaylistId(parentId ?: -1, parentDataSource)
                            mediaItems = listSong.map { it.toBrowserMediaItem(parentMediaType) }
                            cacheListSong(parentMediaType,parentDataSource,listSong)
                        }
                    }
                }
            } catch (e: Exception) {
                d( "onLoadChildren: $e")
            }
            result.sendResult(mediaItems.toMutableList())
        }
    }

    private fun getRootMediaBrowserData() : List<MediaBrowserCompat.MediaItem> {
        return listOf(
            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder()
                    .setMediaId(MediaIdExtra(mediaType = MediaType.TYPE_ALL_SONGS, dataSource = DataSource.LOCAL).toString())
                    .setTitle("Songs").build(),
                MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
            ),
            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder()
                    .setMediaId(MediaIdExtra(mediaType = MediaType.TYPE_ALL_ALBUMS, dataSource = DataSource.LOCAL).toString())
                    .setTitle("Albums").build(),
                MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
            ),
            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder()
                    .setMediaId(MediaIdExtra(mediaType = MediaType.TYPE_ALL_ARTISTS, dataSource = DataSource.LOCAL).toString())
                    .setTitle("Artists").build(),
                MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
            ),
            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder()
                    .setMediaId(MediaIdExtra(mediaType = MediaType.TYPE_ALL_GENRES, dataSource = DataSource.LOCAL).toString())
                    .setTitle("Genres").build(),
                MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
            ),
            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder()
                    .setMediaId(MediaIdExtra(mediaType = MediaType.TYPE_ALL_PLAYLISTS, dataSource = DataSource.NONE).toString())
                    .setTitle("Playlists").build(),
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
                    Intent(applicationContext, MediaPlayerService::class.java)
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
                            PlaybackStateCompat.ACTION_PREPARE or
                            PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                            PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH or
                            PlaybackStateCompat.ACTION_PREPARE_FROM_URI or
                            PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                            PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH or
                            PlaybackStateCompat.ACTION_PLAY_FROM_URI

        override fun onPrepare(playWhenReady: Boolean) {
            d( "onPrepare: ")
        }

        override fun onPrepareFromMediaId(
            mediaId: String,
            playWhenReady: Boolean,
            extras: Bundle?
        ) {
            serviceScope.launch {
                val mediaIdExtra = MediaIdExtra.getDataFromString(mediaId)
                val id = mediaIdExtra.id
                val parentMediaType = mediaIdExtra.parentMediaType ?: -1
                val mediaType = mediaIdExtra.mediaType
                val dataSource = mediaIdExtra.dataSource
                val index = extras?.getInt("index")

                var listSongToPlay = getListSongToPlay(parentMediaType,dataSource)
                withContext(Dispatchers.Main) {
                    if(listSongToPlay == currentFocusListSong) {
                        if(isNeedToApplyNewListSong) {
                            currentPlayer.setPlayList(currentFocusListSong)
                            isNeedToApplyNewListSong = false
                        }
                        index?.let {
                            currentPlayer.playAtIndex(index)
                        }
                    }else {
                        currentFocusListSong = listSongToPlay
                        currentPlayer.setPlayList(currentFocusListSong)
                        index?.let {
                            currentPlayer.playAtIndex(index)
                        }
                    }
                }
            }
        }

        override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) {
            d( "onPrepareFromSearch: " + query)
        }

        override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) = Unit

        override fun onCommand(player: Player, command: String, extras: Bundle?, cb: ResultReceiver?): Boolean {
            when(command) {
                CMD_ADD_PLAYLIST -> {
                    val playlist = extras?.getParcelable<Playlist>(KEY_PLAYLIST)
                    serviceScope.launch {
                        playlist?.let {
                            playlistRepository.insert(it)
                        }
                    }
                }
                CMD_CLICK_FAVORITE -> {
                    val favorite = if(currentPlayer.currentItem().favorite == 1L) 0 else 1
                    val mediaIdExtra = MediaIdExtra.getDataFromString(currentPlayer.currentItem().id ?: "")
                    val songId = mediaIdExtra.id ?: -1
                    val dataSource = mediaIdExtra.dataSource
                    val builder = MediaMetadataCompat.Builder(currentPlayer.currentItem())
                    builder.favorite = favorite
                    currentPlayer.updateCurrentItem(builder.build())
                    serviceScope.launch {
                        favoriteRepository.insert(Favorite(userId = userSessionInfo.userId, deviceId = userSessionInfo.deviceId, songId = songId, value = favorite), dataSource = dataSource)
                    }
                }
            }
            return true
        }
    }

    private val playerStateListener = object : Player.Listener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            d( "onPlayerStateChanged: $playbackState + $playWhenReady")
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
                    
                }
                else -> {
                    mediaNotification.hideNotification()
                }
            }
        }
    }

    private fun cacheListSong(parentMediaType: Int, parentDataSource: Int, listSong: List<MediaMetadataCompat>){
        if(parentMediaType == MediaType.TYPE_PLAYLIST) {
            cachedplaylistListSong = listSong
        }
        else {
            if(parentDataSource == DataSource.LOCAL) {
                cachedlocalListSong = listSong
            }
            else {
                cachedremoteListSong = listSong
            }
        }
        currentFocusListSong = listSong
        isNeedToApplyNewListSong = true
    }

    private fun getListSongToPlay(parentMediaType: Int, parentDataSource: Int) : List<MediaMetadataCompat> {
        var listSong = listOf<MediaMetadataCompat>()

        if(parentMediaType == MediaType.TYPE_PLAYLIST) {
            listSong = cachedplaylistListSong
        }
        else {
            if(parentDataSource == DataSource.LOCAL) {
                listSong = cachedlocalListSong
            }
            else {
                listSong = cachedremoteListSong
            }
        }
        return listSong
    }
}
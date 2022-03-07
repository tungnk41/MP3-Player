package com.example.mediaplayerservice.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.mediaplayerservice.R
import com.example.mediaplayerservice.const.NOTIFICATION_CHANNEL_ID
import com.example.mediaplayerservice.const.NOTIFICATION_ID
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import kotlinx.coroutines.*
import java.lang.Exception

class MediaNotification (private val context: Context, sessionToken: MediaSessionCompat.Token, notificationListener: PlayerNotificationManager.NotificationListener) {

    private var player: Player? = null
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private var notificationManager: PlayerNotificationManager
    private val platformNotificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)

        val builder = PlayerNotificationManager.Builder(context, NOTIFICATION_ID, NOTIFICATION_CHANNEL_ID)
        with (builder) {
            setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
            setNotificationListener(notificationListener)
            setChannelNameResourceId(0)
            setChannelDescriptionResourceId(0)
        }
        notificationManager = builder.build()
        notificationManager.setMediaSessionToken(sessionToken)
        notificationManager.setSmallIcon(R.drawable.ic_launcher_foreground)
        notificationManager.setUsePreviousActionInCompactView(true)
        notificationManager.setUseNextActionInCompactView(true)
        notificationManager.setUseFastForwardAction(false)
        notificationManager.setUseRewindAction(false)
    }

    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    fun showNotificationForPlayer(player: Player){
        notificationManager.setPlayer(player)
    }

    private inner class DescriptionAdapter(private val controller: MediaControllerCompat) : PlayerNotificationManager.MediaDescriptionAdapter {

        var currentIconUri: Uri? = null
        var currentBitmap: Bitmap? = null

        override fun createCurrentContentIntent(player: Player): PendingIntent? =
            controller.sessionActivity

        override fun getCurrentContentTitle(player: Player) =
            controller.metadata.description.title.toString()

        override fun getCurrentContentText(player: Player) =
            controller.metadata.description.subtitle.toString()

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            val iconUri = controller.metadata.description.iconUri
            return if (currentIconUri != iconUri || currentBitmap == null) {

                // Cache the bitmap for the current song so that successive calls to
                // `getCurrentLargeIcon` don't cause the bitmap to be recreated.
                currentIconUri = iconUri
                serviceScope.launch {
                    currentBitmap = iconUri?.let {
                        try {
                            resolveUriAsBitmap(it)
                        }catch (e: Exception) {
                            BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_default_art_24)
                        }

                    }
                    currentBitmap?.let { callback.onBitmap(it) }
                }
                null
            } else {
                currentBitmap
            }
        }

        private suspend fun resolveUriAsBitmap(uri: Uri): Bitmap? {
            return withContext(Dispatchers.IO) {
                Glide.with(context)
                    .asBitmap()
                    .load(uri)
                    .override(NOTIFICATION_LARGE_ICON_SIZE,NOTIFICATION_LARGE_ICON_SIZE)
                    .submit()
                    .get()
            }
        }

    }
}

const val NOTIFICATION_LARGE_ICON_SIZE = 144
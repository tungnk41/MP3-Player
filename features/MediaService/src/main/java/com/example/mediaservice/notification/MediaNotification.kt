package com.example.mediaservice.notification

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.mediaservice.R
import com.example.mediaservice.utils.NOTIFICATION_CHANNEL_ID
import com.example.mediaservice.utils.NOTIFICATION_ID
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import kotlinx.coroutines.*

class MediaNotification(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener
) {

    private val notiJob = SupervisorJob()
    private val notiScope = CoroutineScope(Dispatchers.Main + notiJob)
    private var notificationManager: PlayerNotificationManager
    private lateinit var artworkDefault: Bitmap
    init {
        val mediaController = MediaControllerCompat(context, sessionToken)

        val builder =
            PlayerNotificationManager.Builder(context, NOTIFICATION_ID, NOTIFICATION_CHANNEL_ID)
        with(builder) {
            setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
            setNotificationListener(notificationListener)
            setChannelNameResourceId(R.string.notification_channel_name)
            setChannelDescriptionResourceId(R.string.notification_channel_desc)
        }
        notificationManager = builder.build()
        notificationManager.setMediaSessionToken(sessionToken)
        notificationManager.setSmallIcon(R.drawable.ic_default_art_24)
        notificationManager.setUsePreviousActionInCompactView(true)
        notificationManager.setUseNextActionInCompactView(true)
        notificationManager.setUseFastForwardAction(false)
        notificationManager.setUseRewindAction(false)

        notiScope.launch(Dispatchers.Default) {
           artworkDefault = BitmapFactory.decodeResource(context.resources,R.drawable.default_album_art)
        }
    }


    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    fun showNotificationForPlayer(player: Player) {
        notificationManager.setPlayer(player)
    }

    private inner class DescriptionAdapter(private val controller: MediaControllerCompat) :
        PlayerNotificationManager.MediaDescriptionAdapter {

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
            loadBitmap(controller.metadata.description.iconUri!!, callback)
            return artworkDefault
        }

        private fun loadBitmap(url: Uri, callback: PlayerNotificationManager.BitmapCallback?) {
            Glide.with(context)
                .asBitmap()
                .load(url)
                .skipMemoryCache(false)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        callback?.onBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
        }
    }
}

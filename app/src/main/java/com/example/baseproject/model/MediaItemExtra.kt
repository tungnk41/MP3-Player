package com.example.baseproject.model

import android.net.Uri
import com.example.mediaservice.repository.models.MediaIdExtra
import com.example.mediaservice.utils.DataSource
import com.example.mediaservice.utils.MediaType

data class MediaItemUI(
    val mediaIdExtra: MediaIdExtra, // Keep mediaIdExtra as String
    val id: Long,
    val title: String,
    val subTitle: String,
    val iconUri: Uri,
    val isBrowsable: Boolean,
    val dataSource: Int,
    val mediaType: Int
)
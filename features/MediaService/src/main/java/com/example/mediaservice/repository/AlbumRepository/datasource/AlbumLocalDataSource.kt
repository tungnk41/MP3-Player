package com.example.mediaservice.repository.AlbumRepository.datasource

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.mediaservice.repository.AlbumRepository.AlbumDataSource
import com.example.mediaservice.repository.models.Album
import com.example.mediaservice.utils.DataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.HashSet
import javax.inject.Inject

class AlbumLocalDataSource @Inject constructor(@ApplicationContext val context: Context) :
    AlbumDataSource {
    override suspend fun findAll(): List<Album> = withContext(Dispatchers.IO){
        val listAlbum = mutableListOf<Album>()
        val contentResolver: ContentResolver = context.contentResolver
        val contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val setIdAlbum = hashSetOf<Long>()
        //Column Query
        val projection = arrayOf(
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM
        )

        //Condition
        val cursor: Cursor? = contentResolver.query(contentUri, projection, null, null,null)

        cursor?.let {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    var imageUri = ""
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val albumArtUri = Uri.parse("content://media/external/audio/albumart")
                        imageUri = ContentUris.withAppendedId(albumArtUri,id).toString()
                    } else {
                        imageUri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART))
                    }
                    if(!setIdAlbum.contains(id)) {
                        setIdAlbum.add(id)
                        listAlbum.add(Album(id = id,title = name, iconUri = imageUri, dataSource = DataSource.LOCAL))
                    }
                } while (cursor.moveToNext())
            }
            if (cursor != null) {
                cursor.close()
            }
        }
        listAlbum
    }
}
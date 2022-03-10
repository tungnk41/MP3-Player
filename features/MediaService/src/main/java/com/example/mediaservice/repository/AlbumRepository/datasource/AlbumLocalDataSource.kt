package com.example.mediaservice.repository.AlbumRepository.datasource

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.example.mediaservice.repository.AlbumRepository.AlbumDataSource
import com.example.mediaservice.repository.models.Album
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumLocalDataSource @Inject constructor(@ApplicationContext val context: Context) :
    AlbumDataSource {
    override suspend fun findAll(): List<Album> = withContext(Dispatchers.IO){
        val listAlbum = mutableListOf<Album>()
        val contentResolver: ContentResolver = context.contentResolver
        val contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        //Column Query
        val projection = arrayOf(
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM,
        )
        //Condition
        val cursor: Cursor? = contentResolver.query(contentUri, projection, null, null, null)

        cursor?.let {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    listAlbum.add(Album(id,name))
                } while (cursor.moveToNext())
            }
            if (cursor != null) {
                cursor.close()
            }
        }
        listAlbum
    }
}
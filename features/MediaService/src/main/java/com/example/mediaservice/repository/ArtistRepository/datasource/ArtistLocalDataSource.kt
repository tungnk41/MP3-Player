package com.example.mediaservice.repository.ArtistRepository.datasource

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.example.mediaservice.repository.ArtistRepository.ArtistDataSource
import com.example.mediaservice.repository.models.Artist
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ArtistLocalDataSource @Inject constructor(@ApplicationContext val context: Context) :
    ArtistDataSource {
    override suspend fun findAll(): List<Artist> {
        val listArtist = mutableListOf<Artist>()
        val contentResolver: ContentResolver = context.contentResolver
        val contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        //Column Query
        val projection = arrayOf(
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ARTIST,
        )
        //Condition
        val cursor: Cursor? = contentResolver.query(contentUri, projection, null, null, null)

        cursor?.let {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    listArtist.add(Artist(id,name))
                } while (cursor.moveToNext())
            }
            if (cursor != null) {
                cursor.close()
            }
        }
        return listArtist
    }
}
package com.example.mediaservice.repository.GenreRepository.datasource

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.example.mediaservice.repository.GenreRepository.GenreDataSource
import com.example.mediaservice.repository.models.Genre
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GenreLocalDataSource @Inject constructor(@ApplicationContext val context: Context) :
    GenreDataSource {
    override suspend fun findAll(): List<Genre> {
        val listGenre = mutableListOf<Genre>()

        val contentResolver: ContentResolver = context.contentResolver
        val contentUri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI
        //Column Query
        val projection = arrayOf(
            MediaStore.Audio.Genres._ID,
            MediaStore.Audio.Genres.NAME,
        )
        //Condition
        val cursor: Cursor? = contentResolver.query(contentUri, projection, null, null, null)

        cursor?.let {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME))
                    listGenre.add(Genre(id,name))
                } while (cursor.moveToNext())
            }
            if (cursor != null) {
                cursor.close()
            }
        }
        return listGenre
    }
}
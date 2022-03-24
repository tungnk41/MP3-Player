package com.example.mediaservice.repository.GenreRepository.datasource

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.example.mediaservice.repository.GenreRepository.GenreDataSource
import com.example.mediaservice.repository.models.Album
import com.example.mediaservice.repository.models.Genre
import com.example.mediaservice.utils.DataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber.d
import javax.inject.Inject

class GenreLocalDataSource @Inject constructor(@ApplicationContext val context: Context) : GenreDataSource {
    override suspend fun findAll(): List<Genre> = withContext(Dispatchers.IO){
        val listGenre = mutableListOf<Genre>()

        val contentResolver: ContentResolver = context.contentResolver
        val contentUri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI
        val setIdGenre = hashSetOf<Long>()
        //Column Query
        val projection = arrayOf(
            MediaStore.Audio.Genres._ID,
            MediaStore.Audio.Genres.NAME,
        )
        //Condition
        val cursor: Cursor? = contentResolver.query(contentUri, projection, null, null, MediaStore.Audio.Genres.DEFAULT_SORT_ORDER)
        cursor?.let {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME))
                    if(!setIdGenre.contains(id)) {
                        setIdGenre.add(id)
                        if (getSongCountForGenre(id) > 0){
                            listGenre.add(Genre(id = id, title = name, dataSource = DataSource.LOCAL))
                        }
                    }
                } while (cursor.moveToNext())
            }
            if (cursor != null) {
                cursor.close()
            }
        }
        listGenre
    }

    private suspend fun getSongCountForGenre(genreId: Long): Int = withContext(Dispatchers.IO){
        var result = 0
        val uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId)
        val contentResolver: ContentResolver = context.contentResolver
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        cursor?.let {
            if (cursor.moveToFirst()) {
                result = if(it.count <= 0) 0 else it.count
            }
            if (cursor != null) {
                cursor.close()
            }
        }
        result
    }
}
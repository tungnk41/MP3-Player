package com.example.musicplayer.repository.SongRepository.datasource

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import com.example.musicplayer.repository.SongRepository.SongDataSource
import com.example.musicplayer.repository.models.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongLocalDataSource @Inject constructor(@ApplicationContext val context: Context) : SongDataSource {


    override suspend fun getAllSong() : List<Song>  = withContext(Dispatchers.IO){
        val contentResolver: ContentResolver = context.contentResolver
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.DURATION,
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC}=1"
        val cursor: Cursor? = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null)

        getSongFromCursor(cursor)
    }

    override suspend fun getSongFromAlbum(albumId: Long): List<Song> = withContext(Dispatchers.IO){
        val contentResolver: ContentResolver = context.contentResolver

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.DURATION,

            )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC}=1"
        val cursor: Cursor? = contentResolver.query(MediaStore.Audio.Genres.Members.getContentUri("external",albumId), projection, selection, null, null)

        getSongFromCursor(cursor)
    }

    override suspend fun getSongFromArtist(artistId: Long): List<Song> = withContext(Dispatchers.IO){
        val contentResolver: ContentResolver = context.contentResolver

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.DURATION,

            )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC}=1"
        val cursor: Cursor? = contentResolver.query(MediaStore.Audio.Genres.Members.getContentUri("external",artistId), projection, selection, null, null)

        getSongFromCursor(cursor)
    }

    override suspend fun getSongFromGenre(genreId: Long): List<Song> = withContext(Dispatchers.IO){
        val contentResolver: ContentResolver = context.contentResolver

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.DURATION,

            )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC}=1"
        val cursor: Cursor? = contentResolver.query(MediaStore.Audio.Genres.Members.getContentUri("external",genreId), projection, selection, null, null)

        getSongFromCursor(cursor)
    }

    private suspend fun getSongFromCursor(cursor: Cursor?) : List<Song> {
        val listSong = mutableListOf<Song>()
        cursor?.let {
            if (cursor.moveToFirst()) {

                do {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    val songUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    val album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    val albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                    val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    val artistId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID))
                    val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    var image = ""
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val albumArtUri = Uri.parse("content://media/external/audio/albumart")
                        image = ContentUris.withAppendedId(albumArtUri,albumId).toString()
                    } else {
                        image = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART))
                    }
//              val albumArtBitmap = context.getContentResolver().loadThumbnail(imageUri, Size(100, 100), null)

                    val song = Song(
                        id = id,
                        title = title,
                        album = album,
                        albumId = albumId,
                        artist = artist,
                        artistId = artistId,
                        source = songUri.toString(),
                        image = image,
                        duration = duration
                    )
                    listSong.add(song)
                } while (cursor.moveToNext())
            }
            if (cursor != null) {
                cursor.close()
            }
        }
        return listSong
    }
}
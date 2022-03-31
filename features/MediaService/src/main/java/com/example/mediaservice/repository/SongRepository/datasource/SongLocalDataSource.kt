package com.example.mediaservice.repository.SongRepository.datasource

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.mediaservice.database.dao.SongPlaylistDao
import com.example.mediaservice.module.LocalDataSource
import com.example.mediaservice.repository.FavoriteRepository.FavoriteDataSource
import com.example.mediaservice.repository.FavoriteRepository.datasource.FavoriteLocalDataSource
import com.example.mediaservice.repository.SongRepository.SongDataSource
import com.example.mediaservice.repository.models.Song
import com.example.mediaservice.repository.models.entity.SongPlaylist
import com.example.mediaservice.utils.DataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber.d
import java.util.*
import javax.inject.Inject

class SongLocalDataSource @Inject constructor(@ApplicationContext val context: Context,@LocalDataSource private val favoriteDataSource: FavoriteDataSource,private val songPlaylistDao: SongPlaylistDao) :
    SongDataSource {

    override suspend fun findAll() : List<Song>  = withContext(Dispatchers.IO){
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
        val sortOrder = MediaStore.Audio.Media.TITLE
        val cursor: Cursor? = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, sortOrder)

        findAllByCursor(cursor)
    }

    override suspend fun findAllByAlbumId(albumId: Long): List<Song> = withContext(Dispatchers.IO){
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
        val selection = "${MediaStore.Audio.Media.IS_MUSIC}=1 AND ${MediaStore.Audio.Media.ALBUM_ID}=$albumId"
        val cursor: Cursor? = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null)

        findAllByCursor(cursor)
    }

    override suspend fun findAllByArtistId(artistId: Long): List<Song> = withContext(Dispatchers.IO){
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
        val selection = "${MediaStore.Audio.Media.IS_MUSIC}=1 AND ${MediaStore.Audio.Media.ARTIST_ID}=$artistId"
        val cursor: Cursor? = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null)

        findAllByCursor(cursor)
    }

    override suspend fun findAllByGenreId(genreId: Long): List<Song> = withContext(Dispatchers.IO){
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
        val cursor: Cursor? = contentResolver.query(MediaStore.Audio.Genres.Members.getContentUri("external",genreId), projection, selection, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER)

        findAllByCursor(cursor)
    }

    override suspend fun findAllByPlaylistId(playlistId: Long): List<Song> = withContext(Dispatchers.IO) {
        val setSongPlaylistId = songPlaylistDao.findAllByPlaylistId(playlistId).filter { !it.isRemoteData }.map{ it.songId }.toHashSet()
        val listSong = findAll().filter { setSongPlaylistId.contains(it.id) }
        listSong
    }

    override suspend fun saveSongToPlaylist(playlistId: Long, songId: Long) {
        songPlaylistDao.insert(SongPlaylist(playlistId = playlistId, songId = songId, isRemoteData = false))
    }

    override suspend fun searchSongByTitle(title: String): List<Song> = withContext(Dispatchers.IO) {
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
        val selection = "${MediaStore.Audio.Media.IS_MUSIC}=1 AND ${MediaStore.Audio.Media.TITLE} LIKE ?"
        val selectionArgs = arrayOf("%${title}%")
        val cursor: Cursor? = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null)

        findAllByCursor(cursor)
    }


    private suspend fun findAllByCursor(cursor: Cursor?) : List<Song> = withContext(Dispatchers.IO) {
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

                    val favorite = favoriteDataSource.findBySongId(id)

                    val song = Song(
                        id = id,
                        title = title,
                        album = album,
                        albumId = albumId,
                        artist = artist,
                        artistId = artistId,
                        mediaUri = songUri.toString(),
                        iconUri = image,
                        duration = duration,
                        favorite = favorite?.value ?: 0,
                        dataSource = DataSource.LOCAL
                    )
                    listSong.add(song)
                } while (cursor.moveToNext())
            }
            if (cursor != null) {
                cursor.close()
            }
        }
        listSong
    }
}
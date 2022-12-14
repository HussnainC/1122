package com.codecx.musicplayer.repo

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import com.codecx.musicplayer.musicdatabase.entities.SongModel
import com.codecx.musicplayer.utils.getInt
import com.codecx.musicplayer.utils.getLong
import com.codecx.musicplayer.utils.getString
import com.codecx.musicplayer.utils.FileUtils
import com.codecx.musicplayer.utils.ImageUtils

class SongsRepository(val context: Context) {


    @SuppressLint("Range")
    fun createSongFromCursor(cursor: Cursor): SongModel {
        val title = cursor.getString(AudioColumns.TITLE)
        val duration = cursor.getLong(AudioColumns.DURATION)
        val data = cursor.getString(AudioColumns.DATA)
        val id = cursor.getLong(AudioColumns._ID)
        val dateAdded = cursor.getString(AudioColumns.DATE_ADDED)
        val artist = cursor.getString(AudioColumns.ARTIST)
//        val trackNumber = cursor.getString(AudioColumns.TRACK)
        val year = cursor.getInt(AudioColumns.YEAR)
        val dateModified = cursor.getLong(AudioColumns.DATE_MODIFIED)
        val artistId = cursor.getLong(AudioColumns.ARTIST_ID)
        val artistName = cursor.getString(AudioColumns.ARTIST)
//        val composer = cursor.getString(AudioColumns.COMPOSER)
//        val albumArtist = cursor.getString(AudioColumns.ALBUM_ARTIST)
        val uri = ContentUris
            .withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID))
            )
        val albumId = cursor.getLong(AudioColumns.ALBUM_ID)
        val size = cursor.getString(AudioColumns.SIZE)

        val image = ImageUtils.albumArtUriToBitmap(context, albumId)
            ?: ImageUtils.getDefaultAlbumArt(context)

        var bitrate = ""
        if (data != "") {
            val metadata = MediaMetadataRetriever()
            metadata.setDataSource(data)
            bitrate = metadata?.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE).toString()

        }

        return SongModel(
            title = title,
            duration = duration,
            data = data,
            dateAdded = dateAdded,
            artist = artist,
            id = id,
            uri = uri,
            albumId = albumId,
            size = size,
            bitrate = bitrate,
            image = image,
            trackNumber = "",
            year = year,
            dateModified = dateModified,
            artistId = artistId,
            artistName = artistName,
            composer = "",
            albumArtist = ""
        )
    }

   suspend fun getListOfSongs(): ArrayList<SongModel> {
        return getSongsFromStorage()
    }

    private suspend fun getSongsFromStorage(): ArrayList<SongModel> {
        val songsAreInStorage = ArrayList<SongModel>()
        val cursor = FileUtils.fetchFiles(
            fileType = FileUtils.FILE_TYPES.MUSIC,
            context = context
        )
        if (cursor != null && cursor.count != 0) {
            do {
                cursor.moveToNext()
                val i = cursor.getLong(AudioColumns.DURATION)
                if ((cursor.getLong(AudioColumns.DURATION) ?: 0) > 60000)
                    songsAreInStorage.add(createSongFromCursor(cursor))
            } while (!cursor.isLast)
        } else {
//                TODO(handle null cursor)
        }
        cursor?.close()
        return songsAreInStorage
    }

}


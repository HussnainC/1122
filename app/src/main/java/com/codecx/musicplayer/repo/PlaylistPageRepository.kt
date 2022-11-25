package com.codecx.musicplayer.repo

import com.codecx.musicplayer.musicdatabase.entities.SongModel
import com.codecx.musicplayer.interfaces.PlaylistPageRepositoryInterface
import com.codecx.musicplayer.utils.DatabaseConverterUtils
import com.codecx.musicplayer.musicgraphicalinterfaces.Fragments.LibraryFragment
import com.codecx.musicplayer.musicgraphicalinterfaces.Fragments.PlaylistFragment

class PlaylistPageRepository() : PlaylistPageRepositoryInterface {
    private var playlistId: Long = 0
    fun setPlayListId(id: Long) {
        this.playlistId = id
    }

    override suspend fun getSongsIdFromDatabase(): String {
        var songsOfPlaylist: String = ""
        songsOfPlaylist = RoomRepository.localDatabase.playlistDao().getSongsOfPlaylist(playlistId)
        return songsOfPlaylist
    }

    override suspend fun songsIdToSongModelConverter(songId: String): SongModel? {
        val allSongsInStorage = LibraryFragment.viewModel.getDataSet()

        for (song in allSongsInStorage) {
            if (song.id == songId.toLong()) {
                return song
            }
        }
        return null
    }

    override suspend fun getSongs(): ArrayList<SongModel> {

        var songs: ArrayList<SongModel> = arrayListOf()

        val songsIdInString = getSongsIdFromDatabase()
        if (songsIdInString != null) {
            val songsIdInArraylist = convertStringToArraylist(songsIdInString)

            for (songId in songsIdInArraylist) {
                val realSong = songsIdToSongModelConverter(songId)
                if (realSong != null)
                    songs.add(realSong)
            }
        }

        return songs
    }

    fun convertStringToArraylist(songs: String): ArrayList<String> {
        return DatabaseConverterUtils.stringToArraylist(songs)
    }

    fun removeSongFromPlaylist(songId: String) {

        PlaylistFragment.viewModel?.playlistRepository?.removeSongFromPlaylist(playlistId, songId)

    }

}

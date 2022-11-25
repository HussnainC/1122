package com.codecx.musicplayer.utils

import com.codecx.musicplayer.musicdatabase.entities.SongModel
import com.codecx.musicplayer.repo.RoomRepository

fun SongModel.isFavorite(): Boolean
{
    for (favSongs in RoomRepository.cachedFavArray) {
        if (favSongs.id!!.equals(this.id)) {
            return true
        }
    }
    return false
}
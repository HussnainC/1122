package com.codecx.musicplayer.interfaces

import com.codecx.musicplayer.musicdatabase.entities.PlaylistModel

interface PassDataForSelectPlaylists {
    fun passDataToInvokingFragment(playlists : ArrayList<PlaylistModel>)
}
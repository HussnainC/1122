package com.codecx.musicplayer.recycleradapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.codecx.musicplayer.R
import com.codecx.musicplayer.databinding.PlaylistSongRvItemBinding
import com.codecx.musicplayer.musicdatabase.entities.SongModel
import com.codecx.musicplayer.helper.Coordinator
import com.codecx.musicplayer.utils.ImageUtils
import com.codecx.musicplayer.musicgraphicalinterfaces.activities.MainActivity

class PlaylistPageAdapater(var dataset: ArrayList<SongModel>, val context: Activity) :
    RVBaseAdapter() {

    var position = 0
    lateinit var dataSend: OnDataSend
    lateinit var playlist_name: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecyclerViewViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.playlist_song_rv_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val song: SongModel = dataset[position]

        this.position = position
        val viewHolder = holder as PlaylistPageAdapater.RecyclerViewViewHolder
        with(viewHolder.binding) {
            name = song.title
            artist = song.artist
            song.image?.let {
                ImageUtils.loadImageToImageView(
                    context = context,
                    imageView = playlistMusicIv,
                    image = it
                )
            }


            playlistSongContainer.setOnClickListener {
                updatePosition(newIndex = viewHolder.adapterPosition)
                Coordinator.SourceOfSelectedSong = playlist_name
                Coordinator.currentDataSource = dataset

                MainActivity.activity.updateVisibility()

                Coordinator.playSelectedSong(dataset[position])

            }

            playlistMusicMenuBtn.setOnClickListener {
                val popUpMenu = PopupMenu(context, it)
                popUpMenu.inflate(R.menu.songs_in_playlist_popup_menu)

                popUpMenu.setOnMenuItemClickListener {
                    val id = dataset[position].id
                    return@setOnMenuItemClickListener handleMenuButtonClickListener(
                        it.itemId,
                        id.toString()
                    )
                }
                popUpMenu.show()
            }
        }

    }

    private fun handleMenuButtonClickListener(itemId: Int, songId: String): Boolean {
        when (itemId) {
            R.id.delete_song_from_playlist_menu_item -> {
                dataSend.onSend(context, songId)
            }
            else -> return false
        }
        return true
    }

    fun updatePosition(newIndex: Int) {
        position = newIndex
    }


    override fun getCurrentPosition(): Int {
        return position
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    interface OnDataSend {
        fun onSend(context: Activity, id: String)
        fun onDelete(id: String)
    }

    fun OnDataSend(dataSend: OnDataSend) {
        this.dataSend = dataSend
    }

    open inner class RecyclerViewViewHolder(val binding: PlaylistSongRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
package com.codecx.musicplayer.recycleradapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.codecx.musicplayer.R
import com.codecx.musicplayer.databinding.PlaylistItemBinding
import com.codecx.musicplayer.musicdatabase.entities.PlaylistModel
import com.codecx.musicplayer.repo.PlaylistRepository
import com.codecx.musicplayer.musicgraphicalinterfaces.Fragments.PlaylistFragment

class PlaylistAdapter(
    var context: Activity,
    arrayList: ArrayList<PlaylistModel>
) : RVBaseAdapter() {

    var dataset: ArrayList<PlaylistModel>
    lateinit var viewHolder: PlaylistAdapter.RecyclerViewViewHolder
    lateinit var dataSend: PlaylistAdapter.OnDataSend

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return RecyclerViewViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.playlist_item,
                parent,
                false
            )
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val playlist: PlaylistModel = dataset[position]
        viewHolder =
            holder as PlaylistAdapter.RecyclerViewViewHolder
        with(viewHolder.binding) {

            name = playlist.name
            val playlistRepository = PlaylistRepository(context)

            noOfSongs = context.getString(
                R.string.countOfSongsInPlaylist,
                playlist.countOfSongs.toString()
            )

            playlistItem.setOnClickListener {

                dataSend.openPlaylist(playlist.id)

            }

            playlistMenuBtn.setOnClickListener { it ->
                val popUpMenu = PopupMenu(context, it)
                popUpMenu.inflate(R.menu.playlists_popup_menu)

                popUpMenu.setOnMenuItemClickListener {
                    val id = playlistRepository.getPlaylists()[position].id

                    return@setOnMenuItemClickListener handleMenuButtonClickListener(
                        it.itemId,
                        id,position
                    )
                }
                popUpMenu.show()
            }
        }



    }

    override fun getCurrentPosition(): Int {
//        Fake!
        return -1
    }


    private fun handleMenuButtonClickListener(itemId: Int, playlistId: Long,position: Int): Boolean {
        when (itemId) {
            R.id.deletePlaylist_menu_item -> {
                PlaylistFragment.viewModel?.playlistRepository?.removePlaylist(playlistId)
                dataset.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position,dataset.size)
            //  dataSend.onSend(context, playlistId)
            }
//            R.id.renamePlaylist_menu_item -> {
////                TODO
//            }
            else -> return false
        }
        return true
    }

    override fun getItemCount(): Int {
        return dataset.size
    }


    interface OnDataSend {
        fun onSend(context: Activity, id: Long)
        fun openPlaylist(id: Long)
    }

    fun OnDataSend(dataSend: OnDataSend) {
        this.dataSend = dataSend
    }

    open inner class RecyclerViewViewHolder(val binding: PlaylistItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    init {
        dataset = arrayList
    }

}
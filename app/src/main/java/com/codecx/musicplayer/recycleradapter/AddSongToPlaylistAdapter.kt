package com.codecx.musicplayer.recycleradapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.codecx.musicplayer.R
import com.codecx.musicplayer.databinding.AddSongToPlaylistDialogItemBinding
import com.codecx.musicplayer.musicdatabase.entities.PlaylistModel


class AddSongToPlaylistAdapter(
    var context: Activity,
    arrayList: ArrayList<PlaylistModel>
) : RVBaseAdapter() {

    var dataset: ArrayList<PlaylistModel>

    companion object {
        var choices: ArrayList<PlaylistModel> = arrayListOf()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return RecyclerViewViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.add_song_to_playlist_dialog_item,
                parent,
                false
            )
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val playlist: PlaylistModel = dataset[position]
        val viewHolder =
            holder as AddSongToPlaylistAdapter.RecyclerViewViewHolder
        with(viewHolder.binding) {
            tvName = playlist.name
            materialCheckBox.setOnClickListener {

                if (materialCheckBox.isChecked) {
                    choices?.add(playlist)
                } else {
                    choices?.remove(playlist)
                }
            }
        }


    }

    override fun getCurrentPosition(): Int {
//        Fake!
        return -1
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    open inner class RecyclerViewViewHolder(val binding: AddSongToPlaylistDialogItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    init {
        dataset = arrayList
    }
}
package com.codecx.musicplayer.recycleradapter

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.codecx.musicplayer.R
import com.codecx.musicplayer.databinding.FavRvItemBinding
import com.codecx.musicplayer.musicdatabase.entities.SongModel
import com.codecx.musicplayer.helper.Coordinator
import com.codecx.musicplayer.repo.RoomRepository
import com.codecx.musicplayer.utils.ImageUtils
import com.codecx.musicplayer.musicgraphicalinterfaces.activities.MainActivity

class FavAdapter(
    val context: Activity,
    var dataset: ArrayList<SongModel>
) : RVBaseAdapter() {

    var position = 0

    lateinit var dataSend: OnDataSend


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return RecyclerViewViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.fav_rv_item,
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val song: SongModel = dataset[position]
        this.position = position
        val viewHolder = holder as RecyclerViewViewHolder
        with(viewHolder.binding) {
            title = song.title
            artist = song.artist
            song.image?.let {
                ImageUtils.loadImageToImageView(
                    context = context,
                    imageView = favMusicIv,
                    image = it
                )
            }

            favSongContainer.setOnClickListener {
                updatePosition(newIndex = viewHolder.adapterPosition)
                Coordinator.SourceOfSelectedSong = "fav"
                Coordinator.currentDataSource = dataset
                MainActivity.activity.updateVisibility()
                Coordinator.playSelectedSong(dataset[position])

            }

            likeBtn.setOnClickListener { it ->
               likeBtn.setImageResource(R.drawable.ic_unfavored)
                RoomRepository.removeSongFromFavorites(dataset[position])
            }
        }

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

    fun getSongUri(position: Int): Uri? {
        return dataset[position].uri
    }

    fun getSong(position: Int): SongModel {
        if (position < 0) {

            Toast.makeText(context, "please try again!", Toast.LENGTH_SHORT).show()
            return SongModel()
        } else {
            return dataset[position]
        }
    }

    open inner class RecyclerViewViewHolder(val binding: FavRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnDataSend {
        fun onSend(context: Activity, songModel: SongModel)
    }

    fun OnDataSend(dataSend: OnDataSend) {
        this.dataSend = dataSend
    }
}
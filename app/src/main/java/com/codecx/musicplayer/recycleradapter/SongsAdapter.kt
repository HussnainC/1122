package com.codecx.musicplayer.recycleradapter

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.codecx.musicplayer.R
import com.codecx.musicplayer.databinding.SongRvItemBinding
import com.codecx.musicplayer.musicdatabase.entities.SongModel
import com.codecx.musicplayer.helper.Coordinator
import com.codecx.musicplayer.repo.RoomRepository
import com.codecx.musicplayer.utils.ImageUtils
import com.codecx.musicplayer.utils.SongUtils
import com.codecx.musicplayer.utils.TimeUtils
import com.codecx.musicplayer.musicgraphicalinterfaces.Fragments.LibraryFragment
import com.codecx.musicplayer.musicgraphicalinterfaces.activities.MainActivity
import com.codecx.musicplayer.musicgraphicalinterfaces.dialogs.SongDetailsDialog


class SongsAdapter(
    val context: Activity,
    var dataset: ArrayList<SongModel>
) : RVBaseAdapter(), Filterable {

    var position = 0
    var songFilterList: ArrayList<SongModel> = ArrayList()
    lateinit var dataSend: OnDataSend

    lateinit var viewHolder: RecyclerViewViewHolder

    init {
        songFilterList = dataset
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        return RecyclerViewViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.song_rv_item,
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val song: SongModel = songFilterList[position]
        this.position = position
        viewHolder = holder as RecyclerViewViewHolder
        with(viewHolder.binding) {
            name = song.title
            songDuration.text = song.duration?.let { TimeUtils.getReadableDuration(it) }
            artist = song.artist
            song.image?.let {
                ImageUtils.loadImageToImageView(
                    context = context,
                    imageView = musicIv,
                    image = it
                )
            }


            songContainer.setOnClickListener {
                updatePosition(newIndex = viewHolder.adapterPosition)
                Coordinator.SourceOfSelectedSong = "library"
                Coordinator.currentDataSource = songFilterList


                MainActivity.activity.updateVisibility()
                Coordinator.playSelectedSong(songFilterList[position])

            }


            musicMenuBtn.setOnClickListener { it ->
                val popUpMenu = PopupMenu(context, it)
                popUpMenu.inflate(R.menu.songs_popup_menu)

                popUpMenu.setOnMenuItemClickListener {

                    return@setOnMenuItemClickListener handleMenuButtonClickListener(
                        it.itemId,
                        position
                    )
                }
                popUpMenu.show()
            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun handleMenuButtonClickListener(itemId: Int, position: Int): Boolean {
        when (itemId) {

            R.id.addToPlayList_menu_item -> {

                if (position >= 0) {
                    dataSend.onSend(context, getSong(position))
                } else {
                    Toast.makeText(context, "please try again", Toast.LENGTH_SHORT).show()
                }

            }
            R.id.deleteFromDevice_menu_item -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val songid = songFilterList[position].id

                    for (playlistId in songid?.let {
                        RoomRepository.listOfPlaylistsContainSpecificSong(
                            it
                        )
                    }!!) {
                        RoomRepository.removeSongFromPlaylist(playlistId, songid.toString())
                    }

                    RoomRepository.removeSongFromFavorites(songFilterList[position])

                    getSongUri(position)?.let {
                        SongUtils.deleteMusic(
                            LibraryFragment.mactivity.baseContext,
                            LibraryFragment.mactivity,
                            it
                        )
                    }

                } else {
                    getSongUri(position)?.let { SongUtils.del(getSong(position).id.toString(), it) }
                }

            }
            R.id.details_menu_item -> {


                val songDetailsDialog = SongDetailsDialog(getSong(position))

                val manager: FragmentManager =
                    (context as AppCompatActivity).supportFragmentManager

                manager?.beginTransaction()
                    ?.let { it -> songDetailsDialog.show(it, "songDetails") }


            }
            R.id.share_menu_item -> {
                SongUtils.shareMusic(context, getSong(position))
            }
//            R.id.setAsRingtone_menu_item -> {
//            }
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
        return songFilterList.size
    }

    fun getSongUri(position: Int): Uri? {
        return songFilterList[position].uri
    }

    fun getSong(position: Int): SongModel {
        if (position < 0) {

            Toast.makeText(context, "please try again!", Toast.LENGTH_SHORT).show()
            return SongModel()
        } else {
            return songFilterList[position]
        }
    }

    open inner class RecyclerViewViewHolder(val binding: SongRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnDataSend {
        fun onSend(context: Activity, songModel: SongModel)
    }

    fun OnDataSend(dataSend: OnDataSend) {
        this.dataSend = dataSend
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString()?.lowercase() ?: ""
                if (charString.isEmpty()) songFilterList = dataset else {
                    val filteredList = ArrayList<SongModel>()
                    dataset.filter {
                        it.title?.lowercase()?.contains(charString) == true
                    }.forEach { filteredList.add(it) }
                    songFilterList = filteredList

                }
                return FilterResults().apply { values = songFilterList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                songFilterList = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<SongModel>
                notifyDataSetChanged()
            }
        }
    }

}
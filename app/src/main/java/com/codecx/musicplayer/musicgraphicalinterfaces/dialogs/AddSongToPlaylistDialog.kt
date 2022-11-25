package com.codecx.musicplayer.musicgraphicalinterfaces.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.codecx.musicplayer.R
import com.codecx.musicplayer.recycleradapter.AddSongToPlaylistAdapter
import com.codecx.musicplayer.databinding.AddSongToPlaylistBinding
import com.codecx.musicplayer.musicdatabase.entities.PlaylistModel
import com.codecx.musicplayer.interfaces.PassDataForSelectPlaylists
import com.codecx.musicplayer.utils.ScreenSizeUtils

class AddSongToPlaylistDialog(val array: ArrayList<PlaylistModel>) : DialogFragment() {

    lateinit var binding: AddSongToPlaylistBinding
    var playlistAdapter: AddSongToPlaylistAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        dialog?.window?.apply {
            setBackgroundDrawable(ContextCompat.getDrawable(context!!, android.R.color.transparent))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        val view = inflater.inflate(R.layout.add_song_to_playlist, container, false)
        initBinding(view)

        playlistAdapter = activity?.let {
            AddSongToPlaylistAdapter(
                it,
                array
            )
        }

        binding.playlists.layoutManager = LinearLayoutManager(context)
        binding.playlists.adapter = playlistAdapter

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
with(binding){

    addSongToPlaylistLayout.layoutParams.width =
        ScreenSizeUtils.getScreenWidth() * 6 / 10
    addSongToPlaylistLayout.layoutParams.height =
        ScreenSizeUtils.getScreenHeight() * 6 / 10
    addSongToPlaylistLayout.requestLayout()

    playlists.layoutParams.width =
        addSongToPlaylistLayout.layoutParams.width * 10 / 10
    playlists.layoutParams.height =
        (addSongToPlaylistLayout.layoutParams.height* 6.5 / 10).toInt()
    playlists.requestLayout()

    acceptSelectedPlaylistBtn.layoutParams.width =
        addSongToPlaylistLayout.layoutParams.width * 4 / 10
    acceptSelectedPlaylistBtn.layoutParams.height =
        (addSongToPlaylistLayout.layoutParams.height* 1.2 / 10).toInt()
    acceptSelectedPlaylistBtn.requestLayout()
}

    }

    override fun onResume() {
        super.onResume()

        binding.acceptSelectedPlaylistBtn.setOnClickListener {

            val targetFragment = targetFragment
            val passData: PassDataForSelectPlaylists = targetFragment as PassDataForSelectPlaylists
            passData.passDataToInvokingFragment(AddSongToPlaylistAdapter.choices)

            this.dismiss()
        }
    }

    private fun initBinding(view: View) {
        binding = AddSongToPlaylistBinding.bind(view)
    }

}




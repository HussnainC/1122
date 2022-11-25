package com.codecx.musicplayer.musicgraphicalinterfaces.Fragments

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.codecx.musicplayer.R
import com.codecx.musicplayer.recycleradapter.PlaylistAdapter
import com.codecx.musicplayer.baseclasses.BaseFragment
import com.codecx.musicplayer.databinding.FragmentPlaylistBinding
import com.codecx.musicplayer.musicdatabase.entities.PlaylistModel
import com.codecx.musicplayer.interfaces.PassData
import com.codecx.musicplayer.musicgraphicalinterfaces.activities.MainActivity
import com.codecx.musicplayer.musicgraphicalinterfaces.dialogs.CreatePlaylistDialog
import com.codecx.musicplayer.utils.beGone
import com.codecx.musicplayer.utils.beVisible
import com.codecx.musicplayer.livedataviewmodels.PlaylistViewModel


class PlaylistFragment : BaseFragment(R.layout.fragment_playlist), PassData {

    var playlistAdapter: PlaylistAdapter? = null

    private var newPlaylistName: String = ""

    companion object {
        var viewModel: PlaylistViewModel? = null
    }

    private val binding get() = _binding as FragmentPlaylistBinding


    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(PlaylistViewModel::class.java)
        context?.let { viewModel?.setFragmentContext(it) }
        viewModel!!.dataset.observe(viewLifecycleOwner, playlistUpdateObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = DataBindingUtil.bind(view)!!

        setupViewModel()
        playlistAdapter = activity?.let {
            PlaylistAdapter(
                it,
                viewModel?.dataset?.value as ArrayList<PlaylistModel>
            )
        }

        binding.playlistRv.layoutManager = LinearLayoutManager(context)
    }


    override fun passDataToInvokingFragment(str: String?) {
        newPlaylistName = str ?: ""

        context?.let { it ->
            viewModel?.playlistRepository?.createPlaylist(newPlaylistName)
            viewModel?.updateDataset()
        }
    }

    override fun onResume() {
        super.onResume()

        binding.fab.setOnClickListener {

            val createPlaylist = CreatePlaylistDialog()

            createPlaylist.setTargetFragment(this, 0)
            this.fragmentManager?.let { it1 -> createPlaylist.show(it1, "pl") }

        }

        playlistAdapter?.OnDataSend(
            object : PlaylistAdapter.OnDataSend {
                override fun onSend(context: Activity, id: Long) {
                    viewModel?.updateDataset()
                }

                override fun openPlaylist(id: Long) {

                    val playlistPageFragment = PlaylistPageFragment(id)
                    val fragmentManager: FragmentManager =
                        MainActivity.activity.supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.addToBackStack(null)
                    transaction.add(
                        R.id.fragment_base_container,
                        playlistPageFragment,
                        "bottom sheet container"
                    )
                        .commit()

                }
            }
        )
        viewModel?.updateDataset()


    }

    private val playlistUpdateObserver = Observer<ArrayList<Any>> { dataset ->

        if (dataset.isNotEmpty()) {
            playlistAdapter?.dataset = dataset as ArrayList<PlaylistModel>
            binding.mEmpty.beGone()
            binding.playlistRv.apply {
                adapter = playlistAdapter
                beVisible()
            }

        } else {
            with(binding) {
                mEmpty.beVisible()
                playlistRv.beGone()
            }
        }
    }

}
package com.codecx.musicplayer.livedataviewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codecx.musicplayer.musicdatabase.entities.PlaylistModel
import com.codecx.musicplayer.repo.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel : BaseViewModel() {


    override var dataset: MutableLiveData<ArrayList<Any>> = MutableLiveData()
    lateinit var playlistRepository: PlaylistRepository


    init {
        dataset.value = ArrayList()
    }


    fun setFragmentContext(context: Context) {
        playlistRepository = PlaylistRepository(context)
        fillRecyclerView()
    }

    fun fillRecyclerView() {
        updateDataset()
    }

    fun updateDataset() = viewModelScope.launch(Dispatchers.IO) {

        dataset.postValue(playlistRepository.getPlaylists() as ArrayList<Any>)
    }

    fun getDataSet(): ArrayList<PlaylistModel> {
        updateDataset()
        return dataset.value as ArrayList<PlaylistModel>
    }

}
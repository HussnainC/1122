package com.codecx.musicplayer.livedataviewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codecx.musicplayer.musicdatabase.entities.SongModel
import com.codecx.musicplayer.repo.PlaylistPageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistPageViewModel(
    val playlistPageRepository: PlaylistPageRepository
) : BaseViewModel() {

    override var dataset: MutableLiveData<ArrayList<Any>> = MutableLiveData()
    private var playlistId: Long = -1L


    init {
        dataset.value = ArrayList()
    }

    fun setPlayllistId(pId: Long) {
        playlistId = pId
        playlistPageRepository.setPlayListId(playlistId)
        fillRecyclerView()
    }

    fun fillRecyclerView() = viewModelScope.launch(Dispatchers.IO) {
        updateDataset()

    }

    fun updateDataset() = viewModelScope.launch(Dispatchers.IO) {
        dataset.postValue(playlistPageRepository.getSongs() as ArrayList<Any>)
    }

    fun getDataset(): ArrayList<SongModel> {
        return dataset.value as ArrayList<SongModel>
    }
}
package com.codecx.musicplayer.livedataviewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codecx.musicplayer.musicdatabase.entities.SongModel
import com.codecx.musicplayer.repo.SongsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SongsViewModel : BaseViewModel() {


    override var dataset: MutableLiveData<ArrayList<Any>> = MutableLiveData()
    lateinit var songsRepository: SongsRepository


    init {
        dataset.value = ArrayList()
    }

    fun setFragmentContext(context: Context) {
        songsRepository = SongsRepository(context)
        fillRecyclerView()
    }


    fun fillRecyclerView() {
        updateDataset()
    }


     fun updateDataset() = viewModelScope.launch(Dispatchers.IO) {
        dataset.postValue(songsRepository.getListOfSongs() as ArrayList<Any>)
    }


    fun getDataSet(): ArrayList<SongModel> {
        return dataset.value as ArrayList<SongModel>
    }

}
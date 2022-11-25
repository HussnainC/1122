package com.codecx.musicplayer.livedataviewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codecx.musicplayer.musicdatabase.entities.SongModel
import com.codecx.musicplayer.repo.FavRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavViewModel( var favRepository: FavRepository) : BaseViewModel() {
    override var dataset: MutableLiveData<ArrayList<Any>> = MutableLiveData()


    init {
        dataset.value = ArrayList()

        fillRecyclerView()
    }

     fun fillRecyclerView() {
        updateDataset()
    }

    fun updateDataset() =viewModelScope.launch(Dispatchers.IO){
        dataset.postValue(favRepository.getFavSongs() as ArrayList<Any>)
    }

    fun getDataset(): ArrayList<SongModel>
    {
        return dataset.value as ArrayList<SongModel>
    }

}
package com.codecx.musicplayer.livedataviewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    open lateinit var dataset: MutableLiveData<ArrayList<Any>>

}
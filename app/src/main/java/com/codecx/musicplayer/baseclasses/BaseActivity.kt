package com.codecx.musicplayer.baseclasses

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

open class BaseActivity(val layoutId: Int) : AppCompatActivity() {
    lateinit var _binding: ViewDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        _binding = DataBindingUtil.setContentView(this, layoutId)
    }
}
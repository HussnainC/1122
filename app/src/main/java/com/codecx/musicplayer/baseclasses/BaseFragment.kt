package com.codecx.musicplayer.baseclasses

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

open class BaseFragment(val layoutId: Int) : Fragment(layoutId) {
    lateinit var _binding: ViewDataBinding

}
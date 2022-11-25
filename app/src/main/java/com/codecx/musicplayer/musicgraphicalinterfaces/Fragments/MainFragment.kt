package com.codecx.musicplayer.musicgraphicalinterfaces.Fragments

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.codecx.musicplayer.R
import com.codecx.musicplayer.recycleradapter.ViewPagerFragmentAdapter
import com.codecx.musicplayer.baseclasses.BaseFragment
import com.codecx.musicplayer.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainFragment : BaseFragment(R.layout.fragment_main) {

    val binding get() = _binding as FragmentMainBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = DataBindingUtil.bind(view)!!

        initTabs()
    }

    private fun initTabs() {

        val tabNames = resources.getStringArray(R.array.tabNames)
        val icons = arrayOf(R.drawable.music2, R.drawable.playlist, R.drawable.lover)

        val adapter = ViewPagerFragmentAdapter(childFragmentManager, lifecycle)
        adapter.apply {
            addFragment(LibraryFragment())
            addFragment(PlaylistFragment())
            addFragment(FavoriteFragment())
        }
        with(binding) {
            viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewpager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
            viewpager.adapter = adapter
            TabLayoutMediator(toolbar.tabLayoutHome, viewpager)
            { tab, position ->
                tab.text = tabNames[position]
                tab.setIcon(icons[position])
            }.attach()

        }

    }
}
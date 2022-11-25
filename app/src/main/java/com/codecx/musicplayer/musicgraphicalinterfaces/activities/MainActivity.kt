package com.codecx.musicplayer.musicgraphicalinterfaces.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.codecx.musicplayer.R
import com.codecx.musicplayer.baseclasses.BaseActivity
import com.codecx.musicplayer.databinding.ActivityMainBinding
import com.codecx.musicplayer.helper.Coordinator
import com.codecx.musicplayer.repo.RoomRepository
import com.codecx.musicplayer.musicservices.NotificationPlayerService
import com.codecx.musicplayer.musicgraphicalinterfaces.Fragments.MainFragment
import com.codecx.musicplayer.musicgraphicalinterfaces.Fragments.PlayerPanelFragment
import com.codecx.musicplayer.utils.PermissionProvider
import com.codecx.musicplayer.utils.ScreenSizeUtils.getScreenHeight
import com.sothree.slidinguppanel.SlidingUpPanelLayout


class MainActivity : BaseActivity(R.layout.activity_main) {

    companion object {
        var permissionsGranted: Boolean = false
        lateinit var playerPanelFragment: PlayerPanelFragment
        lateinit var activity: MainActivity

    }


    var prefs: SharedPreferences? = null


    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )


    private val binding get() = _binding as ActivityMainBinding


    override fun onDestroy() {
        super.onDestroy()

        NotificationPlayerService.stopNotification(baseContext)

        Coordinator.mediaPlayerAgent.stop()
    }

    override fun onBackPressed() {
        onStop()
        moveTaskToBack(true)
    }


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        activity = this
        RoomRepository.createDatabase()

        prefs = getSharedPreferences("AppName", MODE_PRIVATE)

        checkForPermissions()

        initMainFragment()

        initBottomSheet()
        with(binding) {

            slidingLayout.panelHeight = 0
            slidingLayout.requestLayout()
            slidingLayout.addPanelSlideListener(object :
                SlidingUpPanelLayout.PanelSlideListener {
                override fun onPanelSlide(panel: View?, slideOffset: Float) {
                }

                override fun onPanelStateChanged(
                    panel: View?,
                    previousState: SlidingUpPanelLayout.PanelState?,
                    newState: SlidingUpPanelLayout.PanelState?
                ) {
                    when (slidingLayout.panelState) {
                        SlidingUpPanelLayout.PanelState.EXPANDED -> {
                            playerPanelFragment.updatePanelBasedOnState(SlidingUpPanelLayout.PanelState.EXPANDED)
                        }
                        SlidingUpPanelLayout.PanelState.COLLAPSED -> {
                            playerPanelFragment.updatePanelBasedOnState(SlidingUpPanelLayout.PanelState.COLLAPSED)

                        }
                        else -> {}
                    }
                }
            })
        }


    }

    fun updateVisibility() {
        binding.slidingLayout.panelHeight = getScreenHeight() * 1 / 10
    }


    private fun checkForPermissions() {
        val permissionProvider = PermissionProvider()
        permissionProvider.askForPermission(this, permissions)

        if (permissionProvider.permissionIsGranted) {

        }

    }


    private fun initBottomSheet() {

        playerPanelFragment = PlayerPanelFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(
            binding.bottomSheetContainer.id,
            playerPanelFragment,
            "bottom sheet container"
        )
            .commit()
    }

    private fun initMainFragment() {
        val mainFragment = MainFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(
            binding.fragmentBaseContainer.id,
            mainFragment,
            "main fragment container"
        )
            .commit()
    }

}
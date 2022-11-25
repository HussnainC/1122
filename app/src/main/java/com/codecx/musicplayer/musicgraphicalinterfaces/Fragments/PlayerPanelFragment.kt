package com.codecx.musicplayer.musicgraphicalinterfaces.Fragments

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import androidx.annotation.RequiresApi
import com.codecx.musicplayer.R
import com.codecx.musicplayer.baseclasses.BaseFragment
import com.codecx.musicplayer.databinding.FragmentPlayerPanelBinding
import com.codecx.musicplayer.helper.Coordinator
import com.codecx.musicplayer.interfaces.PlayerPanelInterface
import com.codecx.musicplayer.repo.RoomRepository
import com.codecx.musicplayer.musicgraphicalinterfaces.activities.MainActivity
import com.codecx.musicplayer.utils.ImageUtils
import com.codecx.musicplayer.utils.ScreenSizeUtils
import com.codecx.musicplayer.utils.TimeUtils
import com.codecx.musicplayer.utils.isFavorite
import com.frolo.waveformseekbar.WaveformSeekBar
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlin.random.Random


class PlayerPanelFragment : BaseFragment(R.layout.fragment_player_panel), PlayerPanelInterface,
    View.OnClickListener,
    WaveformSeekBar.OnSeekBarChangeListener {
    lateinit var binding: FragmentPlayerPanelBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentPlayerPanelBinding.bind(view)

        updatePanelBasedOnState(SlidingUpPanelLayout.PanelState.COLLAPSED)

        activity?.baseContext?.let {
            Coordinator.setup(
                it
            )
        }

        setDefaultVisibilities()

        setOnEventListeners()

        seekbarHandler()

        binding.playerRemote.waveformSeekBar.setWaveform(createWaveform(), true)
        setupLayoutParams()

    }

    private fun setupLayoutParams() {
        with(binding) {


            header.apply {
                layoutParams.height = (ScreenSizeUtils.getScreenHeight() * 0.8 / 10).toInt()
                requestLayout()
            }


            musicTitleTv.layoutParams.height =
                (ScreenSizeUtils.getScreenHeight() * 0.4 / 10).toInt()
            header.requestLayout()


            playerRemote.constraintLayout2.layoutParams.height =
                (ScreenSizeUtils.getScreenHeight() * 0.5 / 10).toInt()
            header.requestLayout()


            playerRemote.constraintLayout3.layoutParams.height =
                (ScreenSizeUtils.getScreenHeight() * 1.4 / 10).toInt()
            header.requestLayout()


            playerRemote.shuffleRepeatLayout.layoutParams.height =
                (ScreenSizeUtils.getScreenHeight() * 1 / 10)
            header.requestLayout()

            onCollapse.wheelprogress.apply {
                layoutParams.height =
                    ((ScreenSizeUtils.getScreenHeight() * 1 / 10) * 7 / 10)
                layoutParams.width =
                    (ScreenSizeUtils.getScreenWidth() * 1.1 / 10).toInt()
                requestLayout()
            }
            onExpand.likeIv.apply {
                layoutParams.height =
                    (ScreenSizeUtils.getScreenHeight() * 0.6 / 10).toInt()
                layoutParams.width =
                    (ScreenSizeUtils.getScreenWidth() * 0.6 / 10).toInt()

                requestLayout()
            }
        }
    }

    override fun setDefaultVisibilities() {
        binding.onCollapse.playBtnOnHeader.visibility = View.GONE
    }

    fun setOnEventListeners() {
        with(binding) {

            onCollapse.playBtnOnHeader.setOnClickListener(this@PlayerPanelFragment)
            onCollapse.pauseBtnOnHeader.setOnClickListener(this@PlayerPanelFragment)
            playerRemote.nextBtn.setOnClickListener(this@PlayerPanelFragment)
            playerRemote.prevBtn.setOnClickListener(this@PlayerPanelFragment)
            playerRemote.playOrPauseLayout.setOnClickListener(this@PlayerPanelFragment)
            playerRemote.shuffleContainer.setOnClickListener(this@PlayerPanelFragment)
            playerRemote.repeatContainer.setOnClickListener(this@PlayerPanelFragment)
            playerRemote.waveformSeekBar.setOnSeekBarChangeListener(this@PlayerPanelFragment)
            onExpand.likeIv.setOnClickListener(this@PlayerPanelFragment)
        }
    }

    override fun setSongImage() {

        context?.let {
            ImageUtils.loadImageToImageView(
                it,
                binding.musicAlbumImage,
                Coordinator.currentPlayingSong?.image!!
            )
        }
    }

    fun updatePanel() {
        setSongTitle()
        setSongImage()

        if (Coordinator.currentPlayingSong!!.isFavorite()) {
            binding.onExpand.likeIv.setImageResource(R.drawable.lover)
        } else {
            binding.onExpand.likeIv.setImageResource(R.drawable.ic_unfavored)
        }

        binding.playerRemote.musicMax.text =
            Coordinator.currentPlayingSong?.duration?.let {
                TimeUtils.getReadableDuration(
                    it
                )
            }
    }

    override fun setSongTitle() {
        binding.musicTitleTv.text = Coordinator.currentPlayingSong?.title
    }

    override fun getPanelState() {
    }

    override fun setPanelState() {
    }

    override fun updateHeader() {
    }

    override fun seekTo(mCurrentPosition: Int) {

        binding.playerRemote.waveformSeekBar.setProgressInPercentage(
            (mCurrentPosition / (Coordinator.currentPlayingSong?.duration?.div(
                1000F
            )!!))
        )
        if (binding.onCollapse.root.visibility == View.VISIBLE) {

            updateWheelProgress(
                (mCurrentPosition * 360) / ((Coordinator.currentPlayingSong?.duration?.div(
                    1000
                ))?.toInt() ?: 0)
            )

            binding.onCollapse.songTitleOnHeader.text =
                if (Coordinator.isPlaying()) Coordinator.currentPlayingSong?.title else ""

            context?.let {
                ImageUtils.loadImageToImageView(
                    it,
                    binding.onCollapse.songImageOnHeader,
                    Coordinator.currentPlayingSong?.image!!
                )
            }
        }

    }

    override fun seekbarHandler() {
        val mHandler = Handler()
        activity?.runOnUiThread(object : Runnable {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                if (Coordinator.isPlaying()) {

                    val mCurrentPosition = Coordinator.getPositionInPlayer() / 1000
                    val duration = Coordinator.currentPlayingSong?.duration?.div(1000)

                    seekTo(mCurrentPosition)
                    setRemainingTime(mCurrentPosition)

                    if (mCurrentPosition == duration?.toInt()?.minus(3) ?: 0) {
                        Coordinator.takeActionBasedOnRepeatMode(
                            MainActivity.activity.getString(R.string.onSongCompletion),
                            MainActivity.activity.getString(R.string.play_next)
                        )
                    }
                }
                mHandler.postDelayed(this, 1000)
            }
        })
    }

    override fun switchPlayPauseButton() {
        if (Coordinator.isPlaying()) {
            with(binding) {
                if (!musicAnimation.isAnimating) {
                    musicAnimation.playAnimation()
                }
                playerRemote.btnPauseBtn.visibility = View.VISIBLE
                playerRemote.btnPlayBtn.visibility = View.GONE
            }
        } else {
            with(binding) {
                if (musicAnimation.isAnimating) {
                    musicAnimation.pauseAnimation()
                }
                playerRemote.btnPauseBtn.visibility = View.GONE
                playerRemote.btnPlayBtn.visibility = View.VISIBLE
            }
        }
    }

    override fun updatePanelBasedOnState(newState: SlidingUpPanelLayout.PanelState) {

        when (newState) {
            SlidingUpPanelLayout.PanelState.EXPANDED -> {

                binding.onExpand.root.visibility = View.VISIBLE
                binding.onCollapse.root.visibility = View.GONE
                switchPlayPauseButton()
            }
            SlidingUpPanelLayout.PanelState.COLLAPSED -> {

                binding.onCollapse.root.visibility = View.VISIBLE
                binding.onExpand.root.visibility = View.GONE

                try {
                    if (Coordinator.isPlaying()) {
                        binding.onCollapse.playBtnOnHeader.visibility = View.GONE
                        binding.onCollapse.pauseBtnOnHeader.visibility = View.VISIBLE
                    } else {
                        binding.onCollapse.playBtnOnHeader.visibility = View.VISIBLE
                        binding.onCollapse.pauseBtnOnHeader.visibility = View.GONE
                    }
                } catch (e: Exception) {

                }
            }
            else -> {}
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {

        with(binding) {
            when (v) {

                onExpand.likeIv -> {
                    if (Coordinator.currentPlayingSong!!.isFavorite()) {

                        onExpand.likeIv.setImageResource(R.drawable.ic_unfavored)
                        RoomRepository.removeSongFromFavorites(Coordinator.currentPlayingSong!!)


                    } else {
                        onExpand.likeIv.setImageResource(R.drawable.lover)

                        RoomRepository.addSongToFavorites(Coordinator.currentPlayingSong!!.id ?: -1)

                    }
                }

                playerRemote.nextBtn -> Coordinator.playNextSong()

                playerRemote.prevBtn -> Coordinator.playPrevSong()

                playerRemote.playOrPauseLayout -> {

                    if (Coordinator.isPlaying()) {
                        Coordinator.pause()
                    } else {
                        Coordinator.resume()
                    }
                    switchPlayPauseButton()
                }

                playerRemote.shuffleContainer -> {
                    if (Coordinator.shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_NONE) {

                        Coordinator.shuffleMode = PlaybackStateCompat.SHUFFLE_MODE_ALL

                        playerRemote.shuffleContainer.displayedChild = 1

                        Coordinator.updateNowPlayingQueue()

                    } else {

                        Coordinator.shuffleMode = PlaybackStateCompat.SHUFFLE_MODE_NONE

                        playerRemote.shuffleContainer.displayedChild = 2

                        Coordinator.updateNowPlayingQueue()
                    }
                }

                playerRemote.repeatContainer -> {
                    when (Coordinator.repeatMode) {
                        PlaybackStateCompat.REPEAT_MODE_NONE -> {
                            Coordinator.repeatMode = PlaybackStateCompat.REPEAT_MODE_ALL

                            playerRemote.repeatContainer.displayedChild = 1

                            Coordinator.updateNowPlayingQueue()
                        }

                        PlaybackStateCompat.REPEAT_MODE_ALL -> {
                            Coordinator.repeatMode = PlaybackStateCompat.REPEAT_MODE_ONE

                            playerRemote.repeatContainer.displayedChild = 2

                            Coordinator.updateNowPlayingQueue()
                        }

                        PlaybackStateCompat.REPEAT_MODE_ONE -> {
                            Coordinator.repeatMode = PlaybackStateCompat.REPEAT_MODE_NONE

                            playerRemote.repeatContainer.displayedChild = 3

                            Coordinator.updateNowPlayingQueue()
                        }
                    }
                }

                onCollapse.playBtnOnHeader -> {

                    Coordinator.resume()
                    onCollapse.playBtnOnHeader.visibility = View.GONE
                    onCollapse.pauseBtnOnHeader.visibility = View.VISIBLE
                }

                onCollapse.pauseBtnOnHeader -> {
                    Coordinator.pause()
                    onCollapse.playBtnOnHeader.visibility = View.VISIBLE
                    onCollapse.pauseBtnOnHeader.visibility = View.GONE
                }
            }

        }
    }

    private fun createWaveform(): IntArray {
        val random = Random(System.currentTimeMillis())
        val length: Int = 50 + random.nextInt(50)
        val values = IntArray(length)
        var maxValue = 0
        for (i in 0 until length) {
            val newValue: Int = 5 + random.nextInt(50)
            if (newValue > maxValue) {
                maxValue = newValue
            }
            values[i] = newValue
        }
        return values
    }

    override fun setRemainingTime(remainingTimeInPercentage: Int) {
        binding.playerRemote.musicMin.text =
            TimeUtils.getReadableDuration((remainingTimeInPercentage * 1000).toLong())
    }

    private fun updateWheelProgress(progressInPercentage: Int) {
        binding.onCollapse.wheelprogress.setPercentage(progressInPercentage)
    }


    override fun onProgressInPercentageChanged(
        seekBar: WaveformSeekBar?,
        percent: Float,
        fromUser: Boolean
    ) {
        if (Coordinator.isPlaying()) {

            binding.playerRemote.musicMin?.text = TimeUtils.getReadableDuration(
                ((percent * TimeUtils.getDurationOfCurrentMusic().toLong()).toLong())
            )
        }
    }

    override fun onStartTrackingTouch(seekBar: WaveformSeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: WaveformSeekBar?) {
        Coordinator.seekTo((binding.playerRemote.waveformSeekBar.progressPercent * Coordinator.currentPlayingSong?.duration!!).toInt())

    }

}
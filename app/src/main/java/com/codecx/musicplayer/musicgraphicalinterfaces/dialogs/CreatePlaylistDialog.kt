package com.codecx.musicplayer.musicgraphicalinterfaces.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.codecx.musicplayer.R
import com.codecx.musicplayer.databinding.CreatePlaylistDialogBinding
import com.codecx.musicplayer.interfaces.PassData
import com.codecx.musicplayer.repo.RoomRepository
import com.codecx.musicplayer.utils.ScreenSizeUtils


class CreatePlaylistDialog : DialogFragment() {

    lateinit var binding: CreatePlaylistDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.window?.apply {
            setBackgroundDrawable(ContextCompat.getDrawable(context!!, android.R.color.transparent))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        val view = inflater.inflate(R.layout.create_playlist_dialog, container, false)
        binding = CreatePlaylistDialogBinding.bind(view)
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {

            addPlaylistLayout.layoutParams.width = ScreenSizeUtils.getScreenWidth() * 8 / 10
            addPlaylistLayout.requestLayout()

            textField.layoutParams.width =
                addPlaylistLayout.layoutParams.width * 8 / 10
            textField.requestLayout()

            btnNegative.layoutParams.width =
                (addPlaylistLayout.layoutParams.width * 3 / 10)
            btnNegative.requestLayout()

            btnPositive.layoutParams.width =
                addPlaylistLayout.layoutParams.width * 5 / 10
            btnNegative.requestLayout()
        }

    }

    override fun onResume() {
        super.onResume()


        binding.btnPositive.setOnClickListener {

            if (binding.playlistName.text.toString().trim().isEmpty()) {
                val shake: Animation =
                    AnimationUtils.loadAnimation(this@CreatePlaylistDialog.context, R.anim.shake)
                binding.addPlaylistLayout.startAnimation(shake)
                binding.textField.error = getString(R.string.no_name_error)
            } else if (isUnique(binding.playlistName.text.toString())) {
                val shake: Animation =
                    AnimationUtils.loadAnimation(this@CreatePlaylistDialog.context, R.anim.shake)
                binding.addPlaylistLayout.startAnimation(shake)
                binding.textField.error = getString(R.string.duplicate_name_error)
            } else {
                val targetFragment = targetFragment
                val passData: PassData = targetFragment as PassData
                targetFragment.passDataToInvokingFragment(binding.playlistName.text.toString())

                this.dismiss()
            }
        }

        binding.btnNegative.setOnClickListener {
            this.dismiss()
        }
    }

    private fun isUnique(name: String): Boolean {
        for (playlist in RoomRepository.cachedPlaylistArray!!) {
            if (playlist.name == name)
                return true
        }
        return false
    }
}
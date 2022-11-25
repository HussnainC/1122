package com.codecx.musicplayer.musicgraphicalinterfaces.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.codecx.musicplayer.R
import com.codecx.musicplayer.databinding.SongDetailsDialogBinding
import com.codecx.musicplayer.musicdatabase.entities.SongModel
import com.codecx.musicplayer.utils.ScreenSizeUtils
import com.codecx.musicplayer.utils.TimeUtils

class SongDetailsDialog(val song: SongModel) : DialogFragment() {

    lateinit var binding: SongDetailsDialogBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.window?.apply {
            setBackgroundDrawable(ContextCompat.getDrawable(context!!, android.R.color.transparent))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        val view = inflater.inflate(R.layout.song_details_dialog, container, false)
        initBinding(view)

        binding.detailsFileSizeContent.text =
            getString(
                R.string.file_size,
                (((song.size)?.toFloat()?.div(1024))?.div(1024))?.toInt()
            )
        binding.detailsFileNameContent.text = song.title

        binding.detailsFileBitrateContent.text =
            if (song.bitrate == "") ""
            else getString(R.string.file_bitrate, ((song.bitrate)?.toInt()?.div(1000)).toString())
        binding.detailsFileLengthContent.text =
            song.duration?.let { TimeUtils.getReadableDuration(it) }
        binding.detailsFilePathContent.text = song.data

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.songDetails.layoutParams.width =
            (ScreenSizeUtils.getScreenWidth() * 8.5 / 10).toInt()
        binding.songDetails.requestLayout()

    }

    override fun onResume() {
        super.onResume()

        binding.btnOk.setOnClickListener {
            this.dismiss()
        }


    }

    fun initBinding(view: View) {
        binding = SongDetailsDialogBinding.bind(view)
    }
}


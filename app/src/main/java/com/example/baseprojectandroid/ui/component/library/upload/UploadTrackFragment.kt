package com.example.baseprojectandroid.ui.component.library.upload

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentUploadTrackBinding
import com.example.baseprojectandroid.extension.gone
import com.example.baseprojectandroid.extension.visible
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.ui.component.library.UploadTrackViewModel
import com.example.baseprojectandroid.utils.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UploadTrackFragment :
    BaseFragmentBinding<FragmentUploadTrackBinding, BaseViewModel>() {

    private val uploadTrackViewModel by activityViewModels<UploadTrackViewModel>()
    override fun getContentViewId() = R.layout.fragment_upload_track

    override fun initializeViews() {

    }

    override fun registerListeners() {
        dataBinding.chooseAudio.setOnClickListener {
            openAudioChooser()
        }

        dataBinding.chooseImage.setOnClickListener {
            openImageChooser()
        }

        dataBinding.btnUpload.setOnClickListener {
            hiddenKeyboard()
            uploadSong()
        }

        dataBinding.backToHome.setOnClickListener {
            removeFragment(this)
        }

        dataBinding.cancelFail.setOnClickListener {
            removeFragment(this)
        }

        dataBinding.cancelProcess.setOnClickListener {
            removeFragment(this)
        }

        dataBinding.btnCancel.setOnClickListener {
            removeFragment(this)
        }

        dataBinding.tryagainFail.setOnClickListener {
            bindViewLoadFile()
        }

        dataBinding.backToHome.setOnClickListener {
            removeFragment(this)
        }
    }

    private fun bindViewLoadFile() {
        dataBinding.layoutUpload.gone()
        dataBinding.layoutProcess.visible()
        CoroutineScope(Dispatchers.Main).launch {
            for (i in 0..100) {
                delay(300)
                if (dataBinding.progressBar.progress == 100) return@launch
                dataBinding.progressBar.progress = i
                //dataBinding.txtOpening.text = getString(R.string.mgs_open_success, "$i%")
            }
            dataBinding.layoutProcess.gone()
            dataBinding.layoutDone.visible()
        }
    }

    private fun openAudioChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/mpeg" // Filter for audio files
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(
            Intent.createChooser(intent, "Select Music"),
            REQUEST_CODE_PICK_MUSIC
        )
    }

    override fun registerObservers() {
        super.registerObservers()
        uploadTrackViewModel.currentSongFile.observe(viewLifecycleOwner) {
            it?.let { song ->
                dataBinding.nameSongUpload.text = song.name
            }
        }

        uploadTrackViewModel.currentImageFile.observe(viewLifecycleOwner) {
            it?.let { image ->
                dataBinding.imgSongUpload.text = image.name
            }
        }

        uploadTrackViewModel.uploadSongResponse.observe(viewLifecycleOwner) {
            it?.let { state ->
                state.whenSuccess {
                    Logger.d("buituyen upload track", it.data.toString())
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                }.whenFailure {
                    Toast.makeText(
                        requireContext(),
                        "Failure. Message = ${state.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }.whenLoading {
                    Toast.makeText(requireContext(), "On Going", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    uploadTrackViewModel.onImageUriSelected(requireContext(), data!!.data!!)
                }
                REQUEST_CODE_PICK_MUSIC -> {
                    uploadTrackViewModel.onSongUriSelected(requireContext(), data!!.data!!)
                }
            }
        }
    }


    private fun uploadSong() {
        uploadTrackViewModel.uploadSong(dataBinding.etTrackName.text.toString().trim())
        openFragment(UploadTrackDetailFragment())
        hiddenKeyboard()
    }

    override fun onBackPressed(): Boolean {
        removeFragment(this)
        return true
    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
        const val REQUEST_CODE_PICK_MUSIC = 102
    }
}
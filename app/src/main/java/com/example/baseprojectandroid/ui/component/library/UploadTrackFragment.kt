package com.example.baseprojectandroid.ui.component.library

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentUploadTrackBinding
import com.example.baseprojectandroid.extension.getFileName
import com.example.baseprojectandroid.extension.gone
import com.example.baseprojectandroid.extension.snackbar
import com.example.baseprojectandroid.extension.visible
import com.example.baseprojectandroid.model.AccountState
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.component.MainActivity
import com.example.baseprojectandroid.upload.UploadRequestBody
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


@AndroidEntryPoint
class UploadTrackFragment : BaseFragmentBinding<FragmentUploadTrackBinding, UploadTrackViewModel>() {

    private var selectedMusicUri: Uri? = null
    private var selectedImageUri: Uri? = null
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
            //uploadImage()
            bindViewLoadFile()
            //uploadSong()
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

    private fun uploadImage() {
        val bodyImage = getFileImage()?.let { UploadRequestBody(it, "image") }

        val imagePart = MultipartBody.Part.createFormData("file",getFileImage()!!.name ,
            "".toRequestBody(MultipartBody.FORM)
        )
        val handle = CoroutineExceptionHandler { _, e ->
            e as HttpException
            Log.d("ditcu", e.response()!!.message())
            e.printStackTrace()
        }
        val exceptionScope = CoroutineScope(SupervisorJob() + handle)
        exceptionScope.launch(handle) {
//        viewModel.viewModelScope.launch {
            viewModel.uploadImage(
                imagePart
            ).collect { state ->
                when (state) {
                    is AccountState.Loading -> {
                        withContext(Dispatchers.Main) {
                            showToast("Đang chờ xử lý. Vui lòng đợi trong giây lát")
                        }
                    }

                    is AccountState.Finished -> {
                        withContext(Dispatchers.Main) {
                            showToast("Đăng ký thành công")
                        }
                    }

                    is AccountState.Failed -> {
                        withContext(Dispatchers.Main) {
                            showToast("Đăng ký thất bại")
                        }
                    }
                }
            }
        }
    }

    private fun openAudioChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/mpeg" // Filter for audio files
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(intent, "Select Music"), REQUEST_CODE_PICK_MUSIC)
    }

    override fun initializeData() {

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
                    selectedImageUri = data?.data
                    dataBinding.imgSongUpload.text = data?.data?.let { getFileNameFromUri(it) }
                    //image_view.setImageURI(selectedImageUri)
                }
                REQUEST_CODE_PICK_MUSIC -> {
                    selectedMusicUri = data?.data
                    dataBinding.nameSongUpload.text = data?.data?.let { getFileNameFromUri(it) }
                }
            }
        }
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        uri.let {
            val cursor = requireContext().contentResolver?.query(it, null, null, null, null)
            cursor?.use { cursor ->
                if (cursor.moveToFirst()) {
                    return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
                }
            }
        }
        return null
    }

    private fun getFileImage(): File? {
        if (selectedImageUri == null) {
            view?.snackbar("Select an Image First")
            return null
        }

        val parcelFileDescriptor = context?.contentResolver?.openFileDescriptor(selectedImageUri!!, "r", null) ?: return null
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(requireContext().cacheDir, requireContext().contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        return file
    }

    private fun getFileAudio(): File? {
        if (selectedMusicUri == null) {
            view?.snackbar("Select an Image First")
            return null
        }

        val parcelFileDescriptor = context?.contentResolver?.openFileDescriptor(selectedMusicUri!!, "r", null) ?: return null
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(requireContext().cacheDir, requireContext().contentResolver.getFileName(selectedMusicUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        return file
    }

    private fun uploadSong() {
        val bodyImage = getFileImage()?.let { UploadRequestBody(it, "image") }
        val bodyAudio = getFileAudio()?.let { UploadRequestBody(it, "song") }
        bodyImage?.let {
            MultipartBody.Part.createFormData("image", getFileImage()?.absolutePath,
                it
            )
        }?.let {
            bodyAudio?.let { it1 ->
                MultipartBody.Part.createFormData(
                    "song",
                    getFileAudio()?.name,
                    it1
                )
            }?.let { it2 -> viewModel.uploadSong(name = "thanhxuan", image = it, song = it2, category = 3, creator = 3) }
        }
    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
        const val REQUEST_CODE_PICK_MUSIC = 102
    }
}
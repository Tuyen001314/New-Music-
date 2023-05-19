package com.example.baseprojectandroid.ui.component.home

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentGettingBinding
import com.example.baseprojectandroid.databinding.FragmentHomeBinding
import com.example.baseprojectandroid.model.DownloadState
import com.example.baseprojectandroid.ui.base.BaseFragment
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.component.getting.GettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.File

@AndroidEntryPoint
class HomeFragment: BaseFragmentBinding<FragmentHomeBinding, HomeViewModel>() {
    override fun getContentViewId(): Int = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun registerListeners() {
        dataBinding.buttonDown.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val flow = context?.let { it1 ->
                    downloadFile(
                        "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/music%2F%C6%AFng%20Qu%C3%A1%20Ch%E1%BB%ABng%20-%20AMEE.mp3?alt=media&token=3201a2c5-fbd4-4924-940e-dbadb6c91bb0",
                        it1, "ungquachung1.mp3"
                    )
                }
                if (flow != null) {
                    flow.collect { state ->
                        when (state) {
                            is DownloadState.Downloading -> {
                                withContext(Dispatchers.Main) {
                                    bindViewProcess(state.progress)
                                }
                            }
                            is DownloadState.Failed -> {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            is DownloadState.Finished -> {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "done",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    dataBinding.processTxt.text = "done"
                                }
                            }
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }

    private fun ResponseBody.saveFile(filePath: String): Flow<DownloadState> {
        return flow {
            emit(DownloadState.Downloading(0))
            val destinationFile = File(filePath)
            try {
                byteStream().use { inputStream ->
                    destinationFile.outputStream().use { outputStream ->
                        val totalBytes = contentLength()
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var progressBytes = 0L
                        var bytes = inputStream.read(buffer)
                        while (bytes >= 0) {
                            outputStream.write(buffer, 0, bytes)
                            progressBytes += bytes
                            bytes = inputStream.read(buffer)
                            emit(DownloadState.Downloading(((progressBytes * 100) / totalBytes).toInt()))
                        }
                    }
                }
                emit(DownloadState.Finished)
            } catch (e: Exception) {
                emit(DownloadState.Failed(e))
            }
        }
            .flowOn(Dispatchers.IO).distinctUntilChanged()
    }

    private fun downloadFile(url: String, context: Context, filename: String): Flow<DownloadState>? {
        return try {
            val path = getDirFile().path + "/" + filename
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            if (response.body != null && response.body!!.contentLength() > 0) {
                response.body?.saveFile(path)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getDirFile(): File {
        val dir = File(Environment.getExternalStorageDirectory(), "MusicFile")
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir
    }

    private fun bindViewProcess(progress: Int) {
        dataBinding.progressBar.progress = progress
        dataBinding.processTxt.text = "$progress %"
    }

}
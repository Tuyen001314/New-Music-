package com.example.baseprojectandroid.ui.component.download

import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.ItemYourLibraryBinding
import com.example.baseprojectandroid.extension.saveFile
import com.example.baseprojectandroid.model.DownloadState
import com.example.baseprojectandroid.model.Music
import com.example.baseprojectandroid.ui.base.SimpleAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

class DownloadAdapter(context: Context) :
    SimpleAdapter<Music, DownloadAdapter.ViewHolder, ItemYourLibraryBinding>(context) {

    val listData = mutableListOf<Music>()

    private var onClick: ((View, Int, Music) -> Unit)? = null

    override fun getItemViewId(viewType: Int): Int = R.layout.item_your_library

    override fun getViewHolder(viewType: Int, dataBinding: ItemYourLibraryBinding) =
        ViewHolder(dataBinding)

    override fun createViewBinding(parent: ViewGroup): ItemYourLibraryBinding {
        return ItemYourLibraryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DownloadAdapter.ViewHolder {
        dataBinding = ItemYourLibraryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return super.onCreateViewHolder(parent, viewType)
    }

    //----------------------------------------------------------------------------------------------
    inner class ViewHolder(var dataBinding: ItemYourLibraryBinding) :
        SimpleAdapter.ViewHolder(dataBinding) {
        init {

        }

        override fun bindViews(position: Int) {
            val item = getItem(position)
            dataBinding.nameSong.text = item.name

            dataBinding.appCompatImageView.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val flow = downloadFile(
                        "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/music%2F%C6%AFng%20Qu%C3%A1%20Ch%E1%BB%ABng%20-%20AMEE.mp3?alt=media&token=3201a2c5-fbd4-4924-940e-dbadb6c91bb0",
                        context,
                        "ungquachung1${position}.mp3"
                    )
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
                                            context, "error", Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                is DownloadState.Finished -> {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context, "done", Toast.LENGTH_SHORT
                                        ).show()
                                        dataBinding.processTxt.text = "done"
                                    }
                                }
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context, "error", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        private fun bindViewProcess(progress: Int) {
            dataBinding.progressBar3.progress = progress
            dataBinding.processTxt.text = "$progress %"
        }

        private fun downloadFile(
            url: String, context: Context, filename: String
        ): Flow<DownloadState>? {
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
    }

}
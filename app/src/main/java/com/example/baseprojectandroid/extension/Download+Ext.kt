package com.example.baseprojectandroid.extension

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.baseprojectandroid.model.DownloadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.File


fun downloadFile(
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

fun getDirFile(): File {
    val dir = File(Environment.getExternalStorageDirectory(), "MusicFile")
    if (!dir.exists()) {
        dir.mkdir()
    }
    return dir
}


 fun ResponseBody.saveFile(filePath: String): Flow<DownloadState> {
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
            Log.d("buituyen", e.message.toString())
            emit(DownloadState.Failed(e))
        }
    }.flowOn(Dispatchers.IO).distinctUntilChanged()
}
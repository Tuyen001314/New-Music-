package com.example.baseprojectandroid.extension

import android.util.Log
import com.example.baseprojectandroid.model.DownloadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import java.io.File


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
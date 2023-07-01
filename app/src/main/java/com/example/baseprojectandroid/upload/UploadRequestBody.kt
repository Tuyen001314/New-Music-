package com.example.baseprojectandroid.upload

import android.os.Handler
import android.os.Looper
import com.example.baseprojectandroid.ui.component.home.HomeFragment
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import retrofit2.http.Multipart
import java.io.File
import java.io.FileInputStream

class UploadRequestBody(
    private val file: File,
    private val onProcess: (process: Float) -> Unit
) : RequestBody() {

    override fun contentType() = MultipartBody.FORM

    override fun writeTo(sink: BufferedSink) {
        val length = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val fileInputStream = FileInputStream(file)
        var uploaded = 0L
        fileInputStream.use { inputStream ->
            var read: Int
            val handler = Handler(Looper.getMainLooper())
            while (inputStream.read(buffer).also { read = it } != -1) {
                uploaded += read
                onProcess(uploaded / length.toFloat())
                sink.write(buffer, 0, read)
            }
        }
    }

    override fun contentLength(): Long = file.length()
}
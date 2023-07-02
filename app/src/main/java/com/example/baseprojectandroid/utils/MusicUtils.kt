package com.example.baseprojectandroid.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat

object MusicUtils {
    fun checkPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result: Int =
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            val result1: Int =
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }

    @JvmStatic
    @SuppressLint("WrongConstant")
    fun shareMusic(activity: Activity, uri: Uri) {
        try {
            val intent = ShareCompat.IntentBuilder.from(activity)
                .setType(activity.contentResolver.getType(uri)).setStream(uri).intent
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val createChooser = Intent.createChooser(intent, "Sharing File...")
            createChooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (createChooser.resolveActivity(activity.packageManager) != null) {
                activity.startActivity(createChooser)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(activity, "Can't share this file", Toast.LENGTH_LONG).show()
        }
    }
}
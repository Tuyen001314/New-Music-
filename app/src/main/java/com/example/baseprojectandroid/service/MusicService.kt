package com.example.baseprojectandroid.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import androidx.media3.exoplayer.ExoPlayer

class MusicService: Service() {
    private var exoPlayer = ExoPlayer.Builder(this)

    internal class CommandHandler: Handler() {
        override fun handleMessage(msg: Message) {

        }
    }
    private lateinit var mMessage: Messenger

    override fun onBind(intent: Intent?): IBinder? {
        mMessage = Messenger(CommandHandler())
        return mMessage.binder
    }

    companion object {
    }
}
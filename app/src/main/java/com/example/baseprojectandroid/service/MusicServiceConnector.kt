package com.example.baseprojectandroid.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.model.SongState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.xml.transform.ErrorListener

class MusicServiceConnector constructor(
    context: Context
) {
    private var service: MusicService? = null
    private var isBound = false

    var currentSongState: StateFlow<SongState>? = null

    var onServiceConnected: () -> Unit = {}

    private val listOnServiceConnectedCallback = mutableListOf<OnServiceConnected>()

    /**
     * Class for interacting with the main interface of the service.
     */
    private val mConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            this@MusicServiceConnector.service = (service as MusicService.MusicBinder).getService()
            currentSongState = this@MusicServiceConnector.service!!.currentState
            onServiceConnected()
            Log.d("fdsfa", "onServiceConnected: ")
            isBound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected&mdash;that is, its process crashed.
            service = null
            isBound = false
            Log.d("fdsfa", "onServiceDisconnected: ")
            currentSongState = null
        }
    }

    init {
        bindToService(context)
    }

    fun bindToService(context: Context) {
        Intent(context, MusicService::class.java).also {
            context.bindService(it, mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun pauseOrPlay() {
        service!!.pauseOrPlay()
    }

    fun play(song: Song) {
        service!!.play(song)
    }

    fun isConnected() = isBound

    fun addOnConnectedListener(listener: OnServiceConnected) {
        if (isConnected()) {
            listener.onConnected()
        }
        listOnServiceConnectedCallback.add(listener)
    }

    fun removeOnConnectedListener(listener: OnServiceConnected) {
        listOnServiceConnectedCallback.remove(listener)
    }

    interface OnServiceConnected {
        fun onConnected()
    }

    fun makeSureInit(){}
}
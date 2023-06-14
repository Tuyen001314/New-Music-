package com.example.baseprojectandroid.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.example.baseprojectandroid.model.Playlist
import com.example.baseprojectandroid.model.Position
import com.example.baseprojectandroid.model.Song
import kotlinx.coroutines.flow.StateFlow

class MusicServiceConnector constructor(
    context: Context
): MusicController {
    private var service: MusicService? = null
    private var isBound = false

    val currentPlayerState: StateFlow<PlayerState>? get() = service?.currentPlayerState
    val currentSongState: StateFlow<Song>? get() = service?.currentSongFlow
    val currentSongPosition: StateFlow<Position>? get() = service?.currentSongPosition
    val currentPlaylist: StateFlow<Playlist>? get() = service?.currentPlaylist

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
            listOnServiceConnectedCallback.forEach { it.onConnected() }
            Log.d("fdsfa", "onServiceConnected: ")

            isBound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected&mdash;that is, its process crashed.
            service = null
            isBound = false
            Log.d("fdsfa", "onServiceDisconnected: ")
        }
    }

    init {
        bindToService(context)
    }

    fun bindToService(context: Context) {
        Intent(context, MusicService::class.java).also {
            context.bindService(it, mConnection, Context.BIND_AUTO_CREATE)
        }

//        val sessionToken = SessionToken(context, ComponentName(context,  MusicService::class.java))
//         MediaController.Builder(context, sessionToken)
//            .buildAsync()
//            .addListener({
//                listOnServiceConnectedCallback.forEach { it.onConnected() }
//            }, Executors.newFixedThreadPool(4))
    }

    override fun pauseOrPlay() {
        service!!.pauseOrPlay()
    }

    override fun play(song: Song) {
        service!!.play(song)
    }

    override fun play(playlist: Playlist, startSongIndex: Int) = service!!.play(playlist, startSongIndex)
    override fun nextSong() {
        service!!.nextSong()
    }

    override fun prevSong() {
        service!!.prevSong()
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

    override fun updateCurrentPosition() = service!!.updateCurrentPosition()

    override fun updateCurrentSongOfPlaylist(index: Int) = service!!.updateCurrentSongOfPlaylist(index)

    override fun updateCurrentPlaylist() {

    }

    override fun updatePosition(process: Int) {
        service!!.updatePosition(process)
    }

    override fun toggleShuffle() {
        service!!.toggleShuffle()
    }

    override fun toggleRepeat() {
        service!!.toggleRepeat()
    }

    interface OnServiceConnected {
        fun onConnected()
    }
}
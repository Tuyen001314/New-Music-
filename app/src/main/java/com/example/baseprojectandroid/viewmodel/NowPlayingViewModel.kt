package com.example.baseprojectandroid.viewmodel

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.model.SongState
import com.example.baseprojectandroid.repository.SongRepository
import com.example.baseprojectandroid.service.MusicService
import com.example.baseprojectandroid.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val songRepository: SongRepository
): BaseViewModel() {
    private var service: MusicService? = null
    private var isBound = false

    var currentSongState: StateFlow<SongState>? = null

    var onServiceConnected : () -> Unit = {}

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
            this@NowPlayingViewModel.service = (service as MusicService.MusicBinder).getService()
            currentSongState = this@NowPlayingViewModel.service!!.currentState
            onServiceConnected()
            isBound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected&mdash;that is, its process crashed.
            service = null
            isBound = false
            currentSongState = null
        }
    }

    init {
        bindToService(context)
    }

    private fun bindToService(context: Context) {
        Intent(context, MusicService::class.java).also {
            context.bindService(it, mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun unbindService() {

    }

    fun pauseOrPlay() {
        service!!.pauseOrPlay()
    }

    fun play(song: Song) {
        service!!.play(song)
    }
}
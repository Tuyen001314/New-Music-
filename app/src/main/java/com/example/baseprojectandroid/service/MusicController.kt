package com.example.baseprojectandroid.service

import com.example.baseprojectandroid.model.Playlist
import com.example.baseprojectandroid.model.Song

interface MusicController {
    fun pauseOrPlay()

    fun play(song: Song)

    fun play(playlist: Playlist, startSongIndex: Int = 0)

    fun nextSong()

    fun prevSong()

    fun updateCurrentPosition()

    fun updateCurrentSongOfPlaylist(index: Int)

    fun updateCurrentPlaylist()

    fun updatePosition(process: Int)

    fun toggleShuffle()

    fun toggleRepeat()
}
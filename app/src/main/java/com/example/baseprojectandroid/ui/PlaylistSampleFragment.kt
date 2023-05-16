package com.example.baseprojectandroid.ui

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentPlaylistSampleBinding
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.ui.common.SongStateItem
import com.example.baseprojectandroid.viewmodel.NowPlayingViewModel
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class PlaylistSampleFragment : BaseFragmentBinding<FragmentPlaylistSampleBinding, BaseViewModel>() {
    private val nowPlayingViewModel by activityViewModels<NowPlayingViewModel>()
    private val playlistSampleViewModel by activityViewModels<PlaylistSampleViewModel>()


    private val songAdapter = GroupieAdapter()
    override fun getContentViewId(): Int {
        return R.layout.fragment_playlist_sample
    }

    override fun initializeViews() {
        super.initializeViews()
        dataBinding.musicRv.adapter = songAdapter
    }

    override fun registerListeners() {
        super.registerListeners()
        songAdapter.setOnItemClickListener {item, v ->
            nowPlayingViewModel.play(
            (item as SongStateItem).songState.song
            )
        }
    }

    override fun registerObservers() {
        super.registerObservers()
        lifecycleScope.launch(Dispatchers.Default) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playlistSampleViewModel.allSongs.collect {
                    withContext(Dispatchers.Main) {
                        songAdapter.updateAsync(it.map { SongStateItem(it) })
                    }
                }
            }
        }
    }

}
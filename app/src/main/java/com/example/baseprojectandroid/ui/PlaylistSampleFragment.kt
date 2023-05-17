package com.example.baseprojectandroid.ui

import androidx.fragment.app.activityViewModels
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentPlaylistSampleBinding
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.ui.common.SongStateItem
import com.example.baseprojectandroid.viewmodel.NowPlayingViewModel
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint

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
        dataBinding.musicRv.apply {
            adapter = songAdapter
            itemAnimator = null
        }
//        childFragmentManager.beginTransaction()
//            .add(R.id.container, NowPlayingFragment(), null)
//            .commit()
    }

    override fun registerListeners() {
        super.registerListeners()
        songAdapter.setOnItemClickListener { item, v ->
            nowPlayingViewModel.play(
                (item as SongStateItem).songState.song
            )
        }
    }

    override fun registerObservers() {
        super.registerObservers()
        playlistSampleViewModel.allSongs.observe(viewLifecycleOwner) {
            songAdapter.update(it.map { SongStateItem(it.copy()) })
        }
    }

}
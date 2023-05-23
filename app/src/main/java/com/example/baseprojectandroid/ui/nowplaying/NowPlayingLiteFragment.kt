package com.example.baseprojectandroid.ui.nowplaying

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.NowPlayingLiteFragmentBinding
import com.example.baseprojectandroid.model.Position
import com.example.baseprojectandroid.model.SongState
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.viewmodel.NowPlayingViewModel
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NowPlayingLiteFragment : BaseFragmentBinding<NowPlayingLiteFragmentBinding, BaseViewModel>() {
    private val nowPlayingViewModel by activityViewModels<NowPlayingViewModel>()

    private val currentPlaylistAdapter = GroupieAdapter()
    override fun getContentViewId(): Int {
        return R.layout.now_playing_lite_fragment
    }

    override fun initializeViews() {
        super.initializeViews()
        dataBinding.vp2CurrentSong.adapter = currentPlaylistAdapter
    }

    override fun registerObservers() {
        super.registerObservers()
        lifecycleScope.launch(Dispatchers.Default) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                nowPlayingViewModel.uiState.collect { uiState ->
                    lifecycleScope.launch(Dispatchers.Main) {
                        uiState.songState.getValueIfNotHandle(viewLifecycleOwner) { songState ->
                            updateNowPlayingSong(songState)
                            dataBinding.vp2CurrentSong.currentItem = uiState.currentSongIndexInPlayingPlaylist
                        }

                        uiState.currentPlaylist.getValueIfNotHandle(viewLifecycleOwner) { playlist ->
                            currentPlaylistAdapter.update(
                                playlist.songs.map {
                                    SongInNowPlayingViewPagerItem(it.copy())
                                }
                            )
                        }

                        updatePosition(uiState.currentPosition)
                    }
                }
            }
        }
    }

    private fun updateNowPlayingSong(songState: SongState) {
//        dataBinding.tvSongName.text = songState.song.name
        dataBinding.btPauseResume.setImageResource(
            when (songState.state) {
                SongState.STATE_PLAYING -> R.drawable.ic_pause
                else -> R.drawable.ic_triangle
            }
        )
        dataBinding.ivThumb.apply {
            Glide.with(this)
                .load(songState.song.thumbnailUrl)
                .override(100, 100)
                .transform(CenterCrop(), RoundedCorners(20))
                .into(this)
        }
    }

    private fun updatePosition(position: Position) {
        dataBinding.songProgress.progress =
            ((position.currentIndex.toFloat() / position.duration) * 100).toInt()
    }

    override fun registerListeners() {
        super.registerListeners()
        dataBinding.btPauseResume.setOnClickListener {
            nowPlayingViewModel.pauseOrPlay()
        }

        dataBinding.vp2CurrentSong.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var isUserSwipe = false
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("checkvp2", "onPageSelected: ${dataBinding.vp2CurrentSong.isFakeDragging}")
                if (isUserSwipe) {
                 nowPlayingViewModel.updateCurrentSongOfPlaylist(position)
                }
                isUserSwipe = false
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    isUserSwipe = true
                }
            }
        })
    }
}
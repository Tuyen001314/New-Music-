package com.example.baseprojectandroid.ui.component.home

import android.util.Size
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentHomeBinding
import com.example.baseprojectandroid.extension.dp
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.common.CommonSongVerticalItem
import com.example.baseprojectandroid.ui.common.CommonSongVerticalItem.ImageShape
import com.example.baseprojectandroid.ui.common.CommonSongVerticalItem.Modifier
import com.example.baseprojectandroid.ui.common.HorizontallyGroup
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragmentBinding<FragmentHomeBinding, HomeViewModel>() {
    private val recentGroup = HorizontallyGroup()
    private val mainAdapter = GroupieAdapter()
    override fun getContentViewId(): Int = R.layout.fragment_home

    override fun initializeViews() {
        super.initializeViews()
        dataBinding.rvContentMain.adapter = mainAdapter
        dataBinding.rvContentMain.layoutManager = LinearLayoutManager(requireContext())
        mainAdapter.add(recentGroup)
    }

    override fun registerObservers() {
        super.registerObservers()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.recentSong.collect {
                        recentGroup.update(it.map {
                            CommonSongVerticalItem(
                                song = it,
                                modifier = Modifier.new()
                                    .size(Size(100.dp, 100.dp))
                                    .shape(ImageShape.CIRCLE)
                            ) {

                            }
                        })
                    }
                }
            }
        }
    }
}
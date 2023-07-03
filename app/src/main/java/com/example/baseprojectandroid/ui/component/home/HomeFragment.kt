package com.example.baseprojectandroid.ui.component.home

import android.util.Size
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentHomeBinding
import com.example.baseprojectandroid.extension.dp
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.common.CommonHorizontalItem
import com.example.baseprojectandroid.ui.common.CommonHorizontalItem.ImageShape
import com.example.baseprojectandroid.ui.common.CommonHorizontalItem.Modifier
import com.example.baseprojectandroid.ui.common.CommonVerticalItem
import com.example.baseprojectandroid.ui.common.HeaderItem
import com.example.baseprojectandroid.ui.common.HorizontallyGroup
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragmentBinding<FragmentHomeBinding, HomeViewModel>() {
    private val recentGroup = HorizontallyGroup()
    private val downloadSection = Section(HeaderItem("All Song"))
    private val mainAdapter = GroupieAdapter()
    override fun getContentViewId(): Int = R.layout.fragment_home

    override fun initializeViews() {
        super.initializeViews()
        dataBinding.rvContentMain.apply {
            adapter = mainAdapter
            dataBinding.rvContentMain.layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
        }
        initMainAdapter()
    }

    override fun registerObservers() {
        super.registerObservers()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.recentSong.collect {
                        recentGroup.update(it.map {
                            CommonHorizontalItem(
                                song = it,
                                modifier = Modifier.new()
                                    .size(Size(100.dp, 100.dp))
                                    .shape(ImageShape.CIRCLE)
                            ) {
                            }
                        })
                    }
                }

                launch {
                    viewModel.downloadSong.collect {
                        downloadSection.update(it.map {
                            CommonVerticalItem(it) {
                                viewModel.playSong(it.song)
                            }
                        })
                    }
                }

            }
        }
    }

    private fun initMainAdapter() {
        // add recent
        mainAdapter.add(recentGroup)

        //add downloaded list
        mainAdapter.add(downloadSection)
    }
}
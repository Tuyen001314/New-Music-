package com.example.baseprojectandroid.ui.component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.baseprojectandroid.databinding.ActivityMainBinding
import com.example.baseprojectandroid.ui.base.BaseFragmentPagerAdapter
import com.example.baseprojectandroid.ui.component.home.HomeFragment
import com.example.baseprojectandroid.ui.component.library.UploadTrackFragment
import com.example.baseprojectandroid.ui.component.library.YourLibraryFragment
import com.example.baseprojectandroid.ui.component.search.SearchFragment
import com.example.baseprojectandroid.ui.event.HideDetailPlayer
import com.example.baseprojectandroid.ui.event.ShowDetailPlayer
import com.example.baseprojectandroid.ui.nowplaying.NowPlayingFragment
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var adapter: BaseFragmentPagerAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewPager()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    private fun setupViewPager() {
        adapter = BaseFragmentPagerAdapter(supportFragmentManager, lifecycle)
        adapter.add(HomeFragment::class)
            .add(SearchFragment::class)
            .add(YourLibraryFragment::class)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = adapter
        binding.bottomBarView.setWithViewPager(binding.viewPager)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun handleShowDetailPlayer(event: ShowDetailPlayer) {
        supportFragmentManager.commit {
            add(android.R.id.content, NowPlayingFragment(), NowPlayingFragment.TAG)
        }
    }

    @Subscribe
    fun handleHideDetailPlayer(event: HideDetailPlayer) {
        supportFragmentManager.commit {
            val nowPlayingFragment =
                supportFragmentManager.findFragmentByTag(NowPlayingFragment.TAG)
            nowPlayingFragment?.let { remove(it) }
        }
    }

}
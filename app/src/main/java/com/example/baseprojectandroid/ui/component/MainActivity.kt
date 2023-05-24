package com.example.baseprojectandroid.ui.component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.ActivityMainBinding
import com.example.baseprojectandroid.service.MusicServiceConnector
import com.example.baseprojectandroid.ui.base.BaseFragmentPagerAdapter
import com.example.baseprojectandroid.ui.component.home.HomeFragment
import com.example.baseprojectandroid.ui.component.library.YourLibraryFragment
import com.example.baseprojectandroid.ui.component.library.profile.ProfileFragment
import com.example.baseprojectandroid.ui.component.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var musicServiceConnector: MusicServiceConnector
    private lateinit var adapter: BaseFragmentPagerAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewPager()
        musicServiceConnector.bindToService(this)
    }

    private fun setupViewPager() {
        adapter = BaseFragmentPagerAdapter(supportFragmentManager, lifecycle)
        adapter.add(HomeFragment::class)
            .add(SearchFragment::class)
            .add(ProfileFragment::class)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = adapter
        binding.bottomBarView.setWithViewPager(binding.viewPager)
    }
}
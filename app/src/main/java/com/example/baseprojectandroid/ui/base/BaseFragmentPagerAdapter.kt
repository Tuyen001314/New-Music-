package com.example.baseprojectandroid.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.reflect.KClass

class BaseFragmentPagerAdapter(fm: FragmentManager, lifeCircle: Lifecycle) :
    FragmentStateAdapter(fm, lifeCircle) {

    private var tabLayoutMediator: TabLayoutMediator? = null

    private val fragmentList = mutableListOf<FragmentData>()

    fun add(fragment: Fragment, title: String? = null): BaseFragmentPagerAdapter {
        fragmentList.add(FragmentData(title, fragment.javaClass, fragment))
        return this
    }

    fun <T : Fragment> add(clazz: KClass<T>, title: String? = null): BaseFragmentPagerAdapter {
        fragmentList.add(FragmentData(title, clazz.java, null))
        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Fragment> findFragment(position: Int): T? {
        val item = fragmentList[position]
        var fg = item.fragment as? T
        if (fg == null) {
            fg = item.clazz.newInstance() as? T
        }
        item.fragment = fg
        return fg
    }

    private fun getPageTitle(position: Int): CharSequence? {
        return fragmentList[position].title
    }

    val fragments get() = fragmentList.map { it.fragment }
    val titles get() = fragmentList.map { it.title ?: "" }

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = findFragment(position)!!

    fun setEnableTitle(viewPager2: ViewPager2, tabLayout: TabLayout) {
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = getPageTitle(position)
        }
        tabLayoutMediator!!.attach()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        tabLayoutMediator?.detach()
        fragmentList.clear()
    }

    override fun onViewDetachedFromWindow(holder: FragmentViewHolder) {
        super.onViewDetachedFromWindow(holder)
    }

    data class FragmentData(
        var title: String? = null, val clazz: Class<out Fragment>, var fragment: Fragment? = null
    )
}
package com.akexorcist.roundcornerprogressbar.example

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.akexorcist.roundcornerprogressbar.example.fragment.*

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> SimpleDemoFragment.newInstance()
        1 -> CenteredDemoFragment.newInstance()
        2 -> IconDemoFragment.newInstance()
        3 -> TextDemoFragment.newInstance()
        4 -> IndeterminateDemoFragment.newInstance()
        else -> SimpleDemoFragment.newInstance()
    }
}

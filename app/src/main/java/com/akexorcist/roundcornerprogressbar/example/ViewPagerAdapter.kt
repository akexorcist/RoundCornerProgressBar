package com.akexorcist.roundcornerprogressbar.example

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.akexorcist.roundcornerprogressbar.example.fragment.CenteredFragment
import com.akexorcist.roundcornerprogressbar.example.fragment.SimpleFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> SimpleFragment.newInstance()
        1 -> CenteredFragment.newInstance()
        else -> SimpleFragment.newInstance()
    }
}

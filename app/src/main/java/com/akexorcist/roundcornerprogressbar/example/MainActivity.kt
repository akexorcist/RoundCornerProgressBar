package com.akexorcist.roundcornerprogressbar.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akexorcist.roundcornerprogressbar.example.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(
                when (position) {
                    0 -> R.string.tab_round_corner_progress_bar
                    1 -> R.string.tab_centered_round_corner_progress_bar
                    2 -> R.string.tab_icon_round_corner_progress_bar
                    3 -> R.string.tab_text_round_corner_progress_bar
                    4 -> R.string.tab_indeterminate_round_corner_progress_bar
                    else -> R.string.blank
                }
            )
        }.attach()
    }
}

package com.akexorcist.roundcornerprogressbar.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
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

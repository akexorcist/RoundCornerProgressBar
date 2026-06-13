package com.akexorcist.roundcornerprogressbar.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.akexorcist.roundcornerprogressbar.example.DemoViewModel
import com.akexorcist.roundcornerprogressbar.example.R
import com.akexorcist.roundcornerprogressbar.example.ViewPagerAdapter
import com.akexorcist.roundcornerprogressbar.example.databinding.FragmentViewDemoBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class ViewDemoFragment : Fragment() {
    private var binding: FragmentViewDemoBinding? = null
    private val viewModel: DemoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentViewDemoBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = binding ?: return

        binding.viewPager.adapter = ViewPagerAdapter(this)
        binding.viewPager.setCurrentItem(viewModel.selectedTab.value, false)
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

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.selectTab(position)
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedTab.collect { index ->
                if (binding.viewPager.currentItem != index) {
                    binding.viewPager.setCurrentItem(index, false)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

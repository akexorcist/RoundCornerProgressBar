package com.akexorcist.roundcornerprogressbar.example

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.commit
import com.akexorcist.roundcornerprogressbar.example.compose.DemoModeToggle
import com.akexorcist.roundcornerprogressbar.example.compose.DemoTheme
import com.akexorcist.roundcornerprogressbar.example.databinding.ActivityMainBinding
import com.akexorcist.roundcornerprogressbar.example.fragment.ComposeDemoFragment
import com.akexorcist.roundcornerprogressbar.example.fragment.ViewDemoFragment

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var isViewMode by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
            }
            WindowInsetsCompat.CONSUMED
        }

        isViewMode = savedInstanceState?.getBoolean(STATE_VIEW_MODE) ?: false

        if (savedInstanceState == null) {
            val composeFragment = ComposeDemoFragment()
            val viewFragment = ViewDemoFragment()
            supportFragmentManager.commit {
                add(R.id.fragmentContainer, composeFragment, TAG_COMPOSE)
                add(R.id.fragmentContainer, viewFragment, TAG_VIEW)
                if (isViewMode) hide(composeFragment) else hide(viewFragment)
            }
        }

        binding.floatingToggle.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )
        binding.floatingToggle.setContent {
            DemoTheme {
                DemoModeToggle(
                    isViewMode = isViewMode,
                    onModeChange = ::switchMode,
                )
            }
        }
    }

    private fun switchMode(viewMode: Boolean) {
        if (isViewMode == viewMode) return
        isViewMode = viewMode
        val compose = supportFragmentManager.findFragmentByTag(TAG_COMPOSE) ?: return
        val view = supportFragmentManager.findFragmentByTag(TAG_VIEW) ?: return
        supportFragmentManager.commit {
            if (viewMode) {
                show(view)
                hide(compose)
            } else {
                show(compose)
                hide(view)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_VIEW_MODE, isViewMode)
    }

    companion object {
        private const val TAG_VIEW = "view_demo"
        private const val TAG_COMPOSE = "compose_demo"
        private const val STATE_VIEW_MODE = "view_mode"
    }
}

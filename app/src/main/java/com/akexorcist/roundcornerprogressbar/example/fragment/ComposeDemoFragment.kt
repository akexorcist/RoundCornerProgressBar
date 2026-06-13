package com.akexorcist.roundcornerprogressbar.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.akexorcist.roundcornerprogressbar.example.DemoViewModel
import com.akexorcist.roundcornerprogressbar.example.R
import com.akexorcist.roundcornerprogressbar.example.compose.CenteredDemoScreen
import com.akexorcist.roundcornerprogressbar.example.compose.DemoTheme
import com.akexorcist.roundcornerprogressbar.example.compose.IconDemoScreen
import com.akexorcist.roundcornerprogressbar.example.compose.IndeterminateDemoScreen
import com.akexorcist.roundcornerprogressbar.example.compose.SimpleDemoScreen
import com.akexorcist.roundcornerprogressbar.example.compose.TextDemoScreen
import kotlinx.coroutines.launch

class ComposeDemoFragment : Fragment() {
    private val viewModel: DemoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            DemoTheme {
                val selectedTab by viewModel.selectedTab.collectAsState()
                ComposeDemo(
                    selectedTab = selectedTab,
                    onTabSelected = viewModel::selectTab,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ComposeDemo(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
) {
    val tabs = listOf(
        stringResource(R.string.tab_round_corner_progress_bar),
        stringResource(R.string.tab_centered_round_corner_progress_bar),
        stringResource(R.string.tab_icon_round_corner_progress_bar),
        stringResource(R.string.tab_text_round_corner_progress_bar),
        stringResource(R.string.tab_indeterminate_round_corner_progress_bar),
    )
    val pagerState = rememberPagerState(initialPage = selectedTab, pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(selectedTab) {
        if (pagerState.currentPage != selectedTab) {
            pagerState.scrollToPage(selectedTab)
        }
    }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect(onTabSelected)
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = { Text(text = stringResource(R.string.app_name)) })
                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    edgePadding = 0.dp,
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                            text = { Text(text = title) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) { page ->
            when (page) {
                0 -> SimpleDemoScreen()
                1 -> CenteredDemoScreen()
                2 -> IconDemoScreen()
                3 -> TextDemoScreen()
                else -> IndeterminateDemoScreen()
            }
        }
    }
}

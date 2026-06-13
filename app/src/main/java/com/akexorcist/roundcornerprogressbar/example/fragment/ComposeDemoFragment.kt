package com.akexorcist.roundcornerprogressbar.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
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
            DemoTabRow(
                tabs = tabs,
                selectedIndex = pagerState.currentPage,
                onTabSelected = { scope.launch { pagerState.animateScrollToPage(it) } },
            )
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

private class TabMetric(
    val left: Float = 0f,
    val width: Float = 0f,
    val textWidth: Float = 0f,
)

@Composable
private fun DemoTabRow(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    val metrics = remember(tabs.size) { mutableStateListOf(*Array(tabs.size) { TabMetric() }) }

    Column {
        Box(modifier = Modifier.horizontalScroll(scrollState)) {
            Column {
                Row {
                    tabs.forEachIndexed { index, title ->
                        val selected = index == selectedIndex
                        Box(
                            modifier = Modifier
                                .onGloballyPositioned { coordinates ->
                                    val left = coordinates.positionInParent().x
                                    val width = coordinates.size.width.toFloat()
                                    val current = metrics[index]
                                    if (current.left != left || current.width != width) {
                                        metrics[index] = TabMetric(left, width, current.textWidth)
                                    }
                                }
                                .clickable { onTabSelected(index) }
                                .widthIn(min = 72.dp)
                                .height(48.dp)
                                .padding(horizontal = 13.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleSmall,
                                color = if (selected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                                onTextLayout = { result ->
                                    val current = metrics[index]
                                    val textWidth = result.size.width.toFloat()
                                    if (current.textWidth != textWidth) {
                                        metrics[index] = TabMetric(current.left, current.width, textWidth)
                                    }
                                },
                            )
                        }
                    }
                }
                val selectedMetric = metrics.getOrElse(selectedIndex) { TabMetric() }
                val indicatorLeft by animateDpAsState(
                    targetValue = with(density) {
                        (selectedMetric.left + (selectedMetric.width - selectedMetric.textWidth) / 2f).toDp()
                    },
                    label = "tabIndicatorLeft",
                )
                val indicatorWidth by animateDpAsState(
                    targetValue = with(density) { selectedMetric.textWidth.toDp() },
                    label = "tabIndicatorWidth",
                )
                Box(
                    modifier = Modifier
                        .offset(x = indicatorLeft)
                        .width(indicatorWidth)
                        .height(3.dp)
                        .background(MaterialTheme.colorScheme.primary),
                )
            }
        }
        HorizontalDivider()
    }
}

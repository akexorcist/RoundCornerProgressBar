package com.akexorcist.roundcornerprogressbar.compose.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.akexorcist.roundcornerprogressbar.compose.RoundCornerProgressBarDefaults

/**
 * Shared implementation for the basic, centered, and indeterminate variants:
 * a background rounded rectangle with a secondary progress drawn below the
 * primary progress.
 */
@Composable
internal fun BaseRoundCornerProgressBar(
    progress: State<Float>,
    secondaryProgress: State<Float>,
    modifier: Modifier,
    max: Float,
    radius: Dp,
    backgroundPadding: Dp,
    reverse: Boolean,
    centered: Boolean,
    backgroundColor: Color,
    progressColors: List<Color>,
    secondaryProgressColors: List<Color>,
) {
    Canvas(
        modifier = modifier.size(
            width = RoundCornerProgressBarDefaults.Width,
            height = RoundCornerProgressBarDefaults.Height,
        ),
    ) {
        val radiusPx = radius.toPx()
        val paddingPx = backgroundPadding.toPx()
        drawProgressBackground(
            color = backgroundColor,
            radiusPx = radiusPx,
            paddingPx = paddingPx,
        )
        drawDeterminateProgress(
            colors = secondaryProgressColors,
            max = max,
            progress = secondaryProgress.value,
            radiusPx = radiusPx,
            paddingPx = paddingPx,
            reverse = reverse,
            centered = centered,
        )
        drawDeterminateProgress(
            colors = progressColors,
            max = max,
            progress = progress.value,
            radiusPx = radiusPx,
            paddingPx = paddingPx,
            reverse = reverse,
            centered = centered,
        )
    }
}

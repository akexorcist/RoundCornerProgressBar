package com.akexorcist.roundcornerprogressbar.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.akexorcist.roundcornerprogressbar.compose.common.BaseRoundCornerProgressBar
import com.akexorcist.roundcornerprogressbar.compose.common.animateProgressAsState
import com.akexorcist.roundcornerprogressbar.compose.common.resolveProgressColors

/**
 * A progress bar with rounded corners and a horizontally centered progress,
 * the Jetpack Compose equivalent of
 * `com.akexorcist.roundcornerprogressbar.CenteredRoundCornerProgressBar`.
 *
 * See [RoundCornerProgressBar] for the parameter documentation.
 */
@Composable
fun CenteredRoundCornerProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    max: Float = RoundCornerProgressBarDefaults.Max,
    secondaryProgress: Float = 0f,
    radius: Dp = RoundCornerProgressBarDefaults.Radius,
    backgroundPadding: Dp = RoundCornerProgressBarDefaults.BackgroundPadding,
    reverse: Boolean = false,
    backgroundColor: Color = RoundCornerProgressBarDefaults.BackgroundColor,
    progressColor: Color = RoundCornerProgressBarDefaults.ProgressColor,
    progressColors: List<Color>? = null,
    secondaryProgressColor: Color = RoundCornerProgressBarDefaults.SecondaryProgressColor,
    secondaryProgressColors: List<Color>? = null,
    animationEnabled: Boolean = false,
    animationSpeedScale: Float = RoundCornerProgressBarDefaults.AnimationSpeedScale,
    onProgressChanged: ((progress: Float) -> Unit)? = null,
    onSecondaryProgressChanged: ((progress: Float) -> Unit)? = null,
) {
    val progressState = animateProgressAsState(
        progress = progress,
        max = max,
        animationEnabled = animationEnabled,
        animationSpeedScale = animationSpeedScale,
        onProgressChanged = onProgressChanged,
    )
    val secondaryProgressState = animateProgressAsState(
        progress = secondaryProgress,
        max = max,
        animationEnabled = animationEnabled,
        animationSpeedScale = animationSpeedScale,
        onProgressChanged = onSecondaryProgressChanged,
    )
    BaseRoundCornerProgressBar(
        progress = progressState,
        secondaryProgress = secondaryProgressState,
        modifier = modifier,
        max = max,
        radius = radius,
        backgroundPadding = backgroundPadding,
        reverse = reverse,
        centered = true,
        backgroundColor = backgroundColor,
        progressColors = resolveProgressColors(progressColor, progressColors),
        secondaryProgressColors = resolveProgressColors(secondaryProgressColor, secondaryProgressColors),
    )
}

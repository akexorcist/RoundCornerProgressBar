package com.akexorcist.roundcornerprogressbar.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.akexorcist.roundcornerprogressbar.compose.common.BaseRoundCornerProgressBar
import com.akexorcist.roundcornerprogressbar.compose.common.animateProgressAsState
import com.akexorcist.roundcornerprogressbar.compose.common.resolveProgressColors

/**
 * A progress bar with rounded corners, the Jetpack Compose equivalent of
 * `com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar`.
 *
 * @param progress Current primary progress, from 0 to [max].
 * @param modifier Modifier for this progress bar. Defaults to 240x30dp when no size constraint is given.
 * @param max Max progress, matching `rcMax`.
 * @param secondaryProgress Current secondary progress, from 0 to [max], matching `rcSecondaryProgress`.
 * @param radius Corner radius, matching `rcRadius`.
 * @param backgroundPadding Padding between the background and the progress, matching `rcBackgroundPadding`.
 * @param reverse Move the progress from right to left, matching `rcReverse`.
 * @param backgroundColor Color of the progress background, matching `rcBackgroundColor`.
 * @param progressColor Color of the primary progress, matching `rcProgressColor`.
 * @param progressColors Gradient colors of the primary progress, matching `rcProgressColors`.
 * Overrides [progressColor] when not null or empty.
 * @param secondaryProgressColor Color of the secondary progress, matching `rcSecondaryProgressColor`.
 * @param secondaryProgressColors Gradient colors of the secondary progress, matching `rcSecondaryProgressColors`.
 * Overrides [secondaryProgressColor] when not null or empty.
 * @param animationEnabled Animate progress changes, matching `rcAnimationEnable`.
 * @param animationSpeedScale Animation speed scale from 0.2 to 5, matching `rcAnimationSpeedScale`.
 * @param onProgressChanged Called when the displayed primary progress changes,
 * including every frame while animating.
 * @param onSecondaryProgressChanged Called when the displayed secondary progress changes,
 * including every frame while animating.
 */
@Composable
fun RoundCornerProgressBar(
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
        centered = false,
        backgroundColor = backgroundColor,
        progressColors = resolveProgressColors(progressColor, progressColors),
        secondaryProgressColors = resolveProgressColors(secondaryProgressColor, secondaryProgressColors),
    )
}

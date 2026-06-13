package com.akexorcist.roundcornerprogressbar.compose

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.akexorcist.roundcornerprogressbar.compose.common.AccelerateDecelerateEasing
import com.akexorcist.roundcornerprogressbar.compose.common.BaseRoundCornerProgressBar
import com.akexorcist.roundcornerprogressbar.compose.common.MAX_ANIMATION_SPEED_SCALE
import com.akexorcist.roundcornerprogressbar.compose.common.MIN_ANIMATION_SPEED_SCALE
import com.akexorcist.roundcornerprogressbar.compose.common.resolveProgressColors

/**
 * Animates an endless 0 to [RoundCornerProgressBarDefaults.Max] sweep,
 * matching the looping animation of the indeterminate progress bars in the
 * original library.
 */
@Composable
internal fun animateIndeterminateProgressAsState(
    animationSpeedScale: Float,
    onProgressChanged: ((progress: Float) -> Unit)?,
): State<Float> {
    val scale = animationSpeedScale.coerceIn(MIN_ANIMATION_SPEED_SCALE, MAX_ANIMATION_SPEED_SCALE)
    val transition = rememberInfiniteTransition(label = "IndeterminateRoundCornerProgressBar")
    val progressState = transition.animateFloat(
        initialValue = 0f,
        targetValue = RoundCornerProgressBarDefaults.Max,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (RoundCornerProgressBarDefaults.AnimationDurationMillis * scale).toInt(),
                easing = AccelerateDecelerateEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Progress",
    )
    if (onProgressChanged != null) {
        val currentOnProgressChanged by rememberUpdatedState(onProgressChanged)
        LaunchedEffect(Unit) {
            snapshotFlow { progressState.value }.collect { currentOnProgressChanged(it) }
        }
    }
    return progressState
}

/**
 * An endlessly animating progress bar with rounded corners, the Jetpack
 * Compose equivalent of
 * `com.akexorcist.roundcornerprogressbar.indeterminate.IndeterminateRoundCornerProgressBar`.
 *
 * The progress repeatedly sweeps from empty to full. The sweep duration is
 * 500ms scaled by [animationSpeedScale].
 *
 * See [RoundCornerProgressBar] for the parameter documentation.
 */
@Composable
fun IndeterminateRoundCornerProgressBar(
    modifier: Modifier = Modifier,
    secondaryProgress: Float = 0f,
    radius: Dp = RoundCornerProgressBarDefaults.Radius,
    backgroundPadding: Dp = RoundCornerProgressBarDefaults.BackgroundPadding,
    reverse: Boolean = false,
    backgroundColor: Color = RoundCornerProgressBarDefaults.BackgroundColor,
    progressColor: Color = RoundCornerProgressBarDefaults.ProgressColor,
    progressColors: List<Color>? = null,
    secondaryProgressColor: Color = RoundCornerProgressBarDefaults.SecondaryProgressColor,
    secondaryProgressColors: List<Color>? = null,
    animationSpeedScale: Float = RoundCornerProgressBarDefaults.AnimationSpeedScale,
    onProgressChanged: ((progress: Float) -> Unit)? = null,
) {
    val progressState = animateIndeterminateProgressAsState(
        animationSpeedScale = animationSpeedScale,
        onProgressChanged = onProgressChanged,
    )
    val secondaryProgressState = rememberUpdatedState(secondaryProgress)
    BaseRoundCornerProgressBar(
        progress = progressState,
        secondaryProgress = secondaryProgressState,
        modifier = modifier,
        max = RoundCornerProgressBarDefaults.Max,
        radius = radius,
        backgroundPadding = backgroundPadding,
        reverse = reverse,
        centered = false,
        backgroundColor = backgroundColor,
        progressColors = resolveProgressColors(progressColor, progressColors),
        secondaryProgressColors = resolveProgressColors(secondaryProgressColor, secondaryProgressColors),
    )
}

/**
 * An endlessly animating progress bar with rounded corners and a horizontally
 * centered progress, the Jetpack Compose equivalent of
 * `com.akexorcist.roundcornerprogressbar.indeterminate.IndeterminateCenteredRoundCornerProgressBar`.
 *
 * See [IndeterminateRoundCornerProgressBar] and [RoundCornerProgressBar] for
 * the parameter documentation.
 */
@Composable
fun IndeterminateCenteredRoundCornerProgressBar(
    modifier: Modifier = Modifier,
    secondaryProgress: Float = 0f,
    radius: Dp = RoundCornerProgressBarDefaults.Radius,
    backgroundPadding: Dp = RoundCornerProgressBarDefaults.BackgroundPadding,
    reverse: Boolean = false,
    backgroundColor: Color = RoundCornerProgressBarDefaults.BackgroundColor,
    progressColor: Color = RoundCornerProgressBarDefaults.ProgressColor,
    progressColors: List<Color>? = null,
    secondaryProgressColor: Color = RoundCornerProgressBarDefaults.SecondaryProgressColor,
    secondaryProgressColors: List<Color>? = null,
    animationSpeedScale: Float = RoundCornerProgressBarDefaults.AnimationSpeedScale,
    onProgressChanged: ((progress: Float) -> Unit)? = null,
) {
    val progressState = animateIndeterminateProgressAsState(
        animationSpeedScale = animationSpeedScale,
        onProgressChanged = onProgressChanged,
    )
    val secondaryProgressState = rememberUpdatedState(secondaryProgress)
    BaseRoundCornerProgressBar(
        progress = progressState,
        secondaryProgress = secondaryProgressState,
        modifier = modifier,
        max = RoundCornerProgressBarDefaults.Max,
        radius = radius,
        backgroundPadding = backgroundPadding,
        reverse = reverse,
        centered = true,
        backgroundColor = backgroundColor,
        progressColors = resolveProgressColors(progressColor, progressColors),
        secondaryProgressColors = resolveProgressColors(secondaryProgressColor, secondaryProgressColors),
    )
}

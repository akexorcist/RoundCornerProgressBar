package com.akexorcist.roundcornerprogressbar.compose.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import com.akexorcist.roundcornerprogressbar.compose.RoundCornerProgressBarDefaults
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos

internal const val MIN_ANIMATION_SPEED_SCALE = 0.2f
internal const val MAX_ANIMATION_SPEED_SCALE = 5f

/**
 * The exact curve of `android.view.animation.AccelerateDecelerateInterpolator`,
 * the default interpolator of the `ValueAnimator` used by
 * `AnimatedRoundCornerProgressBar`: `cos((t + 1) * π) / 2 + 0.5`.
 */
internal val AccelerateDecelerateEasing = Easing { fraction ->
    (cos((fraction + 1) * PI) / 2.0 + 0.5).toFloat()
}

/**
 * Animates progress changes the same way as `AnimatedRoundCornerProgressBar`:
 * the duration is proportional to the progress difference
 * (`difference * 500 / max * speedScale`), and changes are applied instantly
 * when [animationEnabled] is false.
 */
@Composable
internal fun animateProgressAsState(
    progress: Float,
    max: Float,
    animationEnabled: Boolean,
    animationSpeedScale: Float,
    onProgressChanged: ((progress: Float) -> Unit)?,
): State<Float> {
    val animatable = remember { Animatable(progress.coerceIn(0f, max.coerceAtLeast(0f))) }
    LaunchedEffect(progress, max, animationEnabled, animationSpeedScale) {
        val target = progress.coerceIn(0f, max.coerceAtLeast(0f))
        if (animationEnabled && max > 0f) {
            val scale = animationSpeedScale.coerceIn(MIN_ANIMATION_SPEED_SCALE, MAX_ANIMATION_SPEED_SCALE)
            val duration = abs(animatable.value - target) *
                RoundCornerProgressBarDefaults.AnimationDurationMillis / max * scale
            animatable.animateTo(
                targetValue = target,
                animationSpec = tween(durationMillis = duration.toInt(), easing = AccelerateDecelerateEasing),
            )
        } else {
            animatable.snapTo(target)
        }
    }
    if (onProgressChanged != null) {
        val currentOnProgressChanged by rememberUpdatedState(onProgressChanged)
        LaunchedEffect(Unit) {
            snapshotFlow { animatable.value }.collect { currentOnProgressChanged(it) }
        }
    }
    return animatable.asState()
}

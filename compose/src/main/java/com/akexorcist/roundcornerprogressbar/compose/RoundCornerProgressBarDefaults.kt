package com.akexorcist.roundcornerprogressbar.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Default values used by all round corner progress bar composables.
 *
 * These values match the defaults of the original View-based library
 * (`com.akexorcist:round-corner-progress-bar`).
 */
object RoundCornerProgressBarDefaults {
    /** Default width when the caller does not provide any size constraint. */
    val Width = 240.dp

    /** Default height when the caller does not provide any size constraint. */
    val Height = 30.dp

    /** Default max progress, matching `rcMax`. */
    const val Max = 100f

    /** Default corner radius, matching `rcRadius`. */
    val Radius = 30.dp

    /** Default background padding, matching `rcBackgroundPadding`. */
    val BackgroundPadding = 0.dp

    /** Default background color, matching `rcBackgroundColor`. */
    val BackgroundColor = Color(0xFF5F5F5F)

    /** Default progress color, matching `rcProgressColor`. */
    val ProgressColor = Color(0xFF7F7F7F)

    /** Default secondary progress color, matching `rcSecondaryProgressColor`. */
    val SecondaryProgressColor = Color.Transparent

    /** Default animation speed scale, matching `rcAnimationSpeedScale`. */
    const val AnimationSpeedScale = 1f

    /** Base animation duration in milliseconds for a full progress sweep. */
    const val AnimationDurationMillis = 500

    /** Default icon size, matching `rcIconWidth` / `rcIconHeight`. */
    val IconSize = DpSize(20.dp, 20.dp)

    /** Default icon padding, matching `rcIconPadding`. */
    val IconPadding = PaddingValues(0.dp)

    /** Default progress text color, matching `rcTextProgressColor`. */
    val TextColor = Color.White

    /** Default progress text size, matching `rcTextProgressSize`. */
    val TextSize = 16.sp

    /** Default progress text margin, matching `rcTextProgressMargin`. */
    val TextMargin = 10.dp
}

/**
 * Horizontal alignment of the progress text, matching
 * `rcTextInsideGravity` / `rcTextOutsideGravity` of the original library.
 */
enum class TextGravity {
    /** Align the text to the head of the progress, matching `GRAVITY_START`. */
    Start,

    /** Align the text to the tail of the progress, matching `GRAVITY_END`. */
    End,
}

/**
 * Position priority of the progress text when both positions are possible,
 * matching `rcTextPositionPriority` of the original library.
 */
enum class TextPositionPriority {
    /** Keep the text inside the progress when it fits, matching `PRIORITY_INSIDE`. */
    Inside,

    /** Keep the text outside the progress when there is room, matching `PRIORITY_OUTSIDE`. */
    Outside,
}

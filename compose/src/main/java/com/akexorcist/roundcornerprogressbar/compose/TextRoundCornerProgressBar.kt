package com.akexorcist.roundcornerprogressbar.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.akexorcist.roundcornerprogressbar.compose.common.animateProgressAsState
import com.akexorcist.roundcornerprogressbar.compose.common.computeTextLeft
import com.akexorcist.roundcornerprogressbar.compose.common.drawDeterminateProgress
import com.akexorcist.roundcornerprogressbar.compose.common.drawProgressBackground
import com.akexorcist.roundcornerprogressbar.compose.common.resolveProgressColors

/**
 * A progress bar with rounded corners and a text label that moves with the
 * progress, the Jetpack Compose equivalent of
 * `com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar`.
 *
 * @param text Text shown on the progress bar, matching `rcTextProgress`.
 * @param textColor Color of the text, matching `rcTextProgressColor`.
 * @param textSize Size of the text, matching `rcTextProgressSize`.
 * @param textMargin Margin between the text and the progress edge, matching `rcTextProgressMargin`.
 * @param textInsideGravity Alignment of the text while inside the progress, matching `rcTextInsideGravity`.
 * @param textOutsideGravity Alignment of the text while outside the progress, matching `rcTextOutsideGravity`.
 * @param textPositionPriority Preferred position of the text when both fit, matching `rcTextPositionPriority`.
 *
 * See [RoundCornerProgressBar] for the remaining parameter documentation.
 */
@Composable
fun TextRoundCornerProgressBar(
    text: String,
    progress: Float,
    modifier: Modifier = Modifier,
    max: Float = RoundCornerProgressBarDefaults.Max,
    secondaryProgress: Float = 0f,
    radius: Dp = RoundCornerProgressBarDefaults.Radius,
    backgroundPadding: Dp = RoundCornerProgressBarDefaults.BackgroundPadding,
    reverse: Boolean = false,
    textColor: Color = RoundCornerProgressBarDefaults.TextColor,
    textSize: TextUnit = RoundCornerProgressBarDefaults.TextSize,
    textMargin: Dp = RoundCornerProgressBarDefaults.TextMargin,
    textInsideGravity: TextGravity = TextGravity.Start,
    textOutsideGravity: TextGravity = TextGravity.Start,
    textPositionPriority: TextPositionPriority = TextPositionPriority.Inside,
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
    val resolvedProgressColors = resolveProgressColors(progressColor, progressColors)
    val resolvedSecondaryProgressColors = resolveProgressColors(secondaryProgressColor, secondaryProgressColors)

    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult = remember(textMeasurer, text, textColor, textSize) {
        textMeasurer.measure(
            text = AnnotatedString(text),
            style = TextStyle(color = textColor, fontSize = textSize),
            maxLines = 1,
        )
    }

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
            colors = resolvedSecondaryProgressColors,
            max = max,
            progress = secondaryProgressState.value,
            radiusPx = radiusPx,
            paddingPx = paddingPx,
            reverse = reverse,
            centered = false,
        )
        drawDeterminateProgress(
            colors = resolvedProgressColors,
            max = max,
            progress = progressState.value,
            radiusPx = radiusPx,
            paddingPx = paddingPx,
            reverse = reverse,
            centered = false,
        )

        // The original positions the text from the target progress
        // immediately (`getProgress()` returns the final value while
        // animating), so the text jumps to its final position while the
        // progress bar animates behind it.
        val textLeft = computeTextLeft(
            totalWidth = size.width,
            max = max,
            progress = progress.coerceIn(0f, max.coerceAtLeast(0f)),
            padding = paddingPx,
            textWidth = textLayoutResult.size.width.toFloat(),
            textMargin = textMargin.toPx(),
            insideGravity = textInsideGravity,
            outsideGravity = textOutsideGravity,
            positionPriority = textPositionPriority,
            reverse = reverse,
        )
        drawText(
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                x = textLeft,
                y = (size.height - textLayoutResult.size.height) / 2f,
            ),
        )
    }
}

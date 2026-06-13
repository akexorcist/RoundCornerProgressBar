package com.akexorcist.roundcornerprogressbar.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.akexorcist.roundcornerprogressbar.compose.common.animateProgressAsState
import com.akexorcist.roundcornerprogressbar.compose.common.drawIconProgress
import com.akexorcist.roundcornerprogressbar.compose.common.drawProgressBackground
import com.akexorcist.roundcornerprogressbar.compose.common.resolveProgressColors

/**
 * A progress bar with rounded corners and an icon at its head, the Jetpack
 * Compose equivalent of
 * `com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar`.
 *
 * @param icon Painter for the icon, covering `rcIconSrc` / `setIconImageBitmap` /
 * `setIconImageDrawable` of the original library.
 * @param iconContentDescription Content description of the icon for accessibility.
 * @param iconSize Size of the icon area, matching `rcIconSize` / `rcIconWidth` / `rcIconHeight`.
 * @param iconPadding Padding around the icon image, matching `rcIconPadding` and
 * the per-side `rcIconPadding*` attributes.
 * @param iconBackgroundColor Background color behind the icon, matching `rcIconBackgroundColor`.
 * @param onIconClick Called when the icon is clicked, matching `setOnIconClickListener`.
 *
 * See [RoundCornerProgressBar] for the remaining parameter documentation.
 */
@Composable
fun IconRoundCornerProgressBar(
    icon: Painter,
    progress: Float,
    modifier: Modifier = Modifier,
    iconContentDescription: String? = null,
    max: Float = RoundCornerProgressBarDefaults.Max,
    secondaryProgress: Float = 0f,
    radius: Dp = RoundCornerProgressBarDefaults.Radius,
    backgroundPadding: Dp = RoundCornerProgressBarDefaults.BackgroundPadding,
    reverse: Boolean = false,
    iconSize: DpSize = RoundCornerProgressBarDefaults.IconSize,
    iconPadding: PaddingValues = RoundCornerProgressBarDefaults.IconPadding,
    iconBackgroundColor: Color = RoundCornerProgressBarDefaults.BackgroundColor,
    backgroundColor: Color = RoundCornerProgressBarDefaults.BackgroundColor,
    progressColor: Color = RoundCornerProgressBarDefaults.ProgressColor,
    progressColors: List<Color>? = null,
    secondaryProgressColor: Color = RoundCornerProgressBarDefaults.SecondaryProgressColor,
    secondaryProgressColors: List<Color>? = null,
    animationEnabled: Boolean = false,
    animationSpeedScale: Float = RoundCornerProgressBarDefaults.AnimationSpeedScale,
    onIconClick: (() -> Unit)? = null,
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

    Box(
        modifier = modifier.size(
            width = RoundCornerProgressBarDefaults.Width,
            height = RoundCornerProgressBarDefaults.Height,
        ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val radiusPx = radius.toPx()
            val paddingPx = backgroundPadding.toPx()
            val iconWidthPx = iconSize.width.toPx()
            drawProgressBackground(
                color = backgroundColor,
                radiusPx = radiusPx,
                paddingPx = paddingPx,
            )
            drawIconProgress(
                colors = resolvedSecondaryProgressColors,
                max = max,
                progress = secondaryProgressState.value,
                radiusPx = radiusPx,
                paddingPx = paddingPx,
                iconWidthPx = iconWidthPx,
                reverse = reverse,
            )
            drawIconProgress(
                colors = resolvedProgressColors,
                max = max,
                progress = progressState.value,
                radiusPx = radiusPx,
                paddingPx = paddingPx,
                iconWidthPx = iconWidthPx,
                reverse = reverse,
            )
        }
        val iconCornerRadius = (radius - backgroundPadding / 2).coerceAtLeast(0.dp)
        val iconBackgroundShape = RoundedCornerShape(
            topStart = iconCornerRadius,
            bottomStart = iconCornerRadius,
        )
        Image(
            painter = icon,
            contentDescription = iconContentDescription,
            modifier = Modifier
                .padding(start = backgroundPadding)
                .size(iconSize.width, iconSize.height)
                .background(color = iconBackgroundColor, shape = iconBackgroundShape)
                .then(
                    if (onIconClick != null) {
                        Modifier.clickable(onClick = onIconClick)
                    } else {
                        Modifier
                    }
                )
                .padding(iconPadding),
            contentScale = ContentScale.Fit,
        )
    }
}

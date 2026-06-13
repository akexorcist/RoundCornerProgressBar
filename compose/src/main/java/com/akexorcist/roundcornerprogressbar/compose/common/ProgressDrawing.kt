package com.akexorcist.roundcornerprogressbar.compose.common

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope

internal fun resolveProgressColors(color: Color, colors: List<Color>?): List<Color> =
    colors?.takeIf { it.isNotEmpty() } ?: listOf(color)

/**
 * Creates the brush for a progress rectangle. A multi-color list becomes a
 * horizontal gradient spanning the progress rectangle, reversed when the
 * progress direction is reversed — matching `createGradientDrawable(colors)`
 * of the original library.
 */
private fun progressBrush(colors: List<Color>, reverse: Boolean, startX: Float, endX: Float): Brush =
    if (colors.size <= 1) {
        SolidColor(colors.firstOrNull() ?: Color.Transparent)
    } else {
        Brush.horizontalGradient(
            colors = if (reverse) colors.reversed() else colors,
            startX = startX,
            endX = endX,
        )
    }

/**
 * Draws the progress background, matching `drawBackgroundProgress()`:
 * a rounded rectangle filling the whole bounds with a corner radius of
 * `radius - padding / 2`.
 */
internal fun DrawScope.drawProgressBackground(
    color: Color,
    radiusPx: Float,
    paddingPx: Float,
) {
    val corner = (radiusPx - paddingPx / 2f).coerceAtLeast(0f)
    drawRoundRect(color = color, cornerRadius = CornerRadius(corner, corner))
}

/**
 * Draws a determinate progress rectangle, matching
 * `RoundCornerProgressBar.drawProgress()` (and the centered variant).
 * See [computeProgressRect] for the geometry.
 */
internal fun DrawScope.drawDeterminateProgress(
    colors: List<Color>,
    max: Float,
    progress: Float,
    radiusPx: Float,
    paddingPx: Float,
    reverse: Boolean,
    centered: Boolean,
) {
    val rect = computeProgressRect(
        totalWidth = size.width,
        totalHeight = size.height,
        max = max,
        progress = progress,
        radius = radiusPx,
        padding = paddingPx,
        reverse = reverse,
        centered = centered,
    ) ?: return
    drawRoundRect(
        brush = progressBrush(colors, reverse, rect.left, rect.left + rect.width),
        topLeft = Offset(rect.left, rect.top),
        size = Size(rect.width, rect.height),
        cornerRadius = CornerRadius(rect.cornerRadius, rect.cornerRadius),
    )
}

/**
 * Draws a progress rectangle for the icon variant, matching
 * `IconRoundCornerProgressBar.drawProgress()`.
 * See [computeIconProgressRect] for the geometry.
 */
internal fun DrawScope.drawIconProgress(
    colors: List<Color>,
    max: Float,
    progress: Float,
    radiusPx: Float,
    paddingPx: Float,
    iconWidthPx: Float,
    reverse: Boolean,
) {
    val rect = computeIconProgressRect(
        totalWidth = size.width,
        totalHeight = size.height,
        max = max,
        progress = progress,
        radius = radiusPx,
        padding = paddingPx,
        iconWidth = iconWidthPx,
        reverse = reverse,
    ) ?: return
    val cornerRadius = CornerRadius(rect.cornerRadius, rect.cornerRadius)
    val roundRect = if (rect.fullyRounded) {
        RoundRect(
            left = rect.left,
            top = rect.top,
            right = rect.left + rect.width,
            bottom = rect.top + rect.height,
            cornerRadius = cornerRadius,
        )
    } else {
        RoundRect(
            left = rect.left,
            top = rect.top,
            right = rect.left + rect.width,
            bottom = rect.top + rect.height,
            topLeftCornerRadius = CornerRadius.Zero,
            topRightCornerRadius = cornerRadius,
            bottomRightCornerRadius = cornerRadius,
            bottomLeftCornerRadius = CornerRadius.Zero,
        )
    }
    drawPath(
        path = Path().apply { addRoundRect(roundRect) },
        brush = progressBrush(colors, reverse, rect.left, rect.left + rect.width),
    )
}

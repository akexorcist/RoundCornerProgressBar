package com.akexorcist.roundcornerprogressbar.compose.common

import com.akexorcist.roundcornerprogressbar.compose.TextGravity
import com.akexorcist.roundcornerprogressbar.compose.TextPositionPriority
import kotlin.math.min

/**
 * Pure geometry calculations transcribed 1:1 from the View-based library so
 * they can be verified against it with unit tests.
 *
 * All values are in pixels and relative to the whole progress bar bounds.
 */
internal data class ProgressRect(
    val left: Float,
    val top: Float,
    val width: Float,
    val height: Float,
    val cornerRadius: Float,
)

internal data class IconProgressRect(
    val left: Float,
    val top: Float,
    val width: Float,
    val height: Float,
    val cornerRadius: Float,
    /** All four corners rounded instead of only the two away from the icon. */
    val fullyRounded: Boolean,
)

/**
 * Matches `BaseRoundCornerProgressBar.drawPrimaryProgress()` +
 * `RoundCornerProgressBar.drawProgress()` and the centered offset of
 * `CenteredRoundCornerProgressBar.drawProgress()`.
 *
 * Returns null when there is nothing to draw.
 */
internal fun computeProgressRect(
    totalWidth: Float,
    totalHeight: Float,
    max: Float,
    progress: Float,
    radius: Float,
    padding: Float,
    reverse: Boolean,
    centered: Boolean,
): ProgressRect? {
    if (max <= 0f || progress <= 0f) return null
    val fraction = (progress / max).coerceIn(0f, 1f)
    val progressWidth = (totalWidth - padding * 2f) * fraction
    if (progressWidth <= 0f) return null

    // possibleRadius = radius.coerceAtMost(layoutBackground.measuredHeight / 2)
    val possibleRadius = min(radius, totalHeight / 2f)
    // newRadius = radius - (padding / 2f)
    val cornerRadius = (possibleRadius - padding / 2f).coerceAtLeast(0f)
    // Vertical shrinking when the progress is narrower than the radius,
    // matching the topMargin/bottomMargin logic in drawProgress().
    val verticalMargin = if (padding + (progressWidth / 2f) < possibleRadius) {
        (possibleRadius - padding).coerceAtLeast(0f) - (progressWidth / 2f)
    } else {
        0f
    }
    // CenteredRoundCornerProgressBar sets deltaWidth / 2 margins on both
    // sides inside the padded holder. RelativeLayout then clamps the child
    // width to the space remaining after both margins
    // (holderWidth - 2 * margin = progressWidth - 2 * padding), so the
    // rendered progress is centered and shrunk by the padding on each side.
    // Verified against the View implementation pixel-by-pixel.
    val renderWidth = if (centered) {
        min(progressWidth, progressWidth - padding * 2f)
    } else {
        progressWidth
    }
    if (renderWidth <= 0f) return null
    val left = when {
        centered -> padding + (totalWidth - progressWidth) / 2f
        reverse -> totalWidth - padding - progressWidth
        else -> padding
    }
    val top = padding + verticalMargin
    val height = totalHeight - (top * 2f)
    if (height <= 0f) return null
    return ProgressRect(
        left = left,
        top = top,
        width = renderWidth,
        height = height,
        cornerRadius = cornerRadius,
    )
}

/**
 * Matches `IconRoundCornerProgressBar.drawProgress()`: the progress area
 * excludes the icon width, the vertical shrinking is applied in reverse mode
 * only, and the corners on the icon side stay square unless reversed and not
 * yet full.
 *
 * Returns null when there is nothing to draw.
 */
internal fun computeIconProgressRect(
    totalWidth: Float,
    totalHeight: Float,
    max: Float,
    progress: Float,
    radius: Float,
    padding: Float,
    iconWidth: Float,
    reverse: Boolean,
): IconProgressRect? {
    if (max <= 0f || progress <= 0f) return null
    val fraction = (progress / max).coerceIn(0f, 1f)
    val progressWidth = (totalWidth - (padding * 2f + iconWidth)) * fraction
    if (progressWidth <= 0f) return null

    val possibleRadius = min(radius, totalHeight / 2f)
    val cornerRadius = (possibleRadius - padding / 2f).coerceAtLeast(0f)
    val verticalMargin = if (reverse && padding + (progressWidth / 2f) < possibleRadius) {
        (possibleRadius - padding).coerceAtLeast(0f) - (progressWidth / 2f)
    } else {
        0f
    }
    val left = if (reverse) {
        totalWidth - padding - progressWidth
    } else {
        padding + iconWidth
    }
    val top = padding + verticalMargin
    val height = totalHeight - (top * 2f)
    if (height <= 0f) return null
    return IconProgressRect(
        left = left,
        top = top,
        width = progressWidth,
        height = height,
        cornerRadius = cornerRadius,
        fullyRounded = reverse && fraction < 1f,
    )
}

/**
 * Matches `TextRoundCornerProgressBar.drawTextProgressPosition()` and the
 * align rules of `alignTextProgressInsideProgress()` /
 * `alignTextProgressOutsideProgress()`.
 *
 * Returns the left position of the text. [progress] is the target progress —
 * the original repositions the text with the final value immediately, even
 * while the progress is still animating.
 */
internal fun computeTextLeft(
    totalWidth: Float,
    max: Float,
    progress: Float,
    padding: Float,
    textWidth: Float,
    textMargin: Float,
    insideGravity: TextGravity,
    outsideGravity: TextGravity,
    positionPriority: TextPositionPriority,
    reverse: Boolean,
): Float {
    val fraction = if (max > 0f) (progress / max).coerceIn(0f, 1f) else 0f
    val progressWidth = (totalWidth - padding * 2f) * fraction
    val textTotalWidth = textWidth + textMargin * 2f
    val isOutside = when (positionPriority) {
        TextPositionPriority.Outside -> (totalWidth - progressWidth) > textTotalWidth
        TextPositionPriority.Inside -> (textTotalWidth + textMargin) > progressWidth
    }
    val progressLeft = if (reverse) totalWidth - padding - progressWidth else padding
    val progressRight = progressLeft + progressWidth
    return if (!isOutside) {
        when {
            !reverse && insideGravity == TextGravity.End -> progressLeft + textMargin
            !reverse -> progressRight - textMargin - textWidth
            insideGravity == TextGravity.End -> progressRight - textMargin - textWidth
            else -> progressLeft + textMargin
        }
    } else {
        when {
            !reverse && outsideGravity == TextGravity.End -> totalWidth - padding - textMargin - textWidth
            !reverse -> progressRight + textMargin
            outsideGravity == TextGravity.End -> padding + textMargin
            else -> progressLeft - textMargin - textWidth
        }
    }
}

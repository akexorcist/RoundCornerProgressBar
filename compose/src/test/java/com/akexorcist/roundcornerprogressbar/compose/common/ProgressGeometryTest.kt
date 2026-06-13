package com.akexorcist.roundcornerprogressbar.compose.common

import com.akexorcist.roundcornerprogressbar.compose.TextGravity
import com.akexorcist.roundcornerprogressbar.compose.TextPositionPriority
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Verifies the Compose geometry against values hand-computed from the
 * View-based implementation (`BaseRoundCornerProgressBar`,
 * `RoundCornerProgressBar`, `CenteredRoundCornerProgressBar`,
 * `IconRoundCornerProgressBar`, `TextRoundCornerProgressBar`):
 *
 * - progressWidth = (totalWidth - padding * 2) / (max / progress)
 * - possibleRadius = min(radius, height / 2); corner = possibleRadius - padding / 2
 * - vertical shrink margin = max(possibleRadius - padding, 0) - progressWidth / 2
 *   when padding + progressWidth / 2 < possibleRadius
 * - centered offset = deltaWidth / 2 within the padded holder,
 *   deltaWidth = totalWidth - progressWidth
 */
class ProgressGeometryTest {
    private val delta = 0.001f

    // region computeProgressRect — RoundCornerProgressBar.drawProgress()

    @Test
    fun `basic progress geometry`() {
        val rect = computeProgressRect(1000f, 100f, 100f, 50f, 40f, 0f, reverse = false, centered = false)!!
        assertEquals(0f, rect.left, delta)
        assertEquals(0f, rect.top, delta)
        assertEquals(500f, rect.width, delta)
        assertEquals(100f, rect.height, delta)
        assertEquals(40f, rect.cornerRadius, delta)
    }

    @Test
    fun `reverse progress starts from the right edge`() {
        val rect = computeProgressRect(1000f, 100f, 100f, 50f, 40f, 0f, reverse = true, centered = false)!!
        assertEquals(500f, rect.left, delta)
        assertEquals(500f, rect.width, delta)
    }

    @Test
    fun `padding insets the progress and reduces the corner radius`() {
        // progressWidth = (1000 - 20) * 0.5 = 490, corner = 40 - 10/2 = 35
        val rect = computeProgressRect(1000f, 100f, 100f, 50f, 40f, 10f, reverse = false, centered = false)!!
        assertEquals(10f, rect.left, delta)
        assertEquals(10f, rect.top, delta)
        assertEquals(490f, rect.width, delta)
        assertEquals(80f, rect.height, delta)
        assertEquals(35f, rect.cornerRadius, delta)
    }

    @Test
    fun `reverse with padding starts from the padded right edge`() {
        val rect = computeProgressRect(1000f, 100f, 100f, 50f, 40f, 10f, reverse = true, centered = false)!!
        assertEquals(500f, rect.left, delta)
        assertEquals(490f, rect.width, delta)
    }

    @Test
    fun `tiny progress shrinks vertically like the original margin trick`() {
        // progressWidth = 10; 0 + 5 < 40 -> margin = (40 - 0) - 5 = 35
        val rect = computeProgressRect(1000f, 100f, 100f, 1f, 40f, 0f, reverse = false, centered = false)!!
        assertEquals(0f, rect.left, delta)
        assertEquals(35f, rect.top, delta)
        assertEquals(10f, rect.width, delta)
        assertEquals(30f, rect.height, delta)
    }

    @Test
    fun `radius is coerced to half height`() {
        val rect = computeProgressRect(1000f, 100f, 100f, 50f, 80f, 0f, reverse = false, centered = false)!!
        assertEquals(50f, rect.cornerRadius, delta)
    }

    @Test
    fun `progress over max is clamped to full width`() {
        val rect = computeProgressRect(1000f, 100f, 100f, 150f, 40f, 0f, reverse = false, centered = false)!!
        assertEquals(1000f, rect.width, delta)
    }

    @Test
    fun `no progress or no max draws nothing`() {
        assertNull(computeProgressRect(1000f, 100f, 100f, 0f, 40f, 0f, reverse = false, centered = false))
        assertNull(computeProgressRect(1000f, 100f, 0f, 50f, 40f, 0f, reverse = false, centered = false))
    }

    // endregion

    // region computeProgressRect — CenteredRoundCornerProgressBar.drawProgress()

    @Test
    fun `centered progress offsets by half delta width`() {
        val rect = computeProgressRect(1000f, 100f, 100f, 50f, 40f, 0f, reverse = false, centered = true)!!
        assertEquals(250f, rect.left, delta)
        assertEquals(500f, rect.width, delta)
    }

    @Test
    fun `centered with padding is squeezed by the margins like RelativeLayout`() {
        // Margins = (1000 - 490) / 2 = 255 each side inside the padded holder,
        // so RelativeLayout clamps the width to 490 - 2 * 10 = 470 and the
        // progress starts at left = 10 + 255 = 265 (pixel-verified against
        // the View implementation on an emulator).
        val rect = computeProgressRect(1000f, 100f, 100f, 50f, 40f, 10f, reverse = false, centered = true)!!
        assertEquals(265f, rect.left, delta)
        assertEquals(470f, rect.width, delta)
    }

    @Test
    fun `centered is identical in both directions`() {
        val normal = computeProgressRect(1000f, 100f, 100f, 30f, 40f, 10f, reverse = false, centered = true)!!
        val reversed = computeProgressRect(1000f, 100f, 100f, 30f, 40f, 10f, reverse = true, centered = true)!!
        assertEquals(normal, reversed)

        val normalNoPadding = computeProgressRect(1000f, 100f, 100f, 30f, 40f, 0f, reverse = false, centered = true)!!
        val reversedNoPadding = computeProgressRect(1000f, 100f, 100f, 30f, 40f, 0f, reverse = true, centered = true)!!
        assertEquals(normalNoPadding, reversedNoPadding)
    }

    // endregion

    // region computeIconProgressRect — IconRoundCornerProgressBar.drawProgress()

    @Test
    fun `icon progress starts after the icon and excludes its width`() {
        // progressWidth = (1000 - (0 + 100)) * 0.5 = 450
        val rect = computeIconProgressRect(1000f, 100f, 100f, 50f, 40f, 0f, iconWidth = 100f, reverse = false)!!
        assertEquals(100f, rect.left, delta)
        assertEquals(0f, rect.top, delta)
        assertEquals(450f, rect.width, delta)
        assertEquals(100f, rect.height, delta)
        assertEquals(40f, rect.cornerRadius, delta)
        assertFalse(rect.fullyRounded)
    }

    @Test
    fun `icon progress in reverse is fully rounded until full`() {
        val rect = computeIconProgressRect(1000f, 100f, 100f, 50f, 40f, 0f, iconWidth = 100f, reverse = true)!!
        assertEquals(550f, rect.left, delta)
        assertEquals(450f, rect.width, delta)
        assertTrue(rect.fullyRounded)
    }

    @Test
    fun `icon progress in reverse at full keeps square icon-side corners`() {
        val rect = computeIconProgressRect(1000f, 100f, 100f, 100f, 40f, 0f, iconWidth = 100f, reverse = true)!!
        assertEquals(100f, rect.left, delta)
        assertEquals(900f, rect.width, delta)
        assertFalse(rect.fullyRounded)
    }

    @Test
    fun `icon progress shrinks vertically only in reverse`() {
        // Matching the original: the margin trick lives in the isReverse branch only.
        val normal = computeIconProgressRect(1000f, 100f, 100f, 1f, 40f, 0f, iconWidth = 100f, reverse = false)!!
        assertEquals(0f, normal.top, delta)
        assertEquals(100f, normal.height, delta)

        // progressWidth = 9; margin = (40 - 0) - 4.5 = 35.5
        val reversed = computeIconProgressRect(1000f, 100f, 100f, 1f, 40f, 0f, iconWidth = 100f, reverse = true)!!
        assertEquals(35.5f, reversed.top, delta)
        assertEquals(29f, reversed.height, delta)
        assertEquals(991f, reversed.left, delta)
    }

    // endregion

    // region computeTextLeft — TextRoundCornerProgressBar.drawTextProgressPosition()

    private fun textLeft(
        progress: Float,
        padding: Float = 0f,
        insideGravity: TextGravity = TextGravity.Start,
        outsideGravity: TextGravity = TextGravity.Start,
        priority: TextPositionPriority = TextPositionPriority.Inside,
        reverse: Boolean = false,
    ): Float = computeTextLeft(
        totalWidth = 1000f,
        max = 100f,
        progress = progress,
        padding = padding,
        textWidth = 80f,
        textMargin = 10f,
        insideGravity = insideGravity,
        outsideGravity = outsideGravity,
        positionPriority = priority,
        reverse = reverse,
    )

    @Test
    fun `text inside aligns to the progress head by default`() {
        // ALIGN_RIGHT of progress: x = 500 - 10 - 80 = 410
        assertEquals(410f, textLeft(progress = 50f), delta)
    }

    @Test
    fun `text inside with end gravity aligns to the progress tail`() {
        // ALIGN_LEFT of progress: x = 0 + 10
        assertEquals(10f, textLeft(progress = 50f, insideGravity = TextGravity.End), delta)
    }

    @Test
    fun `text moves outside when the progress is too small`() {
        // progressWidth = 50 < textTotalWidth + margin -> RIGHT_OF progress: x = 50 + 10
        assertEquals(60f, textLeft(progress = 5f), delta)
    }

    @Test
    fun `text outside with end gravity aligns to the parent end`() {
        // ALIGN_PARENT_RIGHT: x = 1000 - 0 - 10 - 80 = 910
        assertEquals(910f, textLeft(progress = 5f, outsideGravity = TextGravity.End), delta)
    }

    @Test
    fun `outside priority keeps the text outside while there is room`() {
        // 1000 - 500 = 500 > 100 -> outside: x = 500 + 10
        assertEquals(510f, textLeft(progress = 50f, priority = TextPositionPriority.Outside), delta)
    }

    @Test
    fun `outside priority falls back inside when there is no room`() {
        // 1000 - 950 = 50 < 100 -> inside: x = 950 - 10 - 80 = 860
        assertEquals(860f, textLeft(progress = 95f, priority = TextPositionPriority.Outside), delta)
    }

    @Test
    fun `reverse text inside aligns to the progress head on the left`() {
        // ALIGN_LEFT of progress: x = (1000 - 500) + 10 = 510
        assertEquals(510f, textLeft(progress = 50f, reverse = true), delta)
    }

    @Test
    fun `reverse text outside sits before the progress`() {
        // LEFT_OF progress: x = 950 - 10 - 80 = 860
        assertEquals(860f, textLeft(progress = 5f, reverse = true), delta)
    }

    @Test
    fun `reverse text outside with end gravity aligns to the parent start`() {
        // ALIGN_PARENT_LEFT: x = 0 + 10
        assertEquals(10f, textLeft(progress = 5f, reverse = true, outsideGravity = TextGravity.End), delta)
    }

    @Test
    fun `text position respects the background padding`() {
        // progressWidth = (1000 - 20) * 0.5 = 490; x = (10 + 490) - 10 - 80 = 410
        assertEquals(410f, textLeft(progress = 50f, padding = 10f), delta)
    }

    // endregion
}

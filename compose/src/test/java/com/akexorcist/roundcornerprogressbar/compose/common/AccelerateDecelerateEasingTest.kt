package com.akexorcist.roundcornerprogressbar.compose.common

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Verifies the easing against reference values of
 * `android.view.animation.AccelerateDecelerateInterpolator`
 * (`cos((t + 1) * π) / 2 + 0.5`), the default interpolator of the
 * `ValueAnimator` used by the original library.
 */
class AccelerateDecelerateEasingTest {
    private val delta = 0.0001f

    @Test
    fun `matches AccelerateDecelerateInterpolator reference values`() {
        assertEquals(0f, AccelerateDecelerateEasing.transform(0f), delta)
        assertEquals(0.146447f, AccelerateDecelerateEasing.transform(0.25f), delta)
        assertEquals(0.5f, AccelerateDecelerateEasing.transform(0.5f), delta)
        assertEquals(0.853553f, AccelerateDecelerateEasing.transform(0.75f), delta)
        assertEquals(1f, AccelerateDecelerateEasing.transform(1f), delta)
    }
}

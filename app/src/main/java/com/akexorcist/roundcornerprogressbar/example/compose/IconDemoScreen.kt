package com.akexorcist.roundcornerprogressbar.example.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.annotation.DrawableRes
import com.akexorcist.roundcornerprogressbar.compose.IconRoundCornerProgressBar
import com.akexorcist.roundcornerprogressbar.example.R

/**
 * Compose version of `IconDemoFragment` with the same samples.
 * Bar height = icon size + 2 * background padding (the View version uses
 * wrap_content).
 */
@Composable
fun IconDemoScreen() {
    DemoScreenContainer {
        SectionTitle(text = stringResource(R.string.title_icon_round_corner_progress_bar))
        InteractiveIconCard()
        IconSampleCard(
            iconRes = R.drawable.ic_android, iconSize = 40.dp, iconPadding = 5.dp,
            max = 150f, progress = 90f, radius = 5.dp, backgroundPadding = 2.dp,
            description = """
            |Max : 150
            |Progress : 90
            |Icon Size : 40dp
            |Icon Padding : 5dp
            |Radius : 5dp
            |Background Padding : 2dp
            |Width : 260dp
            |Height : Wrap Content
            """.trimMargin(),
        )
        IconSampleCard(
            iconRes = R.drawable.ic_android, iconSize = 40.dp, iconPadding = 5.dp,
            max = 150f, progress = 90f, radius = 5.dp, backgroundPadding = 2.dp,
            reverse = true,
            description = """
            |Max : 150
            |Progress : 90
            |Icon Size : 40dp
            |Icon Padding : 5dp
            |Radius : 5dp
            |Background Padding : 2dp
            |Reverse : True
            |Width : 260dp
            |Height : Wrap Content
            """.trimMargin(),
        )
        IconSampleCard(
            iconRes = R.drawable.ic_android, iconSize = 25.dp, iconPadding = 5.dp,
            max = 150f, progress = 50f, secondaryProgress = 80f, radius = 5.dp,
            backgroundPadding = 5.dp, reverse = true,
            description = """
            |Max : 150
            |Progress : 50
            |Secondary Progress : 80
            |Icon Size : 25dp
            |Icon Padding : 5dp
            |Radius : 5dp
            |Background Padding : 5dp
            |Reverse : True
            |Width : 260dp
            |Height : Wrap Content
            """.trimMargin(),
        )
        IconSampleCard(
            iconRes = R.drawable.ic_clock, iconSize = 70.dp, iconPadding = 5.dp,
            max = 150f, progress = 40f, radius = 5.dp, backgroundPadding = 10.dp,
            description = """
            |Max : 150
            |Progress : 40
            |Icon Size : 70dp
            |Icon Padding : 5dp
            |Radius : 5dp
            |Background Padding : 10dp
            |Width : 260dp
            |Height : Wrap Content
            """.trimMargin(),
        )
        IconSampleCard(
            iconRes = R.drawable.ic_download, iconSize = 30.dp, iconPadding = 3.dp,
            max = 150f, progress = 150f, radius = 5.dp, backgroundPadding = 5.dp,
            description = """
            |Max : 150
            |Progress : 150
            |Icon Size : 30dp
            |Icon Padding : 3dp
            |Radius : 5dp
            |Background Padding : 5dp
            |Width : 260dp
            |Height : Wrap Content
            """.trimMargin(),
        )
        IconSampleCard(
            iconRes = R.drawable.ic_television, iconSize = 50.dp, iconPadding = 3.dp,
            max = 150f, progress = 5f, radius = 20.dp, backgroundPadding = 10.dp,
            description = """
            |Max : 150
            |Progress : 5
            |Icon Size : 50dp
            |Icon Padding : 3dp
            |Radius : 20dp
            |Background Padding : 10dp
            |Width : 260dp
            |Height : Wrap Content
            """.trimMargin(),
        )
        IconSampleCard(
            iconRes = R.drawable.ic_television, iconSize = 20.dp, iconPadding = 10.dp,
            max = 150f, progress = 100f, radius = 30.dp, backgroundPadding = 5.dp,
            description = """
            |No Icon Image
            |Max : 150
            |Progress : 100
            |Icon Size : 20dp
            |Icon Padding : 10dp
            |Radius : 30dp
            |Background Padding : 5dp
            |Width : 260dp
            |Height : Wrap Content
            """.trimMargin(),
        )
    }
}

@Composable
private fun IconSampleCard(
    @DrawableRes iconRes: Int,
    iconSize: Dp,
    iconPadding: Dp,
    max: Float,
    progress: Float,
    radius: Dp,
    backgroundPadding: Dp,
    description: String,
    secondaryProgress: Float = 0f,
    reverse: Boolean = false,
) {
    SampleCard {
        IconRoundCornerProgressBar(
            icon = painterResource(iconRes),
            progress = progress,
            modifier = Modifier
                .width(260.dp)
                .height(iconSize + backgroundPadding * 2),
            max = max,
            secondaryProgress = secondaryProgress,
            radius = radius,
            backgroundPadding = backgroundPadding,
            reverse = reverse,
            iconSize = DpSize(iconSize, iconSize),
            iconPadding = PaddingValues(iconPadding),
            iconBackgroundColor = colorResource(R.color.sample_progress_icon_background),
            backgroundColor = colorResource(R.color.sample_progress_background),
            progressColor = colorResource(R.color.sample_progress_primary),
            secondaryProgressColor = colorResource(R.color.sample_progress_secondary),
        )
        SampleDescription(text = description)
    }
}

@Composable
private fun InteractiveIconCard() {
    SampleCard {
        var progress by rememberSaveable { mutableFloatStateOf(30f) }
        var secondaryProgress by rememberSaveable { mutableFloatStateOf(50f) }
        var animationEnabled by rememberSaveable { mutableStateOf(false) }
        var gradientEnabled by rememberSaveable { mutableStateOf(false) }
        IconRoundCornerProgressBar(
            icon = painterResource(R.drawable.ic_android),
            progress = progress,
            modifier = Modifier
                .width(260.dp)
                .height(42.dp),
            max = 200f,
            secondaryProgress = secondaryProgress,
            radius = 8.dp,
            backgroundPadding = 6.dp,
            iconSize = DpSize(30.dp, 30.dp),
            iconPadding = PaddingValues(5.dp),
            iconBackgroundColor = colorResource(R.color.sample_progress_icon_background),
            backgroundColor = colorResource(R.color.sample_progress_background),
            progressColor = colorResource(R.color.sample_progress_primary),
            progressColors = if (gradientEnabled) {
                listOf(
                    colorResource(R.color.sample_progress_gradient_start),
                    colorResource(R.color.sample_progress_gradient_end),
                )
            } else {
                null
            },
            secondaryProgressColor = colorResource(R.color.sample_progress_secondary),
            animationEnabled = animationEnabled,
        )
        ProgressControlRow(centerText = "$progress") { amount ->
            progress = (progress + amount).coerceIn(0f, 200f)
            secondaryProgress = progress + 20f
        }
        LabeledCheckbox(
            label = stringResource(R.string.enable_animation),
            checked = animationEnabled,
            onCheckedChange = { animationEnabled = it },
        )
        LabeledCheckbox(
            label = stringResource(R.string.apply_gradient_progress_color),
            checked = gradientEnabled,
            onCheckedChange = { gradientEnabled = it },
        )
    }
}

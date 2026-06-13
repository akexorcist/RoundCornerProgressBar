package com.akexorcist.roundcornerprogressbar.example.compose

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.akexorcist.roundcornerprogressbar.compose.RoundCornerProgressBar
import com.akexorcist.roundcornerprogressbar.example.R

/**
 * Compose version of `SimpleDemoFragment` with the same samples.
 */
@Composable
fun SimpleDemoScreen() {
    DemoScreenContainer {
        SectionTitle(text = stringResource(R.string.title_round_corner_progress_bar))
        InteractiveSimpleCard()
        SampleCard {
            RoundCornerProgressBar(
                progress = 50f,
                modifier = Modifier
                    .width(260.dp)
                    .height(30.dp),
                max = 100f,
                radius = 0.dp,
                backgroundPadding = 4.dp,
                backgroundColor = colorResource(R.color.sample_progress_background),
                progressColor = colorResource(R.color.sample_progress_primary),
            )
            SampleDescription(
                text = """
                |Max : 100
                |Progress : 50
                |Radius : 0dp
                |Padding : 4dp
                |Width : 260dp
                |Height : 30dp
                """.trimMargin()
            )
        }
        SampleCard {
            RoundCornerProgressBar(
                progress = 40f,
                modifier = Modifier
                    .width(260.dp)
                    .height(30.dp),
                max = 100f,
                secondaryProgress = 60f,
                radius = 10.dp,
                backgroundPadding = 2.dp,
                backgroundColor = colorResource(R.color.sample_progress_background),
                progressColor = colorResource(R.color.sample_progress_primary),
                secondaryProgressColor = colorResource(R.color.sample_progress_secondary),
            )
            SampleDescription(
                text = """
                |Max : 100
                |Progress : 40
                |SecondaryProgress : 60
                |Radius : 10dp
                |Padding : 2dp
                |Width : 260dp
                |Height : 30dp
                """.trimMargin()
            )
        }
        SampleCard {
            RoundCornerProgressBar(
                progress = 20f,
                modifier = Modifier
                    .width(260.dp)
                    .height(30.dp),
                max = 100f,
                secondaryProgress = 75f,
                radius = 80.dp,
                backgroundPadding = 2.dp,
                reverse = true,
                backgroundColor = colorResource(R.color.sample_progress_background),
                progressColor = colorResource(R.color.sample_progress_primary),
                secondaryProgressColor = colorResource(R.color.sample_progress_secondary),
            )
            SampleDescription(
                text = """
                |Max : 100
                |Progress : 20
                |SecondaryProgress : 75
                |Radius : 80dp
                |Padding : 2dp
                |Reverse : True
                |Width : 260dp
                |Height : 30dp
                """.trimMargin()
            )
        }
        SampleCard {
            RoundCornerProgressBar(
                progress = 80f,
                modifier = Modifier
                    .width(260.dp)
                    .height(20.dp),
                max = 100f,
                radius = 20.dp,
                backgroundPadding = 2.dp,
                backgroundColor = colorResource(R.color.sample_progress_background),
                progressColors = listOf(
                    colorResource(R.color.sample_progress_gradient_start),
                    colorResource(R.color.sample_progress_gradient_end),
                ),
            )
            SampleDescription(
                text = """
                |Max : 100
                |Progress : 80
                |Radius : 20dp
                |Padding : 2dp
                |Width : 260dp
                |Height : 20dp
                """.trimMargin()
            )
        }
        SampleCard {
            RoundCornerProgressBar(
                progress = 20f,
                modifier = Modifier
                    .width(260.dp)
                    .height(50.dp),
                max = 200f,
                radius = 20.dp,
                backgroundPadding = 10.dp,
                backgroundColor = colorResource(R.color.sample_progress_background),
                progressColor = colorResource(R.color.sample_progress_primary),
            )
            SampleDescription(
                text = """
                |Max : 200
                |Progress : 20
                |Radius : 20dp
                |Padding : 10dp
                |Width : 260dp
                |Height : 50dp
                """.trimMargin()
            )
        }
    }
}

@Composable
private fun InteractiveSimpleCard() {
    SampleCard {
        var progress by rememberSaveable { mutableFloatStateOf(20f) }
        var secondaryProgress by rememberSaveable { mutableFloatStateOf(30f) }
        var animationEnabled by rememberSaveable { mutableStateOf(false) }
        var gradientEnabled by rememberSaveable { mutableStateOf(false) }
        RoundCornerProgressBar(
            progress = progress,
            modifier = Modifier
                .width(260.dp)
                .height(30.dp),
            max = 100f,
            secondaryProgress = secondaryProgress,
            radius = 10.dp,
            backgroundPadding = 4.dp,
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
            progress = (progress + amount).coerceIn(0f, 100f)
            secondaryProgress = progress + 10f
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

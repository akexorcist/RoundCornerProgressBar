package com.akexorcist.roundcornerprogressbar.example.compose

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akexorcist.roundcornerprogressbar.compose.TextGravity
import com.akexorcist.roundcornerprogressbar.compose.TextPositionPriority
import com.akexorcist.roundcornerprogressbar.compose.TextRoundCornerProgressBar
import com.akexorcist.roundcornerprogressbar.example.R

/**
 * Compose version of `TextDemoFragment` with the same samples.
 */
@Composable
fun TextDemoScreen() {
    DemoScreenContainer {
        SectionTitle(text = stringResource(R.string.title_text_round_corner_progress_bar))
        InteractiveTextCard()
        SampleCard {
            TextRoundCornerProgressBar(
                text = "50",
                progress = 50f,
                modifier = Modifier
                    .width(260.dp)
                    .height(30.dp),
                max = 100f,
                radius = 0.dp,
                backgroundPadding = 4.dp,
                textColor = colorResource(R.color.sample_progress_text_color),
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
            TextRoundCornerProgressBar(
                text = stringResource(R.string.loading),
                progress = 40f,
                modifier = Modifier
                    .width(260.dp)
                    .height(30.dp),
                max = 100f,
                secondaryProgress = 60f,
                radius = 10.dp,
                backgroundPadding = 2.dp,
                textColor = colorResource(R.color.sample_progress_text_color),
                textInsideGravity = TextGravity.End,
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
                |Text Inside Gravity : End
                |Width : 260dp
                |Height : 30dp
                """.trimMargin()
            )
        }
        SampleCard {
            TextRoundCornerProgressBar(
                text = "20",
                progress = 20f,
                modifier = Modifier
                    .width(260.dp)
                    .height(30.dp),
                max = 100f,
                secondaryProgress = 75f,
                radius = 80.dp,
                backgroundPadding = 2.dp,
                reverse = true,
                textColor = colorResource(R.color.sample_progress_text_color),
                textPositionPriority = TextPositionPriority.Outside,
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
                |Text Position Priority : Outside
                |Width : 260dp
                |Height : 30dp
                """.trimMargin()
            )
        }
        SampleCard {
            TextRoundCornerProgressBar(
                text = "80",
                progress = 80f,
                modifier = Modifier
                    .width(260.dp)
                    .height(20.dp),
                max = 100f,
                radius = 20.dp,
                backgroundPadding = 2.dp,
                textColor = colorResource(R.color.sample_progress_text_color),
                textSize = 12.sp,
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
                |Text Size : 12sp
                |Width : 260dp
                |Height : 20dp
                """.trimMargin()
            )
        }
        SampleCard {
            TextRoundCornerProgressBar(
                text = "20",
                progress = 20f,
                modifier = Modifier
                    .width(260.dp)
                    .height(50.dp),
                max = 200f,
                radius = 20.dp,
                backgroundPadding = 10.dp,
                textColor = colorResource(R.color.sample_progress_text_color),
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
private fun InteractiveTextCard() {
    SampleCard {
        var progress by rememberSaveable { mutableFloatStateOf(80f) }
        var animationEnabled by rememberSaveable { mutableStateOf(false) }
        var insideGravityIndex by rememberSaveable { mutableIntStateOf(0) }
        var outsideGravityIndex by rememberSaveable { mutableIntStateOf(0) }
        var priorityIndex by rememberSaveable { mutableIntStateOf(0) }
        TextRoundCornerProgressBar(
            text = "$progress",
            progress = progress,
            modifier = Modifier
                .width(260.dp)
                .height(30.dp),
            max = 255f,
            radius = 10.dp,
            backgroundPadding = 4.dp,
            textColor = colorResource(R.color.sample_progress_text_color),
            textInsideGravity = if (insideGravityIndex == 0) TextGravity.Start else TextGravity.End,
            textOutsideGravity = if (outsideGravityIndex == 0) TextGravity.Start else TextGravity.End,
            textPositionPriority = if (priorityIndex == 0) {
                TextPositionPriority.Inside
            } else {
                TextPositionPriority.Outside
            },
            backgroundColor = colorResource(R.color.sample_progress_background),
            progressColor = colorResource(R.color.sample_progress_primary),
            animationEnabled = animationEnabled,
        )
        ProgressControlRow(centerText = null) { amount ->
            progress = (progress + amount).coerceIn(0f, 255f)
        }
        LabeledCheckbox(
            label = stringResource(R.string.enable_animation),
            checked = animationEnabled,
            onCheckedChange = { animationEnabled = it },
        )
        LabeledRadioGroup(
            label = stringResource(R.string.inside_gravity),
            options = listOf(
                stringResource(R.string.gravity_start),
                stringResource(R.string.gravity_end),
            ),
            selectedIndex = insideGravityIndex,
            onSelected = { insideGravityIndex = it },
        )
        LabeledRadioGroup(
            label = stringResource(R.string.outside_gravity),
            options = listOf(
                stringResource(R.string.gravity_start),
                stringResource(R.string.gravity_end),
            ),
            selectedIndex = outsideGravityIndex,
            onSelected = { outsideGravityIndex = it },
        )
        LabeledRadioGroup(
            label = stringResource(R.string.text_position_priority),
            options = listOf(
                stringResource(R.string.priority_inside),
                stringResource(R.string.priority_outside),
            ),
            selectedIndex = priorityIndex,
            onSelected = { priorityIndex = it },
        )
    }
}

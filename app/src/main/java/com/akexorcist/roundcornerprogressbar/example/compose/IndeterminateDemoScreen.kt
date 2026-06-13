package com.akexorcist.roundcornerprogressbar.example.compose

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.akexorcist.roundcornerprogressbar.compose.IndeterminateCenteredRoundCornerProgressBar
import com.akexorcist.roundcornerprogressbar.compose.IndeterminateRoundCornerProgressBar
import com.akexorcist.roundcornerprogressbar.example.R

/**
 * Compose version of `IndeterminateDemoFragment` with the same samples.
 */
@Composable
fun IndeterminateDemoScreen() {
    DemoScreenContainer {
        SectionTitle(text = stringResource(R.string.title_indeterminate_round_corner_progress_bar))
        SampleCard {
            IndeterminateRoundCornerProgressBar(
                modifier = Modifier
                    .width(260.dp)
                    .height(10.dp),
                backgroundColor = colorResource(R.color.sample_progress_background),
                progressColor = colorResource(R.color.sample_progress_primary),
            )
            SampleDescription(text = "Animation Speed Scale : x1")
        }
        SampleCard {
            IndeterminateRoundCornerProgressBar(
                modifier = Modifier
                    .width(260.dp)
                    .height(10.dp),
                backgroundColor = colorResource(R.color.sample_progress_background),
                progressColor = colorResource(R.color.sample_progress_primary),
                animationSpeedScale = 3f,
            )
            SampleDescription(text = "Animation Speed Scale : x3")
        }
        SectionTitle(text = stringResource(R.string.title_indeterminate_centered_round_corner_progress_bar))
        SampleCard {
            IndeterminateCenteredRoundCornerProgressBar(
                modifier = Modifier
                    .width(260.dp)
                    .height(10.dp),
                backgroundColor = colorResource(R.color.sample_progress_background),
                progressColor = colorResource(R.color.sample_progress_primary),
                animationSpeedScale = 0.5f,
            )
            SampleDescription(text = "Animation Speed Scale : x0.5")
        }
        SampleCard {
            IndeterminateCenteredRoundCornerProgressBar(
                modifier = Modifier
                    .width(260.dp)
                    .height(10.dp),
                backgroundColor = colorResource(R.color.sample_progress_background),
                progressColor = colorResource(R.color.sample_progress_primary),
            )
            SampleDescription(text = "Animation Speed Scale : x1")
        }
    }
}

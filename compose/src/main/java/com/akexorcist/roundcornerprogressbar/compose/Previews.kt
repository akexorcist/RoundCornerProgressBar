package com.akexorcist.roundcornerprogressbar.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
private fun RoundCornerProgressBarPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        RoundCornerProgressBar(
            progress = 60f,
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            radius = 12.dp,
            progressColor = Color(0xFFE91E63),
            secondaryProgress = 80f,
            secondaryProgressColor = Color(0x80E91E63),
        )
        RoundCornerProgressBar(
            progress = 60f,
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            radius = 12.dp,
            reverse = true,
            backgroundPadding = 4.dp,
            progressColors = listOf(Color(0xFF00BCD4), Color(0xFF3F51B5)),
        )
        CenteredRoundCornerProgressBar(
            progress = 50f,
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            radius = 12.dp,
            progressColor = Color(0xFF4CAF50),
        )
        IconRoundCornerProgressBar(
            icon = painterResource(android.R.drawable.btn_star_big_on),
            progress = 40f,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            iconSize = androidx.compose.ui.unit.DpSize(40.dp, 40.dp),
            iconPadding = androidx.compose.foundation.layout.PaddingValues(8.dp),
            iconBackgroundColor = Color(0xFFFF9800),
            radius = 20.dp,
            progressColor = Color(0xFFFFC107),
        )
        TextRoundCornerProgressBar(
            text = "75%",
            progress = 75f,
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp),
            radius = 14.dp,
            progressColor = Color(0xFF9C27B0),
        )
        TextRoundCornerProgressBar(
            text = "10%",
            progress = 10f,
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp),
            radius = 14.dp,
            textColor = Color(0xFFCFD8DC),
            progressColor = Color(0xFF9C27B0),
        )
        IndeterminateRoundCornerProgressBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            radius = 12.dp,
            progressColor = Color(0xFF03A9F4),
        )
        IndeterminateCenteredRoundCornerProgressBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            radius = 12.dp,
            progressColor = Color(0xFF8BC34A),
        )
    }
}

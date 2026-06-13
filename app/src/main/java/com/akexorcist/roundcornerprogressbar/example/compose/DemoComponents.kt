package com.akexorcist.roundcornerprogressbar.example.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akexorcist.roundcornerprogressbar.example.R

@Composable
fun DemoScreenContainer(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = dimensionResource(R.dimen.demo_bottom_clearance),
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content,
    )
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .width(292.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.titleMedium,
    )
}

@Composable
fun SampleCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.padding(bottom = 16.dp),
        shape = RoundedCornerShape(8.dp),
        color = colorResource(R.color.sample_card_background),
        border = BorderStroke(2.dp, colorResource(R.color.sample_card_stroke)),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = content,
        )
    }
}

@Composable
fun SampleDescription(text: String) {
    Text(
        text = text,
        modifier = Modifier.width(260.dp),
        style = MaterialTheme.typography.bodyLarge,
        lineHeight = 25.sp,
    )
}

/**
 * The -20 / -2 / [centerText] / +2 / +20 control row used by every
 * interactive demo, matching the button row of the View demo fragments.
 */
@Composable
fun ProgressControlRow(
    centerText: String?,
    onProgressChange: (amount: Float) -> Unit,
) {
    Row(
        modifier = Modifier.width(260.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        DemoButton(label = "-20") { onProgressChange(-20f) }
        DemoButton(label = "-2") { onProgressChange(-2f) }
        if (centerText != null) {
            Text(
                text = centerText,
                modifier = Modifier.weight(1.2f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
            )
        } else {
            Spacer(modifier = Modifier.weight(1.2f))
        }
        DemoButton(label = "+2") { onProgressChange(2f) }
        DemoButton(label = "+20") { onProgressChange(20f) }
    }
}

@Composable
private fun RowScope.DemoButton(
    label: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp),
    ) {
        Text(text = label, maxLines = 1, fontSize = 16.sp)
    }
}

@Composable
fun LabeledCheckbox(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.width(260.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(text = label)
    }
}

/**
 * A two-option radio group with a label, matching the gravity and position
 * priority radio groups of the View text demo.
 */
@Composable
fun LabeledRadioGroup(
    label: String,
    options: List<String>,
    selectedIndex: Int,
    onSelected: (index: Int) -> Unit,
) {
    Column(modifier = Modifier.width(260.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            options.forEachIndexed { index, option ->
                RadioButton(
                    selected = selectedIndex == index,
                    onClick = { onSelected(index) },
                )
                Text(text = option, modifier = Modifier.padding(end = 16.dp))
            }
        }
    }
}

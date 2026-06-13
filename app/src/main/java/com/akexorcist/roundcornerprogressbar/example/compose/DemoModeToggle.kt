package com.akexorcist.roundcornerprogressbar.example.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DemoModeToggle(
    isViewMode: Boolean,
    onModeChange: (isViewMode: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(percent = 50),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shadowElevation = 6.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ModeLabel(text = "Compose", active = !isViewMode)
            Switch(
                checked = isViewMode,
                onCheckedChange = onModeChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onPrimary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedBorderColor = MaterialTheme.colorScheme.primary,
                ),
            )
            ModeLabel(text = "View", active = isViewMode)
        }
    }
}

@Composable
private fun ModeLabel(text: String, active: Boolean) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
        color = if (active) {
            MaterialTheme.colorScheme.onSurface
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        },
    )
}

private val DemoPrimary = Color(0xFF6200EE)
private val DemoLightColorScheme = lightColorScheme(primary = DemoPrimary)
private val DemoDarkColorScheme = darkColorScheme(primary = DemoPrimary)

@Composable
fun DemoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DemoDarkColorScheme else DemoLightColorScheme,
        content = content,
    )
}

@Preview(name = "Compose active", showBackground = true, backgroundColor = 0xFFE0E0E0)
@Composable
private fun DemoModeTogglePreviewCompose() {
    MaterialTheme {
        DemoModeToggle(isViewMode = false, onModeChange = {})
    }
}

@Preview(name = "View active", showBackground = true, backgroundColor = 0xFFE0E0E0)
@Composable
private fun DemoModeTogglePreviewView() {
    MaterialTheme {
        DemoModeToggle(isViewMode = true, onModeChange = {})
    }
}

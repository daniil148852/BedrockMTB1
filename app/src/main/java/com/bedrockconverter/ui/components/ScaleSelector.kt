// app/src/main/java/com/bedrockconverter/ui/components/ScaleSelector.kt
package com.bedrockconverter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bedrockconverter.ui.theme.BedrockColors
import com.bedrockconverter.ui.theme.BedrockShapes

@Composable
fun ScaleSelector(
    currentScale: Float,
    onScaleSelected: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val presetScales = listOf(0.25f, 0.5f, 1f, 2f, 4f)
    var customScale by remember { mutableFloatStateOf(currentScale) }
    var useCustom by remember { mutableStateOf(!presetScales.contains(currentScale)) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Select Model Scale",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Choose how large your model will appear in Minecraft",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Preset Scale Options
        Text(
            text = "Preset Scales",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            presetScales.forEach { scale ->
                ScaleChip(
                    scale = scale,
                    isSelected = !useCustom && currentScale == scale,
                    onClick = {
                        useCustom = false
                        onScaleSelected(scale)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Custom Scale
        Text(
            text = "Custom Scale",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy

package com.example.sristudio.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class BarChartData(
    val label: String,
    val value: Float,
    val maxValue: Float = 100f
)

@Composable
fun SimpleBarChart(
    data: List<BarChartData>,
    barColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
    showLabels: Boolean = true
) {
    if (data.isEmpty()) return

    val maxVal = data.maxOf { it.maxValue }

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            val barWidth = size.width / (data.size * 2f)
            val spacing = barWidth

            data.forEachIndexed { index, item ->
                val barHeight = if (maxVal > 0) (item.value / maxVal) * size.height else 0f
                val x = index * (barWidth + spacing) + spacing / 2

                // Bar background
                drawRect(
                    color = barColor.copy(alpha = 0.1f),
                    topLeft = Offset(x, 0f),
                    size = Size(barWidth, size.height)
                )

                // Bar value
                drawRect(
                    color = barColor,
                    topLeft = Offset(x, size.height - barHeight),
                    size = Size(barWidth, barHeight)
                )
            }
        }

        if (showLabels) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                data.forEach { item ->
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

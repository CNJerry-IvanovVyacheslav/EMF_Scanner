package com.melongamesinc.emfscanner.presentation.ui.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun EmfRadarView(magnitude: Float, color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val baseRadius = 80.dp.toPx()
        val maxDynamicRadius = (size.minDimension / 2) - baseRadius

        val dynamicScale = magnitude.coerceIn(0f, 150f) / 150f
        val finalRadius = baseRadius + (maxDynamicRadius * dynamicScale)

        drawCircle(
            color = color,
            radius = finalRadius,
            center = this.center,
            style = Stroke(width = 4.dp.toPx()),
            alpha = 0.5f
        )

        drawCircle(
            color = color,
            radius = finalRadius * 0.8f,
            center = this.center,
            alpha = 0.1f
        )
    }
}
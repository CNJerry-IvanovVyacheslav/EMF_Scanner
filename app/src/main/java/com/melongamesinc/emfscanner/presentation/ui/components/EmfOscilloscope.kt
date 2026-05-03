package com.melongamesinc.emfscanner.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun EmfOscilloscope(
    history: List<Float>,
    historyLimit: Int = 200,
    maxScale: Float = 150f
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val warningThreshold = 25f
        val dangerThreshold = 60f

        val warningY = height - (warningThreshold / maxScale * height)
        val dangerY = height - (dangerThreshold / maxScale * height)

        val pathBrush = Brush.verticalGradient(
            0.0f to Color(0xFFFF1744),
            (dangerY / height) to Color(0xFFFF1744),

            (dangerY / height + 0.001f) to Color(0xFFFFD600),
            (warningY / height) to Color(0xFFFFD600),

            (warningY / height + 0.001f) to Color(0xFF00E676),
            1.0f to Color(0xFF00E676)
        )

        val gridLines = 3
        for (i in 0..gridLines) {
            val y = height * i / gridLines
            drawLine(
                color = Color.Gray.copy(alpha = 0.2f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        if (history.size < 2) return@Canvas

        val stepX = width / (historyLimit - 1).coerceAtLeast(1).toFloat()
        val startX = width - ((history.size - 1) * stepX)

        val path = Path().apply {
            history.forEachIndexed { index, value ->
                val x = startX + (index * stepX)
                val y = height - (value.coerceIn(0f, maxScale) / maxScale * height)

                if (index == 0) moveTo(x, y) else lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            brush = pathBrush,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}
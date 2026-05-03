package com.melongamesinc.emfscanner.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.melongamesinc.emfscanner.domain.models.EmfLevel
import com.melongamesinc.emfscanner.presentation.viewmodels.EmfViewModel

@Composable
fun EmfScannerScreen() {
    val viewModel: EmfViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    val animatedMagnitude by animateFloatAsState(
        targetValue = state?.microTesla ?: 0f,
        animationSpec = tween(durationMillis = 500),
        label = "MagnitudeAnimation"
    )

    val targetColor = when (state?.level) {
        EmfLevel.NORMAL -> Color(0xFF00E676)
        EmfLevel.WARNING -> Color(0xFFFFD600)
        EmfLevel.DANGER -> Color(0xFFFF1744)
        null -> Color.Gray
    }

    val animatedColor by animateColorAsState(targetColor, label = "ColorAnimation")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(300.dp)
        ) {
            EmfRadarView(magnitude = animatedMagnitude, color = animatedColor)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = String.format("%.1f", animatedMagnitude),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = animatedColor
                )
                Text(
                    text = "µT",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = state?.level?.name ?: "INITIALIZING...",
            style = MaterialTheme.typography.headlineSmall,
            color = animatedColor,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(48.dp))

        if (state?.isTared == true) {
            OutlinedButton(
                onClick = { viewModel.resetCalibration() },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text("RESET CALIBRATION")
            }
        } else {
            Button(
                onClick = { viewModel.calibrate() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155))
            ) {
                Text("CALIBRATE (TARE)")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Raw Sensor: ${String.format("%.1f", state?.rawMicroTesla ?: 0f)} µT",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun EmfRadarView(magnitude: Float, color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val radiusScale = (magnitude.coerceIn(30f, 200f) / 200f) * size.minDimension / 2

        drawCircle(
            color = color,
            radius = radiusScale,
            center = this.center,
            style = Stroke(width = 4.dp.toPx()),
            alpha = 0.5f
        )

        drawCircle(
            color = color,
            radius = radiusScale * 0.8f,
            center = this.center,
            alpha = 0.1f
        )
    }
}
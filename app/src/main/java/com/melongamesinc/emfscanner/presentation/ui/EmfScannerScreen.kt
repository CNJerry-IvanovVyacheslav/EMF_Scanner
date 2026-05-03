package com.melongamesinc.emfscanner.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.melongamesinc.emfscanner.presentation.ui.components.CalibrationButtons
import com.melongamesinc.emfscanner.presentation.ui.components.EmfOscilloscope
import com.melongamesinc.emfscanner.presentation.ui.components.EmfRadarView
import com.melongamesinc.emfscanner.presentation.viewmodels.EmfViewModel
import com.melongamesinc.emfscanner.utils.getEmfColor

@Composable
fun EmfScannerScreen() {
    val viewModel: EmfViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    val animatedMagnitude by animateFloatAsState(
        targetValue = state?.microTesla ?: 0f,
        animationSpec = tween(durationMillis = 300),
        label = "MagnitudeAnimation"
    )

    val targetColor = getEmfColor(state?.level)
    val animatedColor by animateColorAsState(targetColor, label = "ColorAnimation")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            EmfRadarView(magnitude = animatedMagnitude, color = animatedColor)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = String.format("%.1f", animatedMagnitude),
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Black,
                    color = animatedColor
                )
                Text(text = "µT", fontSize = 18.sp, color = Color.Gray)
            }
        }

        state?.let {
            StatsRow(min = it.min, avg = it.avg, max = it.max)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color(0xFF1E293B), RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            state?.let {
                EmfOscilloscope(
                    history = it.history,
                    historyLimit = it.historyLimit
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        CalibrationButtons(
            isTared = state?.isTared ?: false,
            onCalibrate = { viewModel.calibrate() },
            onReset = { viewModel.resetCalibration() }
        )

        Text(
            text = "Raw Sensor: ${String.format("%.1f", state?.rawMicroTesla ?: 0f)} µT",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun StatsRow(min: Float, avg: Float, max: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(label = "MIN", value = min)
        StatItem(label = "AVG", value = avg, isHighlight = true)
        StatItem(label = "MAX", value = max)
    }
}

@Composable
fun StatItem(label: String, value: Float, isHighlight: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(
            text = String.format("%.1f", value),
            fontSize = 18.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlight) Color.White else Color.LightGray
        )
    }
}
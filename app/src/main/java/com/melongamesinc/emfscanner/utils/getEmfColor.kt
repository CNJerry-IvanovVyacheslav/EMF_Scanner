package com.melongamesinc.emfscanner.utils

import androidx.compose.ui.graphics.Color
import com.melongamesinc.emfscanner.domain.models.EmfLevel

fun getEmfColor(level: EmfLevel?): Color {
    return when (level) {
        EmfLevel.NORMAL -> Color(0xFF00E676)
        EmfLevel.WARNING -> Color(0xFFFFD600)
        EmfLevel.DANGER -> Color(0xFFFF1744)
        null -> Color.Gray
    }
}
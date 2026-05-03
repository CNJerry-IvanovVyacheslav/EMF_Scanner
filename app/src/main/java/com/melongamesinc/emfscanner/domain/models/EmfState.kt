package com.melongamesinc.emfscanner.domain.models

data class EmfState(
    val microTesla: Float,
    val rawMicroTesla: Float,
    val level: EmfLevel,
    val isTared: Boolean
)
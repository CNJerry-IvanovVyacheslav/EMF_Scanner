package com.melongamesinc.emfscanner.domain.models

data class EmfState(
    val microTesla: Float,
    val rawMicroTesla: Float,
    val level: EmfLevel,
    val isTared: Boolean,
    val min: Float = 0f,
    val max: Float = 0f,
    val avg: Float = 0f,
    val history: List<Float> = emptyList(),
    val historyLimit: Int = 200
)
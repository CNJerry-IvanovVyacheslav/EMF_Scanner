package com.melongamesinc.emfscanner.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melongamesinc.emfscanner.domain.models.EmfState
import com.melongamesinc.emfscanner.domain.usecases.ObserveEmfStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class EmfViewModel @Inject constructor(
    private val observeEmfStateUseCase: ObserveEmfStateUseCase
) : ViewModel() {

    private val baselineFlow = MutableStateFlow(0f)
    private val historyLimit = 200
    private val historyQueue = ArrayDeque<Float>(historyLimit)

    val uiState: StateFlow<EmfState?> = observeEmfStateUseCase(baselineFlow)
        .map { state ->
            if (historyQueue.size >= historyLimit) historyQueue.removeFirst()
            historyQueue.addLast(state.microTesla)

            val currentHistory = historyQueue.toList()

            state.copy(
                min = currentHistory.minOrNull() ?: 0f,
                max = currentHistory.maxOrNull() ?: 0f,
                avg = currentHistory.average().toFloat(),
                history = currentHistory,
                historyLimit = this.historyLimit
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun calibrate() {
        uiState.value?.let { currentState ->
            baselineFlow.value = currentState.rawMicroTesla
        }
    }

    fun resetCalibration() {
        baselineFlow.value = 0f
    }
}
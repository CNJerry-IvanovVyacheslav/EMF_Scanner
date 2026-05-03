package com.melongamesinc.emfscanner.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melongamesinc.emfscanner.domain.models.EmfState
import com.melongamesinc.emfscanner.domain.usecases.ObserveEmfStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class EmfViewModel @Inject constructor(
    observeEmfStateUseCase: ObserveEmfStateUseCase
) : ViewModel() {

    val uiState: StateFlow<EmfState?> = observeEmfStateUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}
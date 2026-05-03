package com.melongamesinc.emfscanner.domain.usecases

import com.melongamesinc.emfscanner.domain.models.EmfLevel
import com.melongamesinc.emfscanner.domain.models.EmfState
import com.melongamesinc.emfscanner.domain.repository.EmfRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveEmfStateUseCase @Inject constructor(
    private val repository: EmfRepository
) {
    private companion object {
        const val THRESHOLD_WARNING = 70.0f
        const val THRESHOLD_DANGER = 150.0f
    }

    operator fun invoke(): Flow<EmfState> {
        return repository.observeRawEmf().map { rawValue ->
            val level = when {
                rawValue >= THRESHOLD_DANGER -> EmfLevel.DANGER
                rawValue >= THRESHOLD_WARNING -> EmfLevel.WARNING
                else -> EmfLevel.NORMAL
            }

            EmfState(
                microTesla = rawValue,
                level = level
            )
        }
    }
}
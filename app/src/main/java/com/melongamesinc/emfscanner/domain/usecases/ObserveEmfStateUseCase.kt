package com.melongamesinc.emfscanner.domain.usecases

import com.melongamesinc.emfscanner.domain.models.EmfLevel
import com.melongamesinc.emfscanner.domain.models.EmfState
import com.melongamesinc.emfscanner.domain.repository.EmfRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveEmfStateUseCase @Inject constructor(
    private val repository: EmfRepository
) {
    private companion object {
        const val THRESHOLD_WARNING = 25.0f
        const val THRESHOLD_DANGER = 60.0f
    }

    operator fun invoke(baselineFlow: Flow<Float>): Flow<EmfState> {
        return repository.observeRawEmf().combine(baselineFlow) { rawValue, baseline ->
            val relativeValue = maxOf(0f, rawValue - baseline)

            val level = when {
                relativeValue >= THRESHOLD_DANGER -> EmfLevel.DANGER
                relativeValue >= THRESHOLD_WARNING -> EmfLevel.WARNING
                else -> EmfLevel.NORMAL
            }

            EmfState(
                microTesla = relativeValue,
                rawMicroTesla = rawValue,
                level = level,
                isTared = baseline > 0f
            )
        }
    }
}
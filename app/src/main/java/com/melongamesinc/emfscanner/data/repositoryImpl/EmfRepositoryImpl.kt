package com.melongamesinc.emfscanner.data.repositoryImpl

import com.melongamesinc.emfscanner.domain.repository.EmfRepository
import com.melongamesinc.emfscanner.utils.EmfSensorManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EmfRepositoryImpl @Inject constructor(
    private val sensorManager: EmfSensorManager
) : EmfRepository {

    override fun observeRawEmf(): Flow<Float> {
        return sensorManager.getMagneticFieldFlow()
    }
}
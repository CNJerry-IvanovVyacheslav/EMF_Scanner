package com.melongamesinc.emfscanner.domain.repository

import kotlinx.coroutines.flow.Flow

interface EmfRepository {
    fun observeRawEmf(): Flow<Float>
}
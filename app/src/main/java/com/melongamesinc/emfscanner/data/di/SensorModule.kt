package com.melongamesinc.emfscanner.data.di

import android.content.Context
import com.melongamesinc.emfscanner.utils.EmfSensorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SensorModule {

    @Provides
    @Singleton
    fun provideEmfSensorManager(
        @ApplicationContext context: Context
    ): EmfSensorManager {
        return EmfSensorManager(context)
    }
}
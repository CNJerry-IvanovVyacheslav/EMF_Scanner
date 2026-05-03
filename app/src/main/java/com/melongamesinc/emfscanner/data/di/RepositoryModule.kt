package com.melongamesinc.emfscanner.data.di

import com.melongamesinc.emfscanner.data.repositoryImpl.EmfRepositoryImpl
import com.melongamesinc.emfscanner.domain.repository.EmfRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindEmfRepository(
        impl: EmfRepositoryImpl
    ): EmfRepository
}
package com.mollo.myapplication.di

import com.mollo.myapplication.repository.repositoryImplementation.ConvertionServiceInterfaceImpl
import com.mollo.myapplication.repository.repositoryInterface.ConvertionServiceInterface
import com.mollo.myapplication.services.ConvertionService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideApiService(conversionService: ConvertionService): ConvertionServiceInterface {
        return ConvertionServiceInterfaceImpl(conversionService)
    }
}
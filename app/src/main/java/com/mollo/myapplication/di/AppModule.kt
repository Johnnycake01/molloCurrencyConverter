package com.mollo.myapplication.di

import android.content.Context
import com.mollo.myapplication.di.application.MyConverterApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    internal fun provideApplication(@ApplicationContext app:Context):MyConverterApp{
        return app as MyConverterApp
    }
}
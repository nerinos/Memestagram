package com.example.snplc.di

import com.example.snplc.repositories.DefaultMainRepository
import com.example.snplc.repositories.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object MainModule {

    @ViewModelScoped
    @Provides
    fun provideMainRepository() = DefaultMainRepository() as MainRepository
}
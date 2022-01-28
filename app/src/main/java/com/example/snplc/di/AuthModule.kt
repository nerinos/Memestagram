package com.example.snplc.di

import com.example.snplc.repositories.AuthRepository
import com.example.snplc.repositories.DefaultAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {

    @ViewModelScoped // singleton for activity scope
    @Provides
    fun provideAuthRepository() = DefaultAuthRepository() as AuthRepository // dagger не кастит
}
package com.srnyndrs.android.lemon.di

import com.srnyndrs.android.lemon.data.service.SupabaseAuthenticationService
import com.srnyndrs.android.lemon.domain.authentication.AuthenticationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {
    @Provides
    @Singleton
    fun provideAuthenticationService(): AuthenticationService {
        return SupabaseAuthenticationService()
    }
}
package com.srnyndrs.android.lemon.di

import com.srnyndrs.android.lemon.data.authentication.SupabaseSessionManager
import com.srnyndrs.android.lemon.data.service.SupabaseAuthenticationService
import com.srnyndrs.android.lemon.domain.authentication.AuthenticationService
import com.srnyndrs.android.lemon.domain.authentication.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {
    @Provides
    @Singleton
    fun provideAuthenticationService(supabaseClient: SupabaseClient): AuthenticationService {
        return SupabaseAuthenticationService(supabaseClient)
    }

    @Provides
    @Singleton
    fun provideSessionManager(supabaseClient: SupabaseClient): SessionManager {
        return SupabaseSessionManager(supabaseClient)
    }
}
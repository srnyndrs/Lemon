package com.srnyndrs.android.lemon.di

import com.srnyndrs.android.lemon.data.database.SupabaseUserRepository
import com.srnyndrs.android.lemon.domain.database.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideUserRepository(
        supabaseClient: SupabaseClient
    ): UserRepository {
        return SupabaseUserRepository(supabaseClient)
    }
}
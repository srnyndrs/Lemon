package com.srnyndrs.android.lemon.di

import com.srnyndrs.android.lemon.data.database.SupabaseCategoryRepository
import com.srnyndrs.android.lemon.data.database.SupabasePaymentMethodRepository
import com.srnyndrs.android.lemon.data.database.SupabaseTransactionRepository
import com.srnyndrs.android.lemon.data.database.SupabaseUserRepository
import com.srnyndrs.android.lemon.domain.database.CategoryRepository
import com.srnyndrs.android.lemon.data.database.SupabaseHouseholdRepository
import com.srnyndrs.android.lemon.domain.database.HouseholdRepository
import com.srnyndrs.android.lemon.domain.database.PaymentMethodRepository
import com.srnyndrs.android.lemon.domain.database.TransactionRepository
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

    @Provides
    @Singleton
    fun provideCategoryRepository(
        supabaseClient: SupabaseClient
    ): CategoryRepository {
        return SupabaseCategoryRepository(supabaseClient)
    }

    @Provides
    @Singleton
    fun providePaymentMethodRepository(
        supabaseClient: SupabaseClient
    ): PaymentMethodRepository {
        return SupabasePaymentMethodRepository(supabaseClient)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(
        supabaseClient: SupabaseClient
    ): TransactionRepository {
        return SupabaseTransactionRepository(supabaseClient)
    }

    @Provides
    @Singleton
    fun provideHouseholdRepository(
        supabaseClient: SupabaseClient
    ): HouseholdRepository {
        return SupabaseHouseholdRepository(supabaseClient)
    }
}
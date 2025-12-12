package com.srnyndrs.android.lemon.di

import android.content.Context
import com.srnyndrs.android.lemon.data.storage.SupabasePictureRepository
import com.srnyndrs.android.lemon.domain.storage.PictureRepository
import com.srnyndrs.android.lemon.domain.storage.usecase.UploadProfilePictureUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun providePictureRepository(
        @ApplicationContext context: Context,
        client: SupabaseClient
    ): PictureRepository {
        return SupabasePictureRepository(client)
    }

    @Provides
    @Singleton
    fun provideUploadProfilePictureUseCase(
        @ApplicationContext context: Context,
        pictureRepository: PictureRepository
    ) = UploadProfilePictureUseCase(
        context,
        pictureRepository
    )

}
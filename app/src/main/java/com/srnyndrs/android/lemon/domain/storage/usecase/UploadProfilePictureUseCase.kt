package com.srnyndrs.android.lemon.domain.storage.usecase

import android.content.Context
import android.net.Uri
import com.srnyndrs.android.lemon.domain.storage.PictureRepository
import javax.inject.Inject

class UploadProfilePictureUseCase(
    private val context: Context,
    private val pictureRepository: PictureRepository
) {
    suspend operator fun invoke(userId: String, imageFile: Uri): Result<String> {
        return try {
            val imageBytes = readBytes(context, imageFile)
                ?: return Result.failure(Exception("Failed to read image file"))
            val result = pictureRepository.uploadProfilePicture(
                userId,
                imageBytes
            )

            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private fun readBytes(context: Context, uri: Uri): ByteArray? =
        context.contentResolver.openInputStream(uri)?.use { it.buffered().readBytes() }

}
package com.srnyndrs.android.lemon.domain.storage

interface PictureRepository {
    suspend fun uploadProfilePicture(userId: String, imageFile: ByteArray): String
}
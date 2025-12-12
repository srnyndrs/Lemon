package com.srnyndrs.android.lemon.data.storage

import android.util.Log
import com.srnyndrs.android.lemon.domain.storage.PictureRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.UploadData
import io.github.jan.supabase.storage.UploadOptionBuilder
import io.github.jan.supabase.storage.storage
import io.ktor.http.ContentType
import java.io.File
import javax.inject.Inject

class SupabasePictureRepository @Inject constructor(
    private val client: SupabaseClient
): PictureRepository {
    override suspend fun uploadProfilePicture(
        userId: String,
        imageFile: ByteArray
    ): String {
        Log.d("SupabasePictureRepo", "uploadProfilePicture() called with: userId = $userId, imageFile size = ${imageFile.size}")

        return try {
            val result = client.storage.from("profile_picture").upload(
                // Store in a per-user folder to satisfy RLS policies
                path = "$userId/profile.png",
                data = imageFile,
                // Use the DSL builder to properly set options
                options = {
                    upsert = true
                    contentType = ContentType.Image.PNG
                }
            )
            Log.d("SupabasePictureRepo", "uploadProfilePicture() returned: ${result.path}")

            result.path
        } catch (e: Exception) {
            Log.e("SupabasePictureRepo", "uploadProfilePicture() error before upload: ", e)

            ""
        }
    }
}
package com.srnyndrs.android.lemon.data.authentication

import com.srnyndrs.android.lemon.domain.authentication.AuthenticationService
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class SupabaseAuthenticationService @Inject constructor(
    private val client: SupabaseClient
): AuthenticationService {
    override suspend fun loginWithEmail(email: String, password: String): Result<Unit> {
        return try {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerWithEmail(email: String, password: String): Result<Unit> {
        return try {
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                this.data = buildJsonObject {
                    // TODO: get username by parameter
                    put("display_name", "([^@]+)".toRegex().find(email)?.groups?.get(1)?.value)
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
package com.srnyndrs.android.lemon.data.authentication

import android.util.Log
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

    private val _tag = "SupabaseAuthService"

    override suspend fun loginWithEmail(email: String, password: String): Result<Unit> {
        Log.d(_tag, "loginWithEmail() called with: email = $email")
        return try {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Log.d(_tag, "loginWithEmail() returned: Unit")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(_tag, "loginWithEmail() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun registerWithEmail(email: String, password: String): Result<Unit> {
        Log.d(_tag, "registerWithEmail() called with: email = $email")
        return try {
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                this.data = buildJsonObject {
                    // TODO: get username by parameter
                    put("display_name", "([^@]+)".toRegex().find(email)?.groups?.get(1)?.value)
                }
            }
            Log.d(_tag, "registerWithEmail() returned: Unit")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(_tag, "registerWithEmail() failed", e)
            Result.failure(e)
        }
    }
}
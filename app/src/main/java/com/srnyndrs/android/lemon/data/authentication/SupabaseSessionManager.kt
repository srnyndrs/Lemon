package com.srnyndrs.android.lemon.data.authentication

import android.util.Log
import com.srnyndrs.android.lemon.domain.authentication.SessionManager
import com.srnyndrs.android.lemon.domain.authentication.model.AuthStatus
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.SignOutScope
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SupabaseSessionManager @Inject constructor(
    private val client: SupabaseClient
): SessionManager {

    private val _tag = "SupabaseSessionManager"

    override fun listenSessionStatus(): Flow<AuthStatus> {
        Log.d(_tag, "listenSessionStatus() called")
        return flow {
            client.auth.sessionStatus.collect {
                Log.d(_tag, "listenSessionStatus() emitted: $it")
                when(it) {
                    is SessionStatus.Authenticated -> {
                        emit(AuthStatus.Authenticated(it.session))
                    }
                    is SessionStatus.NotAuthenticated -> {
                        emit(AuthStatus.Unauthenticated)
                    }
                    else -> {
                        emit(AuthStatus.Unknown)
                    }
                }
            }
        }
    }

    override suspend fun logout(): Result<Unit> {
        Log.d(_tag, "logout() called")
        try {
            client.auth.signOut(SignOutScope.LOCAL)
        } catch (e: Exception) {
            Log.e(_tag, "logout() failed", e)
            return Result.failure(e)
        }
        Log.d(_tag, "logout() returned: Unit")
        return Result.success(Unit)
    }
}
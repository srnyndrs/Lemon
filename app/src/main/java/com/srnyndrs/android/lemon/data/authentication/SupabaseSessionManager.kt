package com.srnyndrs.android.lemon.data.authentication

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
    override fun listenSessionStatus(): Flow<AuthStatus> {
        return flow {
            client.auth.sessionStatus.collect {
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
        try {
            client.auth.signOut(SignOutScope.LOCAL)
        } catch (e: Exception) {
            return Result.failure(e)
        }
        return Result.success(Unit)
    }
}
package com.srnyndrs.android.lemon.data.authentication

import com.srnyndrs.android.lemon.domain.authentication.SessionManager
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
    override fun listenSessionStatus(): Flow<com.srnyndrs.android.lemon.domain.authentication.model.SessionStatus> {
        return flow {
            client.auth.sessionStatus.collect {
                when(it) {
                    is SessionStatus.Authenticated -> {
                        emit(com.srnyndrs.android.lemon.domain.authentication.model.SessionStatus.Authenticated(it.session))
                    }
                    is SessionStatus.NotAuthenticated -> {
                        emit(com.srnyndrs.android.lemon.domain.authentication.model.SessionStatus.Unauthenticated)
                    }
                    else -> {
                        emit(com.srnyndrs.android.lemon.domain.authentication.model.SessionStatus.Unknown)
                    }
                }
            }
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            client.auth.signOut(SignOutScope.LOCAL)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
package com.srnyndrs.android.lemon.domain.database.usecase.user

import com.srnyndrs.android.lemon.domain.database.UserRepository
import javax.inject.Inject

class UpdateUsernameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String, newUsername: String): Result<Unit> {
        return userRepository.updateUsername(userId, newUsername)
    }
}

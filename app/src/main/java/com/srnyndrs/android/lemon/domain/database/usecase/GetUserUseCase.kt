package com.srnyndrs.android.lemon.domain.database.usecase

import com.srnyndrs.android.lemon.domain.database.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String) = userRepository.getUser(userId)
}
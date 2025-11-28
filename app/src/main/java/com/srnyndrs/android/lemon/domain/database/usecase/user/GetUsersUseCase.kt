package com.srnyndrs.android.lemon.domain.database.usecase.user

import com.srnyndrs.android.lemon.domain.database.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(currentUserId: String):  Result<List<Pair<String, String>>> {
        return userRepository.getUsers(currentUserId)
    }
}
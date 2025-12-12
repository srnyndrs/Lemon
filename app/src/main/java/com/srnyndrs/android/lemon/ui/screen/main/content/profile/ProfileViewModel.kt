package com.srnyndrs.android.lemon.ui.screen.main.content.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srnyndrs.android.lemon.domain.database.usecase.user.UpdateUsernameUseCase
import com.srnyndrs.android.lemon.domain.storage.usecase.UploadProfilePictureUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = ProfileViewModel.ProfileViewModelFactory::class)
class ProfileViewModel @AssistedInject constructor(
    @Assisted private val userId: String,
    private val updateUsernameUseCase: UpdateUsernameUseCase,
    private val uploadProfilePictureUseCase: UploadProfilePictureUseCase
): ViewModel() {

    @AssistedFactory
    interface ProfileViewModelFactory {
        fun create(userId: String): ProfileViewModel
    }

    fun onEvent(event: ProfileEvent) = viewModelScope.launch {
        when (event) {
            is ProfileEvent.UpdateUsername -> {
                updateUsernameUseCase(userId, event.newUsername)
            }
            is ProfileEvent.UploadProfilePicture -> {
                uploadProfilePictureUseCase(userId, event.imageUri)
            }
        }
    }

}
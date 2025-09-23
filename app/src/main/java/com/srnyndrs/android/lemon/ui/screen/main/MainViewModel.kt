package com.srnyndrs.android.lemon.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srnyndrs.android.lemon.domain.database.model.User
import com.srnyndrs.android.lemon.domain.database.usecase.GetUserUseCase
import com.srnyndrs.android.lemon.ui.utils.UiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = MainViewModel.MainViewModelFactory::class)
class MainViewModel @AssistedInject constructor(
    @Assisted private val userId: String,
    private val getUserUseCase: GetUserUseCase
): ViewModel() {

    @AssistedFactory
    interface MainViewModelFactory {
        fun create(userId: String): MainViewModel
    }

    private val _user = MutableStateFlow<UiState<User>>(UiState.Empty())
    val user = _user.asStateFlow()
        .onStart {
            // TODO
            //fetchUser()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Empty()
        )

    private fun fetchUser() = viewModelScope.launch {
        _user.value = UiState.Loading()
        getUserUseCase(userId).fold(
            onSuccess = {
                _user.value = UiState.Success(it)
            },
            onFailure = {
                _user.value = UiState.Error("Failed to fetch user: ${it.message}")
            }
        )
    }


}
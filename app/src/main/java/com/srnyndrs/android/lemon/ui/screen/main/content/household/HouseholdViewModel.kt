package com.srnyndrs.android.lemon.ui.screen.main.content.household

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srnyndrs.android.lemon.domain.database.usecase.household.AllHouseholdUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.user.GetUserUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.user.GetUsersUseCase
import com.srnyndrs.android.lemon.ui.screen.main.content.household.HouseholdEvent
import com.srnyndrs.android.lemon.ui.screen.main.content.household.HouseholdState
import com.srnyndrs.android.lemon.ui.utils.UiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = HouseholdViewModel.HouseholdViewModelFactory::class)
class HouseholdViewModel @AssistedInject constructor(
    @Assisted private val householdId: String,
    private val allHouseholdUseCase: AllHouseholdUseCase,
    private val getUsersUseCase: GetUsersUseCase,
) : ViewModel() {

    @AssistedFactory
    interface HouseholdViewModelFactory {
        fun create(householdId: String): HouseholdViewModel
    }

    private val _uiState = MutableStateFlow(HouseholdState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchHousehold()
        fetchUsers()
    }

    fun onEvent(event: HouseholdEvent) {
        when (event) {
            is HouseholdEvent.AddMember -> addMember(event.userId, event.role)
            is HouseholdEvent.RemoveMember -> {
                // TODO: enable remove
                //removeMember(event.userId)
            }
            is HouseholdEvent.UpdateMemberRole -> updateMemberRole(event.userId, event.role)
            is HouseholdEvent.UpdateHouseholdName -> updateHouseholdName(event.name)
            HouseholdEvent.DeleteHousehold -> deleteHousehold()
        }
    }

    private fun fetchHousehold() {
        viewModelScope.launch {
            _uiState.update { it.copy(household = UiState.Loading()) }
            allHouseholdUseCase.getHouseholdUseCase(householdId)
                .onSuccess { household ->
                    _uiState.update { it.copy(household = UiState.Success(household)) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(household = UiState.Error(error.message ?: "Unknown error"))
                    }
                }
        }
    }

    private fun fetchUsers() = viewModelScope.launch {
        _uiState.update { it.copy(users = UiState.Loading()) }
        getUsersUseCase()
            .onSuccess { users ->
                _uiState.update { it.copy(users = UiState.Success(users)) }
            }
            .onFailure { error ->
                _uiState.update { it.copy(users = UiState.Error(error.message ?: "Unknown error")) }
            }
    }

    private fun addMember(userId: String, role: String) {
        // TODO
        viewModelScope.launch {
            allHouseholdUseCase.addMemberUseCase(householdId, userId, role)
                .onSuccess { fetchHousehold() }
                .onFailure { error ->
                    _uiState.update { it.copy(household = UiState.Error(error.message ?: "Unknown error")) }
                }
        }
    }

    private fun removeMember(userId: String) {
        viewModelScope.launch {
            allHouseholdUseCase.removeMemberUseCase(householdId, userId)
                .onSuccess { fetchHousehold() }
                .onFailure { error ->
                    _uiState.update { it.copy(household = UiState.Error(error.message ?: "Unknown error")) }
                }
        }
    }

    private fun updateMemberRole(userId: String, role: String) {
        // TODO
        viewModelScope.launch {
            allHouseholdUseCase.updateMemberRoleUseCase(householdId, userId, role)
                .onSuccess { fetchHousehold() }
                .onFailure { error ->
                    _uiState.update { it.copy(household = UiState.Error(error.message ?: "Unknown error")) }
                }
        }
    }

    private fun updateHouseholdName(name: String) {
        viewModelScope.launch {
            allHouseholdUseCase.updateHouseholdNameUseCase(householdId, name)
                .onSuccess { fetchHousehold() }
                .onFailure { error ->
                    _uiState.update { it.copy(household = UiState.Error(error.message ?: "Unknown error")) }
                }
        }
    }

    private fun deleteHousehold() {
        viewModelScope.launch {
            allHouseholdUseCase.deleteHouseholdUseCase(householdId)
                .onFailure { error ->
                    _uiState.update { it.copy(household = UiState.Error(error.message ?: "Unknown error")) }
                }
        }
    }
}
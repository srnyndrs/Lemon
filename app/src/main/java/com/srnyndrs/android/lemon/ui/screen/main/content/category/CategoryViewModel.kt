package com.srnyndrs.android.lemon.ui.screen.main.content.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.domain.database.usecase.category.AllCategoryUseCase
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = CategoryViewModel.CategoryViewModelFactory::class)
class CategoryViewModel @AssistedInject constructor(
    @Assisted private val householdId: String,
    private val allCategoryUseCase: AllCategoryUseCase
) : ViewModel() {

    @AssistedFactory
    interface CategoryViewModelFactory {
        fun create(householdId: String): CategoryViewModel
    }

    private val _categoryState = MutableStateFlow(CategoryState())
    val categoryState = _categoryState.asStateFlow()
        .onStart {
            fetchCategories()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CategoryState(),
        )


    fun onEvent(event: CategoryEvent) = viewModelScope.launch {
        when(event) {
            is CategoryEvent.AddCategory -> {
                _categoryState.update { it.copy(actionProgress = true) }
                allCategoryUseCase.addCategoryUseCase(event.category, householdId).fold(
                    onSuccess = {
                        fetchCategories()
                    },
                    onFailure = {
                        // TODO: Handle states
                    }
                )
                _categoryState.update { it.copy(actionProgress = false) }
            }
            is CategoryEvent.DeleteCategory -> {
                allCategoryUseCase.deleteCategoryUseCase(event.categoryId)
                // TODO: Handle states
            }
        }
    }

    private fun fetchCategories() = viewModelScope.launch {
        _categoryState.update { it.copy(categories = UiState.Loading()) }
        allCategoryUseCase.getCategoriesUseCase(householdId).let { response ->
            response.fold(
                onSuccess = { categories ->
                    _categoryState.update {
                        it.copy(
                            categories = UiState.Success(categories)
                        )
                    }
                },
                onFailure = { exception ->
                    _categoryState.update {
                        it.copy(
                            categories = UiState.Error(exception.message ?: "Unknown error")
                        )
                    }
                }
            )
        }
    }
}
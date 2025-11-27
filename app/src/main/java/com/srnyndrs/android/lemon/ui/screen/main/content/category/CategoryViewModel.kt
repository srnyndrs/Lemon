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
import kotlinx.coroutines.delay
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
                _categoryState.update { it.copy(actionStatus = UiState.Loading()) }
                allCategoryUseCase.addCategoryUseCase(event.category, householdId).fold(
                    onSuccess = { category ->
                        val categories = if(_categoryState.value.categories is UiState.Success) (_categoryState.value.categories as UiState.Success<List<Category>>).data else emptyList()
                        _categoryState.update {
                            it.copy(
                                actionStatus = UiState.Success(Unit),
                                categories = UiState.Success(
                                    categories.plus(category).sortedBy { it.name }
                                )
                            )
                        }
                    },
                    onFailure = { exception ->
                        _categoryState.update { it.copy(actionStatus = UiState.Error(exception.message ?: "Unknown error")) }
                    }
                )
            }
            is CategoryEvent.DeleteCategory -> {
                _categoryState.update { it.copy(actionStatus = UiState.Loading()) }
                allCategoryUseCase.deleteCategoryUseCase(event.categoryId).fold(
                    onSuccess = { categoryId ->
                        val categories = if(_categoryState.value.categories is UiState.Success) (_categoryState.value.categories as UiState.Success<List<Category>>).data else emptyList()
                        val deletedCategory = categories.find {it.id == categoryId}
                        _categoryState.update {
                            it.copy(
                                actionStatus = UiState.Success(Unit),
                                categories = UiState.Success(
                                    deletedCategory?.let { category ->
                                        categories.minus(category).sortedBy { it.name }
                                    } ?: categories
                                )
                            )
                        }
                        _categoryState.update { it.copy(actionStatus = UiState.Success(Unit)) }
                    },
                    onFailure = { exception ->
                        _categoryState.update { it.copy(actionStatus = UiState.Error(exception.message ?: "Unknown error")) }
                    }
                )
            }
            is CategoryEvent.UpdateCategory -> {
                _categoryState.update { it.copy(actionStatus = UiState.Loading()) }
                allCategoryUseCase.updateCategoryUseCase(event.category).fold(
                    onSuccess = { categoryId ->
                        val categories = if(_categoryState.value.categories is UiState.Success) (_categoryState.value.categories as UiState.Success<List<Category>>).data else emptyList()
                        val updatedCategory = categories.find { it.id == categoryId }
                        _categoryState.update {
                            it.copy(
                                actionStatus = UiState.Success(Unit),
                                categories = UiState.Success(
                                    updatedCategory?.let { category ->
                                        categories.minus(category).plus(event.category).sortedBy { it.name }
                                    } ?: categories
                                )
                            )
                        }
                        _categoryState.update { it.copy(actionStatus = UiState.Success(Unit)) }
                    },
                    onFailure = { exception ->
                        _categoryState.update { it.copy(actionStatus = UiState.Error(exception.message ?: "Unknown error")) }
                    }
                )
            }
        }
        delay(5000L)
        _categoryState.update { it.copy(actionStatus = UiState.Empty()) }
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
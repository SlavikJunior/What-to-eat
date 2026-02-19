package com.example.whattoeat.presentation.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whattoeat.domain.domainEntities.common.Recipe
import com.example.whattoeat.domain.useCases.GetUsersRecipeByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UsersRecipeDetailModelState {
    data object DefaultState : UsersRecipeDetailModelState
    data object LoadingState : UsersRecipeDetailModelState
    data class ErrorState(val cause: Throwable?) : UsersRecipeDetailModelState
}

data class UsersRecipeDetailModel(
    val modelState: UsersRecipeDetailModelState = UsersRecipeDetailModelState.DefaultState,
    val recipe: Recipe.RecipeByUser? = null,
)

sealed interface UsersRecipeDetailPageEvent {
    data class LoadRecipe(val recipeId: Int) : UsersRecipeDetailPageEvent
}

@HiltViewModel
class UsersRecipeDetailViewModel @Inject constructor(
    private val getUsersRecipeById: GetUsersRecipeByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsersRecipeDetailModel())
    val uiState = _uiState.asStateFlow()

    fun reduce(event: UsersRecipeDetailPageEvent) {
        when (event) {
            is UsersRecipeDetailPageEvent.LoadRecipe -> onLoadRecipe(event)
        }
    }

    private fun onLoadRecipe(event: UsersRecipeDetailPageEvent.LoadRecipe) {
        _uiState.update {
            it.copy(modelState = UsersRecipeDetailModelState.LoadingState)
        }

        viewModelScope.launch {
            try {
                val recipeById = getUsersRecipeById(event.recipeId)
                if (recipeById != null) {
                    _uiState.update {
                        it.copy(
                            recipe = recipeById,
                            modelState = UsersRecipeDetailModelState.DefaultState
                        )
                    }
                    return@launch
                } else
                    _uiState.update {
                        it.copy(modelState = UsersRecipeDetailModelState.ErrorState(null))
                    }
            } catch (cause: Throwable) {
                _uiState.update {
                    it.copy(modelState = UsersRecipeDetailModelState.ErrorState(cause))
                }
            }
        }
    }
}
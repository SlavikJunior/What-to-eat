package com.example.whattoeat.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.whattoeat.data.net.client.RussianFoodComClient
import com.example.whattoeat.data.net.repository.RecipeSearchRepositoryImpl
import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.domain_entities.support.RecipeSearch
import com.example.whattoeat.domain.use_cases.GetRecipesUC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class Cuisines {
    ALL,
    RUSSIAN
}

open class RecipeListError(override val cause: Throwable?) : Throwable(cause) {
    data class NotEnoughArgumentsError(override val cause: Throwable?) : RecipeListError(cause)
    data class ConflictFilterError(override val cause: Throwable?) : RecipeListError(cause)
    data class SearchError(override val cause: Throwable?) : RecipeListError(cause)
}

sealed interface RecipeListState {
    data object SearchReadyState : RecipeListState
    data object SearchingState : RecipeListState
    data class ErrorState(private val error: RecipeListError) : RecipeListState
}

data class RecipeListFilter(
    private val recipeTitle: String = "", // ye
    private val cuisine: Cuisines = Cuisines.RUSSIAN, // ye
    private val isVegan: Boolean = false, // ye
    private val isSearchByDescription: Boolean = false, // ye
    private val includedProducts: String = "", // ye
    private val excludedProducts: String = "", // ye
    private val isNeedVideo: Boolean = false, // ye
    private val isNeedSteps: Boolean = false, // ye
    private val isAllProductsAndNotContained: Boolean = false, // ye
)

data class RecipeListModel(
    val state: RecipeListState = RecipeListState.SearchReadyState,
    val recipes: List<Recipe> = listOf(),
    val filter: RecipeListFilter = RecipeListFilter(),
    val isErrorShowing: Boolean = true,
    private val isSearchButtonEnabled: Boolean = true,
    private val isListShowing: Boolean = false, // ye
)

fun RecipeListModel.getStateAfterSearchError(cause: Throwable) =
    this.copy(
        state = RecipeListState.ErrorState(
            RecipeListError.SearchError(cause)
        ),
        recipes = emptyList(),
        filter = RecipeListFilter(),
        isErrorShowing = true,
        isSearchButtonEnabled = true,
        isListShowing = false
    )

fun RecipeListModel.getStateAfterSearchSuccess() =
    this.copy(
        state = RecipeListState.SearchReadyState,
        isErrorShowing = false,
        isSearchButtonEnabled = true,
        isListShowing = true
    )

fun RecipeListModel.getStateSearchStarted() =
    this.copy(
        state = RecipeListState.SearchingState,
        recipes = emptyList(),
        isListShowing = false,
        isSearchButtonEnabled = false,
        isErrorShowing = false
    )

sealed interface RecipeListPageEvent {
    data class SearchButtonClicked(val recipeSearch: RecipeSearch): RecipeListPageEvent
    data class RecipeTitleChange(private val recipeTitle: String): RecipeListPageEvent
    data class IncludedProductsChange(private val includedProducts: String): RecipeListPageEvent
    data class ExcludedProductsChange(private val excludedProducts: String): RecipeListPageEvent
    data class CuisineChange(private val cuisine: Cuisines): RecipeListPageEvent
    data object IsListShowingChange : RecipeListPageEvent
    data object IsVeganChange : RecipeListPageEvent
    data object IsNeedVideoChange : RecipeListPageEvent
    data object IsNeedStepsChange : RecipeListPageEvent
    data object IsNeedAllProductsChange : RecipeListPageEvent
    data object IsSearchByDescriptionChange : RecipeListPageEvent
}

class RecipeListViewModel(
    private val getRecipesUseCase: GetRecipesUC
): ViewModel() {

    private val _uiState = MutableStateFlow(RecipeListModel())
    val uiState = _uiState.asStateFlow()

    fun reduce(event: RecipeListPageEvent) =
        when(event) {
            is RecipeListPageEvent.SearchButtonClicked -> onClickSearchButton(event)
            else -> {}
        }

    private fun onClickSearchButton(event: RecipeListPageEvent.SearchButtonClicked) {
        _uiState.update { currentState ->
            currentState.getStateSearchStarted()
        }

        viewModelScope.launch {
            searchRecipes(event.recipeSearch)
        }
    }

    private suspend fun searchRecipes(recipeSearch: RecipeSearch) {
        runCatching {
            getRecipesUseCase(recipeSearch)
                .collect { recipe ->
                    _uiState.update { state ->
                        state.copy(
                            recipes = state.recipes + recipe,
                            isListShowing = true
                        )
                    }
                }
        }.onSuccess {
            _uiState.update { currentState ->
                currentState.getStateAfterSearchSuccess()
            }
        }.onFailure { cause ->
            _uiState.update { currentState ->
                currentState.getStateAfterSearchError(cause)
            }
        }
    }

    companion object Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            RecipeListViewModel(
                getRecipesUseCase = GetRecipesUC(
                    repository = RecipeSearchRepositoryImpl(
                        client = RussianFoodComClient()
                    )
                )
            ) as T
    }
}
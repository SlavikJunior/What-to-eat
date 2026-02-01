package com.example.whattoeat.presentation.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.domain_entities.support.Product
import com.example.whattoeat.domain.domain_entities.support.RecipeSearch
import com.example.whattoeat.domain.use_cases.GetRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    val recipeTitle: String = "", // ye
    val cuisine: Cuisines = Cuisines.RUSSIAN, // ye
    val isVegan: Boolean = false, // ye
    val isSearchByDescription: Boolean = false, // ye
    val includedProducts: String = "", // ye
    val excludedProducts: String = "", // ye
    val isNeedVideo: Boolean = false, // ye
    val isNeedSteps: Boolean = false, // ye
    val isAllProductsAndNotContained: Boolean = false, // ye
)

data class RecipeListModel(
    val isFilterBottomSheetVisible: Boolean = false,
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
    data object SearchButtonClicked : RecipeListPageEvent
    data class RecipeTitleChange(val recipeTitle: String) : RecipeListPageEvent
    data class IncludedProductsChange(val includedProducts: String) : RecipeListPageEvent
    data class ExcludedProductsChange(val excludedProducts: String) : RecipeListPageEvent
    data class CuisineChange(private val cuisine: Cuisines) : RecipeListPageEvent
    data object IsListShowingChange : RecipeListPageEvent
    data object IsVeganChange : RecipeListPageEvent
    data object IsNeedVideoChange : RecipeListPageEvent
    data object IsNeedStepsChange : RecipeListPageEvent
    data object IsNeedAllProductsChange : RecipeListPageEvent
    data object IsSearchByDescriptionChange : RecipeListPageEvent
    data object IsFilterBottomSheetVisibleChange : RecipeListPageEvent
}

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeListModel())
    val uiState = _uiState.asStateFlow()

    fun reduce(event: RecipeListPageEvent) =
        when (event) {
            is RecipeListPageEvent.SearchButtonClicked -> onClickSearchButton()
            is RecipeListPageEvent.IsFilterBottomSheetVisibleChange -> onChangeFilterShitVisible()
            is RecipeListPageEvent.RecipeTitleChange -> onChangeRecipeTitle(event)
            is RecipeListPageEvent.IncludedProductsChange -> onChangeIncludedProducts(event)
            is RecipeListPageEvent.ExcludedProductsChange -> onChangeExcludedProducts(event)
            else -> {}
        }

    private fun onChangeExcludedProducts(event: RecipeListPageEvent.ExcludedProductsChange) {
        _uiState.update { currentState ->
            currentState.copy(
                filter = currentState.filter.copy(
                    excludedProducts = event.excludedProducts
                )
            )
        }
    }

    private fun onChangeIncludedProducts(event: RecipeListPageEvent.IncludedProductsChange) {
        _uiState.update { currentState ->
            currentState.copy(
                filter = currentState.filter.copy(
                    includedProducts = event.includedProducts
                )
            )
        }
    }

    private fun onChangeRecipeTitle(event: RecipeListPageEvent.RecipeTitleChange) {
        _uiState.update { currentState ->
            currentState.copy(
                filter = currentState.filter.copy(
                    recipeTitle = event.recipeTitle
                )
            )
        }
    }

    private fun onChangeFilterShitVisible() {
        _uiState.update { currentState ->
            currentState.copy(
                isFilterBottomSheetVisible = !currentState.isFilterBottomSheetVisible
            )
        }
    }

    private fun onClickSearchButton() {
        _uiState.update { currentState ->
            currentState.getStateSearchStarted()
        }

        viewModelScope.launch {
            searchRecipes()
        }
    }

    private fun combineRecipeSearchByDataFromUi() = with(_uiState.value) {
        RecipeSearch(
            title = filter.recipeTitle,
            includedProducts = if (filter.includedProducts.isNotEmpty())
                filter.includedProducts.split(",")
                    .map { Product(it.trim()) } else null,
            excludedProducts = if (filter.excludedProducts.isNotEmpty())
                filter.excludedProducts.split(",")
                    .map { Product(it.trim()) } else null,
            isAllProductsIncluded = filter.isAllProductsAndNotContained,
            isNeedVideo = filter.isNeedVideo,
            isNeedSteps = filter.isNeedSteps
        )
    }

    private suspend fun searchRecipes() {
        val recipeSearch = combineRecipeSearchByDataFromUi()
        try {
            getRecipesUseCase(recipeSearch)
                .collect { recipe ->
                    _uiState.update { state ->
                        state.copy(
                            recipes = state.recipes + recipe,
                            isListShowing = true
                        )
                    }
                }
            _uiState.update { currentState ->
                currentState.getStateAfterSearchSuccess()
            }
        } catch (cause: Throwable) {
            _uiState.update { currentState ->
                currentState.getStateAfterSearchError(cause)
            }
        }
    }
}
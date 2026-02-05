package com.example.whattoeat.presentation.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.domain_entities.common.Resource
import com.example.whattoeat.domain.domain_entities.support.Cuisines
import com.example.whattoeat.domain.domain_entities.support.Diets
import com.example.whattoeat.domain.domain_entities.support.DishTypes
import com.example.whattoeat.domain.domain_entities.support.SortDirection
import com.example.whattoeat.domain.domain_entities.support.SortTypes
import com.example.whattoeat.domain.search.RecipeSearch
import com.example.whattoeat.domain.use_cases.GetRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.jvm.Throws

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
    val query: String? = null,
    val cuisines: List<Cuisines>? = null,
    val diet: List<Diets>? = null,
    val includedProducts: String? = null,
    val excludedProducts: String? = null,
    val type: DishTypes? = null,
    val instructionsRequired: Boolean = false,
    val maxReadyTime: Int? = null,
    val minServings: Int? = null,
    val sort: SortTypes? = null,
    val sortDirection: SortDirection? = null,
    val offset: Int = 0,
    val number: Int = 5,
    val ranking: Int = 2, // Максимально использовать имеющиеся (1), минимизировать недостающие ингредиенты (2)
    val ignorePantry: Boolean = true // Игнорировать обычные продукты, такие как вода, соль...
)

data class RecipeListModel(
    val isFilterBottomSheetVisible: Boolean = false,
    val state: RecipeListState = RecipeListState.SearchReadyState,
    val recipes: List<Recipe> = listOf(),
    val isSearchByIngredientsEnabled: Boolean = false,
    val filter: RecipeListFilter = RecipeListFilter(),
    val isErrorShowing: Boolean = false,
    val isSearchButtonEnabled: Boolean = true,
    val isListShowing: Boolean = false
)

fun RecipeListModel.getStateAfterSearchError(cause: Throwable) =
    this.copy(
        state = RecipeListState.ErrorState(
            RecipeListError.SearchError(cause)
        ),
        recipes = emptyList(),
        isSearchByIngredientsEnabled = false,
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
    data class QueryChange(val query: String) : RecipeListPageEvent
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
            is RecipeListPageEvent.QueryChange -> onChangeQuery(event)
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

    private fun onChangeQuery(event: RecipeListPageEvent.QueryChange) {
        _uiState.update { currentState ->
            currentState.copy(
                filter = currentState.filter.copy(
                    query = event.query
                ),
                isSearchByIngredientsEnabled = false
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
        val recipeSearch: RecipeSearch
        try {
            recipeSearch = combineRecipeSearchByDataFromUi()
        } catch (e: RecipeListError.NotEnoughArgumentsError) {
            _uiState.update { currentState ->
                currentState.getStateAfterSearchError(cause = e)
            }
            return
        }

        viewModelScope.launch {
            searchRecipes(recipeSearch)
        }
    }

    private suspend fun searchRecipes(recipeSearch: RecipeSearch) {
        try {
            getRecipesUseCase(recipeSearch)
                .collect { resourceRecipe ->
                    when (resourceRecipe) {
                        is Resource.Loading<*> -> {
                            _uiState.update { currentState ->
                                currentState.getStateSearchStarted()
                            }
                        }

                        is Resource.Success<*> -> {
                            if (resourceRecipe.data != null)
                                _uiState.update { currentState ->
                                    currentState.copy(
                                        recipes = currentState.recipes + resourceRecipe.data,
                                        isListShowing = true
                                    )
                                }
                        }

                        is Resource.Error<*> -> {
                            if (resourceRecipe.message != null)
                            _uiState.update { currentState ->
                                currentState.getStateAfterSearchError(RecipeListError.SearchError(null))
                            }
                        }
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

    @Throws(RecipeListError.NotEnoughArgumentsError::class)
    private fun combineRecipeSearchByDataFromUi() = with(_uiState.value) {
        if (isSearchByIngredientsEnabled) {
            if (filter.includedProducts != null)
                RecipeSearch.RecipeByIngredientsSearch(
                    ingredients = filter.includedProducts,
                    number = filter.number,
                    ranking = filter.ranking,
                    ignorePantry = filter.ignorePantry,
                )
            else throw RecipeListError.NotEnoughArgumentsError(null)
        } else
            RecipeSearch.RecipeComplexSearch(
                query = filter.query,
                cuisines = filter.cuisines,
                diet = filter.diet,
                type = filter.type,
                instructionsRequired = filter.instructionsRequired,
                maxReadyTime = filter.maxReadyTime,
                minServings = filter.minServings,
                sort = filter.sort,
                sortDirection = filter.sortDirection,
                offset = filter.offset,
                number = filter.number,
            )
    }
}
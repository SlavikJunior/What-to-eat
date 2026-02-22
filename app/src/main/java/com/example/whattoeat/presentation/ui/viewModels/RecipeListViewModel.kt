package com.example.whattoeat.presentation.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whattoeat.domain.models.common.Recipe
import com.example.whattoeat.data.remoteSource.response.RecipeResponse
import com.example.whattoeat.domain.models.common.Resource
import com.example.whattoeat.domain.models.support.Cuisines
import com.example.whattoeat.domain.models.support.Diets
import com.example.whattoeat.domain.models.support.DishTypes
import com.example.whattoeat.domain.models.support.SortDirection
import com.example.whattoeat.domain.models.support.SortTypes
import com.example.whattoeat.data.remoteSource.request.RecipeRequest
import com.example.whattoeat.domain.useCases.AddFavoriteRecipeUseCase
import com.example.whattoeat.domain.useCases.GetRecipesUseCase
import com.example.whattoeat.domain.useCases.IsFavoriteRecipeUseCase
import com.example.whattoeat.domain.useCases.RemoveFavoriteRecipeUseCase
import com.example.whattoeat.domain.useCases.TranslateTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SearchType {
    COMPLEX_SEARCH,
    SEARCH_BY_INGREDIENTS
}

sealed class RecipeListError(override val cause: Throwable?) : Throwable(cause) {
    data class NotEnoughArgumentsError(override val cause: Throwable?) : RecipeListError(cause)
    data class ConflictFilterError(override val cause: Throwable?) : RecipeListError(cause)
    data class SearchError(override val cause: Throwable?) : RecipeListError(cause)
}

sealed interface RecipeListModelState {
    data object DefaultState : RecipeListModelState
    data object LoadingState : RecipeListModelState
    data class ErrorState(val error: RecipeListError) : RecipeListModelState
}

data class RecipeListFilter(
    val query: String? = null,
    val cuisines: List<Cuisines> = emptyList(),
    val diet: List<Diets> = emptyList(),
    val products: String? = null, // это уже для поиска по продуктам
    val type: DishTypes? = null,
    val instructionsRequired: Boolean = false,
    val maxReadyTime: Int? = null,
    val minServings: Int? = null,
    val sort: SortTypes? = null,
    val sortDirection: SortDirection? = null,
    val number: Int = 5,
    val ranking: Int = 2, // Максимально использовать имеющиеся (1), минимизировать недостающие ингредиенты (2)
    val ignorePantry: Boolean = true // Игнорировать обычные продукты, такие как вода, соль...
)

internal data class RecipeListModel(
    val isFilterBottomSheetVisible: Boolean = false,
    val modelState: RecipeListModelState = RecipeListModelState.DefaultState,
    val recipesComplex: List<Recipe.RecipeComplexExt> = listOf(),
    val recipesByIngredients: List<Recipe.RecipeByIngredientsExt> = listOf(),
    val searchType: SearchType = SearchType.COMPLEX_SEARCH,
    val filter: RecipeListFilter = RecipeListFilter(),
    val isSearchButtonEnabled: Boolean = true,
    val isListShowing: Boolean = false,
    val offset: Int = 0, // устанавливается кнопками навигации по списку
    val totalResults: Int = 0, // количество рецептов в базе
    val countOfRecipesOnPage: Int = 5, // отображаемое количество, часто = filter.number, но может быть меньше, если с бека пришло мало рецептов
)

internal fun RecipeListModel.isIncreaseOffsetButtonEnabled() = offset < totalResults

internal fun RecipeListModel.isDecreaseOffsetButtonEnabled() = offset >= filter.number

internal fun RecipeListModel.getStateAfterSearchError(cause: Throwable) =
    this.copy(
        modelState = RecipeListModelState.ErrorState(
            RecipeListError.SearchError(cause)
        ),
        recipesComplex = emptyList(),
        searchType = SearchType.COMPLEX_SEARCH,
        filter = RecipeListFilter(),
        isSearchButtonEnabled = true,
        isListShowing = false,
        totalResults = 0,
        offset = 0
    )

internal fun RecipeListModel.getStateAfterSearchSuccess() =
    this.copy(
        modelState = RecipeListModelState.DefaultState,
        isSearchButtonEnabled = true,
        isListShowing = true
    )

internal fun RecipeListModel.getStateAfterSearchStarted() =
    this.copy(
        modelState = RecipeListModelState.LoadingState,
        recipesComplex = emptyList(),
        isListShowing = false,
        isSearchButtonEnabled = false,
    )

sealed interface RecipeListPageEvent {
    data class QueryChange(val query: String) : RecipeListPageEvent
    data class ProductsChange(val products: String) : RecipeListPageEvent
    data object SearchTypeChange : RecipeListPageEvent
    data class CuisineChange(val cuisine: Cuisines) : RecipeListPageEvent
    data class DietChange(val diet: Diets) : RecipeListPageEvent
    data class DishTypeChange(val type: DishTypes) : RecipeListPageEvent
    data class SortTypeChange(val sortType: SortTypes) : RecipeListPageEvent
    data class SortDirectionChange(val sortDirection: SortDirection) : RecipeListPageEvent
    data class FavoriteRecipeChange(val recipe: Recipe) : RecipeListPageEvent
    data object IncreaseOffsetChange : RecipeListPageEvent
    data object DecreaseOffsetChange : RecipeListPageEvent
    data object IsListShowingChange : RecipeListPageEvent
    data object IsFilterBottomSheetVisibleChange : RecipeListPageEvent
    data object SearchButtonClicked : RecipeListPageEvent
}

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val getRecipes: GetRecipesUseCase,
    private val isFavoriteRecipe: IsFavoriteRecipeUseCase,
    private val addFavoriteRecipe: AddFavoriteRecipeUseCase,
    private val removeFavoriteRecipe: RemoveFavoriteRecipeUseCase,
    private val translateText: TranslateTextUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeListModel())
    internal val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun reduce(event: RecipeListPageEvent) =
        when (event) {
            is RecipeListPageEvent.SearchButtonClicked -> onClickSearchButton()
            is RecipeListPageEvent.IsFilterBottomSheetVisibleChange -> onChangeFilterShitVisible()
            is RecipeListPageEvent.QueryChange -> onChangeQuery(event)
            is RecipeListPageEvent.ProductsChange -> onProductsChange(event)
            is RecipeListPageEvent.IsListShowingChange -> {}
            is RecipeListPageEvent.SearchTypeChange -> onChangeSearchType()
            is RecipeListPageEvent.FavoriteRecipeChange -> onChangeFavoriteRecipe(event)
            is RecipeListPageEvent.IncreaseOffsetChange -> onChangeIncreaseOffset()
            is RecipeListPageEvent.DecreaseOffsetChange -> onChangeDecreaseOffset()
            is RecipeListPageEvent.CuisineChange -> onChangeCuisine(event)
            is RecipeListPageEvent.DishTypeChange -> onChangeDishType(event)
            is RecipeListPageEvent.DietChange -> onChangeDiet(event)
            is RecipeListPageEvent.SortTypeChange -> onChangeSortType(event)
            is RecipeListPageEvent.SortDirectionChange -> onChangeSortDirection(event)
        }

    private fun onProductsChange(event: RecipeListPageEvent.ProductsChange) {
        _uiState.update {
            it.copy(filter = it.filter.copy(products = event.products))
        }
    }

    private fun onChangeSortDirection(event: RecipeListPageEvent.SortDirectionChange) {
        val checked = _uiState.value.filter.sortDirection == event.sortDirection

        if (checked) {
            _uiState.update { currentState ->
                currentState.copy(
                    filter = currentState.filter.copy(
                        sortDirection = null
                    )
                )
            }
        } else if (_uiState.value.filter.sort != null) {
            _uiState.update { currentState ->
                currentState.copy(
                    filter = currentState.filter.copy(
                        sortDirection = event.sortDirection
                    )
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    modelState = RecipeListModelState.ErrorState(
                        error = RecipeListError.ConflictFilterError(
                            null
                        )
                    )
                )
            }
        }
    }

    private fun onChangeSortType(event: RecipeListPageEvent.SortTypeChange) {
        val checked = _uiState.value.filter.sort == event.sortType

        if (checked) {
            _uiState.update { currentState ->
                currentState.copy(
                    filter = currentState.filter.copy(
                        sort = null,
                        sortDirection = null
                    )
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    filter = currentState.filter.copy(
                        sort = event.sortType,
                        sortDirection = SortDirection.ASC
                    )
                )
            }
        }
    }

    private fun onChangeDiet(event: RecipeListPageEvent.DietChange) {
        val checked = _uiState.value.filter.diet.contains(event.diet)

        if (checked) {
            _uiState.update { currentState ->
                currentState.copy(
                    filter = currentState.filter.copy(
                        diet = currentState.filter.diet - event.diet
                    )
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    filter = currentState.filter.copy(
                        diet = currentState.filter.diet + event.diet
                    )
                )
            }
        }
    }

    private fun onChangeDishType(event: RecipeListPageEvent.DishTypeChange) {
        val checked = _uiState.value.filter.type == event.type

        if (checked) {
            _uiState.update { currentState ->
                currentState.copy(
                    filter = currentState.filter.copy(
                        type = null
                    )
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    filter = currentState.filter.copy(
                        type = event.type
                    )
                )
            }
        }
    }

    private fun onChangeCuisine(event: RecipeListPageEvent.CuisineChange) {
        val checked = _uiState.value.filter.cuisines.contains(event.cuisine)
        if (checked) {
            _uiState.update { currentState ->
                currentState.copy(
                    filter = currentState.filter.copy(
                        cuisines = currentState.filter.cuisines.filter { it != event.cuisine }
                    )
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    filter = currentState.filter.copy(
                        cuisines = listOf(*currentState.filter.cuisines.toTypedArray()) + event.cuisine
                    )
                )
            }
        }
    }

    private fun onChangeDecreaseOffset() {
        val newOffset = _uiState.value.offset - _uiState.value.filter.number

        _uiState.update { currentState ->
            currentState.copy(
                offset = newOffset,
                countOfRecipesOnPage = _uiState.value.filter.number
            )
        }

        viewModelScope.launch {
            onClickSearchButton() // авто-клик
        }
    }

    private fun onChangeIncreaseOffset() {
        val newOffset = _uiState.value.offset + _uiState.value.filter.number

        _uiState.update { currentSate ->
            currentSate.copy(
                offset = newOffset,
                countOfRecipesOnPage = _uiState.value.totalResults - newOffset
            )
        }


        viewModelScope.launch {
            onClickSearchButton() // авто-клик
        }
    }

    private fun onChangeFavoriteRecipe(event: RecipeListPageEvent.FavoriteRecipeChange) {
        val recipe = event.recipe

        viewModelScope.launch {
            val isFavorite = isFavoriteRecipe(recipe)

            if (isFavorite) removeFavoriteRecipe(recipe)
            else addFavoriteRecipe(recipe)


            if (uiState.value.searchType == SearchType.COMPLEX_SEARCH) {
                val updatedRecipes = _uiState.value.recipesComplex.map { tempRecipe ->
                    if (tempRecipe == recipe)
                        tempRecipe.copy(isFavorite = !tempRecipe.isFavorite)
                    else
                        tempRecipe
                }

                _uiState.update { currentState ->
                    currentState.copy(recipesComplex = updatedRecipes)
                }
            } else {
                val updatedRecipes = _uiState.value.recipesByIngredients.map { tempRecipe ->
                    if (tempRecipe == recipe)
                        tempRecipe.copy(isFavorite = !tempRecipe.isFavorite)
                    else
                        tempRecipe
                }

                _uiState.update { currentState ->
                    currentState.copy(recipesByIngredients = updatedRecipes)
                }
            }
        }
    }

    private fun onChangeSearchType() {
        _uiState.update { currentState ->
            currentState.copy(
                searchType = if (currentState.searchType == SearchType.COMPLEX_SEARCH) SearchType.SEARCH_BY_INGREDIENTS else SearchType.COMPLEX_SEARCH,
                isFilterBottomSheetVisible = false,
                filter = RecipeListFilter(),
                isListShowing = false,
                recipesComplex = emptyList(),
                recipesByIngredients = emptyList(),
            )
        }
    }

    private fun onChangeQuery(event: RecipeListPageEvent.QueryChange) {
        _uiState.update { currentState ->
            currentState.copy(
                filter = currentState.filter.copy(
                    query = event.query
                ),
                isListShowing = false,
                offset = 0,
                totalResults = 0
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
        searchJob?.cancel() // canceling previous search request

        searchJob = viewModelScope.launch {
            try {
                _uiState.update { currentState ->
                    currentState.getStateAfterSearchStarted()
                }

                val original = if(_uiState.value.searchType == SearchType.COMPLEX_SEARCH)
                    _uiState.value.filter.query.orEmpty()
                else _uiState.value.filter.products.orEmpty()

                val translated = translateText(input = listOf(original))

                if (translated.isNotEmpty()) {
                    _uiState.update { currentState ->
                        if(_uiState.value.searchType == SearchType.COMPLEX_SEARCH)
                            currentState.copy(filter = currentState.filter.copy(query = translated.first(),))
                        else
                            currentState.copy(filter = currentState.filter.copy(products = translated.first(),))
                    }
                    Log.d(TAG, "Search with translated query: ${translated.first()}")
                }

                val recipeSearch = combineRecipeSearchByDataFromUi()
                Log.d(TAG, "Search after translation: $recipeSearch")

                searchRecipes(recipeSearch)

            } catch (e: RecipeListError.NotEnoughArgumentsError) {
                _uiState.update { currentState ->
                    currentState.getStateAfterSearchError(cause = e)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Search error: ${e.message}", e)
                _uiState.update { currentState ->
                    currentState.getStateAfterSearchError(
                        RecipeListError.SearchError(e)
                    )
                }
            }
        }
    }

    private suspend fun searchRecipes(recipeSearch: RecipeRequest) {
        try {
            getRecipes(recipeSearch)
                .collectLatest { resourceRecipeResult ->
                    Log.d(TAG, "Collected ressource: $resourceRecipeResult from getRecipes()")

                    when (resourceRecipeResult) {
                        is Resource.Loading<*> ->
                            _uiState.update { currentState ->
                                currentState.getStateAfterSearchStarted()
                            }

                        is Resource.Success<*> ->
                            if (resourceRecipeResult.data != null) {
                                if (_uiState.value.searchType == SearchType.COMPLEX_SEARCH) {
                                    val recipeComplexResult =
                                        resourceRecipeResult.data as RecipeResponse.RecipeComplexResponse
                                    _uiState.update { currentState ->
                                        currentState.copy(
                                            recipesComplex = currentState.recipesComplex + recipeComplexResult.recipes.map {
                                                Recipe.RecipeComplexExt(
                                                    recipe = it,
                                                    isFavorite = isFavoriteRecipe(it)
                                                )
                                            },
                                            isListShowing = true,
                                            totalResults = recipeComplexResult.totalResults,
                                            offset = recipeComplexResult.offset
                                        )
                                    }
                                } else {
                                    val recipeByIngredientsResult =
                                        resourceRecipeResult.data as RecipeResponse.RecipeByIngredientsResponse
                                    _uiState.update { currentState ->
                                        currentState.copy(
                                            recipesByIngredients = currentState.recipesByIngredients + recipeByIngredientsResult.recipes.map {
                                                Recipe.RecipeByIngredientsExt(
                                                    recipe = it,
                                                    isFavorite = isFavoriteRecipe(it)
                                                )
                                            },
                                            isListShowing = true,
                                            totalResults = recipeByIngredientsResult.recipes.size,
                                            offset = 0
                                        )
                                    }
                                }
                            }

                        is Resource.Error<*> ->
                            _uiState.update { currentState ->
                                currentState.getStateAfterSearchError(
                                    RecipeListError.SearchError(
                                        null
                                    )
                                )
                            }
                    }
                }
            _uiState.update { currentState ->
                currentState.getStateAfterSearchSuccess()
            }

            searchJob = null
        } catch (cause: Throwable) {
            Log.e(TAG, "Cached throwable: $cause")
            _uiState.update { currentState ->
                currentState.getStateAfterSearchError(cause)
            }
        }
    }

    @Throws(RecipeListError.NotEnoughArgumentsError::class)
    private fun combineRecipeSearchByDataFromUi(): RecipeRequest {
        val recipeSearch = with(_uiState.value) {
            if (searchType == SearchType.SEARCH_BY_INGREDIENTS) {
                if (filter.products != null)
                    RecipeRequest.RecipeByIngredientsRequest(
                        ingredients = filter.products,
                        ranking = filter.ranking,
                        ignorePantry = filter.ignorePantry,
                        offset = 0,
                        number = 10,
                    )
                else throw RecipeListError.NotEnoughArgumentsError(null)
            } else
                RecipeRequest.RecipeComplexRequest(
                    query = filter.query,
                    cuisines = filter.cuisines,
                    diet = filter.diet,
                    type = filter.type,
                    instructionsRequired = filter.instructionsRequired,
                    maxReadyTime = filter.maxReadyTime,
                    minServings = filter.minServings,
                    sort = filter.sort,
                    sortDirection = filter.sortDirection,
                    offset = offset,
                    number = filter.number,
                )
        }

        Log.d(TAG, "Combined recipeSearch: $recipeSearch")
        return recipeSearch
    }

    companion object {
        private const val TAG = "TEST TAG"
    }
}
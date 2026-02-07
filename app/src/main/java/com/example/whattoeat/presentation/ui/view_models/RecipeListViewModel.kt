package com.example.whattoeat.presentation.ui.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.domain_entities.common.RecipeResult
import com.example.whattoeat.domain.domain_entities.common.Resource
import com.example.whattoeat.domain.domain_entities.support.Cuisines
import com.example.whattoeat.domain.domain_entities.support.Diets
import com.example.whattoeat.domain.domain_entities.support.DishTypes
import com.example.whattoeat.domain.domain_entities.support.SortDirection
import com.example.whattoeat.domain.domain_entities.support.SortTypes
import com.example.whattoeat.domain.search.RecipeSearch
import com.example.whattoeat.domain.use_cases.AddFavoriteRecipeUseCase
import com.example.whattoeat.domain.use_cases.GetRecipesUseCase
import com.example.whattoeat.domain.use_cases.IsFavoriteRecipeUseCase
import com.example.whattoeat.domain.use_cases.RemoveFavoriteRecipeUseCase
import com.example.whattoeat.domain.use_cases.TranslateTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.jvm.Throws

enum class SearchType {
    COMPLEX_SEARCH,
    SEARCH_BY_INGREDIENTS
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
    val number: Int = 5,
    val ranking: Int = 2, // Максимально использовать имеющиеся (1), минимизировать недостающие ингредиенты (2)
    val ignorePantry: Boolean = true // Игнорировать обычные продукты, такие как вода, соль...
)

data class RecipeListModel(
    val isFilterBottomSheetVisible: Boolean = false,
    val state: RecipeListState = RecipeListState.SearchReadyState,
    val recipes: List<Recipe.RecipeComplexExt> = listOf(),
    val searchType: SearchType = SearchType.COMPLEX_SEARCH,
    val filter: RecipeListFilter = RecipeListFilter(),
    val isErrorShowing: Boolean = false,
    val isSearchButtonEnabled: Boolean = true,
    val isListShowing: Boolean = false,
    val offset: Int = 0, // устанавливается кнопками навигации по списку
    val totalResults: Int = 0, // количество рецептов в базе
    val countOfRecipesOnPage: Int = 5, // отображаемое количество, часто = filter.number, но может быть меньше, если с бека пришло мало рецептов
)

fun RecipeListModel.numberOfCurrentPage() = (offset / filter.number) + 1

fun RecipeListModel.isIncreaseOffsetButtonEnabled() = offset < totalResults

fun RecipeListModel.isDecreaseOffsetButtonEnabled() = offset >= filter.number

fun RecipeListModel.getStateAfterSearchError(cause: Throwable) =
    this.copy(
        state = RecipeListState.ErrorState(
            RecipeListError.SearchError(cause)
        ),
        recipes = emptyList(),
        searchType = SearchType.COMPLEX_SEARCH,
        filter = RecipeListFilter(),
        isErrorShowing = true,
        isSearchButtonEnabled = true,
        isListShowing = false,
        totalResults = 0,
        offset = 0
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
    data class QueryChange(val query: String) : RecipeListPageEvent
    data class IncludedProductsChange(val includedProducts: String) : RecipeListPageEvent
    data class ExcludedProductsChange(val excludedProducts: String) : RecipeListPageEvent
    data class SearchTypeChange(val searchType: SearchType) : RecipeListPageEvent
    data class CuisineChange(val cuisine: Cuisines? = null) : RecipeListPageEvent
    data class DietChange(val diet: Diets? = null) : RecipeListPageEvent
    data class DishTypeChange(val type: DishTypes? = null) : RecipeListPageEvent
    data class MaxReadyTimeChange(val max: Int? = null) : RecipeListPageEvent
    data class MinServingsChange(val min: Int? = null) : RecipeListPageEvent
    data class SortTypeChange(val sortType: SortTypes?? = null) : RecipeListPageEvent
    data class SortDirectionChange(val sortDirection: SortDirection? = null) : RecipeListPageEvent
    data class OffsetChange(val offset: Int = 0) : RecipeListPageEvent
    data class NumberChange(val number: Int = 5) : RecipeListPageEvent
    data class RankingChange(val ranking: Int = 2) : RecipeListPageEvent
    data class OffsetOnPageChange(val offsetOnPage: Int) : RecipeListPageEvent
    data class FavoriteRecipeChange(val recipe: Recipe) : RecipeListPageEvent
    data object IncreaseOffsetChange : RecipeListPageEvent
    data object DecreaseOffsetChange : RecipeListPageEvent
    data object IgnorePantryChange : RecipeListPageEvent
    data object InstructionsRequiredChange : RecipeListPageEvent
    data object IsListShowingChange : RecipeListPageEvent
    data object IsFilterBottomSheetVisibleChange : RecipeListPageEvent
    data object SearchButtonClicked : RecipeListPageEvent
}

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val isFavoriteRecipeUseCase: IsFavoriteRecipeUseCase,
    private val addFavoriteRecipesUseCase: AddFavoriteRecipeUseCase,
    private val removeFavoriteRecipeUseCase: RemoveFavoriteRecipeUseCase,
    private val translateTextUseCase: TranslateTextUseCase
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
            is RecipeListPageEvent.SearchTypeChange -> onChangeSearchType(event)
            is RecipeListPageEvent.FavoriteRecipeChange -> onChangeFavoriteRecipe(event)
            is RecipeListPageEvent.IncreaseOffsetChange -> onChangeIncreaseOffset()
            is RecipeListPageEvent.DecreaseOffsetChange -> onChangeDecreaseOffset()
            else -> {}
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

        var isFavorite: Boolean
        viewModelScope.launch {
            isFavorite = async {
                isFavoriteRecipeUseCase(recipe)
            }.await()

            if (isFavorite) removeFavoriteRecipeUseCase(recipe)
            else addFavoriteRecipesUseCase(recipe)

            val updatedRecipes = _uiState.value.recipes.map { tempRecipe ->
                if (tempRecipe == recipe)
                    tempRecipe.copy(isFavorite = !tempRecipe.isFavorite)
                else
                    tempRecipe
            }

            withContext(Dispatchers.Main) {
                _uiState.update { currentState ->
                    currentState.copy(
                        recipes = updatedRecipes
                    )
                }
            }
        }
    }

    private fun onChangeSearchType(event: RecipeListPageEvent.SearchTypeChange) {
        _uiState.update { currentState ->
            currentState.copy(
                searchType = event.searchType,
                isFilterBottomSheetVisible = false,
                filter = RecipeListFilter(),
                isListShowing = false,
                isErrorShowing = false
            )
        }
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
        viewModelScope.launch {
            try {
                // 1. Обновляем состояние
                _uiState.update { currentState ->
                    currentState.getStateSearchStarted()
                }

                // 2. Получаем исходные данные
                val originalQuery = _uiState.value.filter.query ?: ""
                val originalIncluded = _uiState.value.filter.includedProducts ?: ""
                val originalExcluded = _uiState.value.filter.excludedProducts ?: ""

                // 3. Выполняем перевод (ОЖИДАЕМ завершения!)
                val translated = translateTextUseCase(
                    input = listOf(originalQuery, originalIncluded, originalExcluded)
                )

                // 4. Обновляем фильтр с переведенным текстом
                if (translated.size >= 3) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            filter = currentState.filter.copy(
                                query = translated[0],
                                includedProducts = translated[1],
                                excludedProducts = translated[2]
                            )
                        )
                    }
                    Log.d(TAG, "Search with translated query: ${translated[0]}")
                }

                // 5. Создаем RecipeSearch с ОБНОВЛЕННЫМИ данными
                val recipeSearch = combineRecipeSearchByDataFromUi()
                Log.d(TAG, "Search after translation: $recipeSearch")

                // 6. Выполняем поиск
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
    private suspend fun searchRecipes(recipeSearch: RecipeSearch) {
        try {
            getRecipesUseCase(recipeSearch)
                .collectLatest { resourceRecipeResult ->
                    Log.d(
                        TAG,
                        "Collected ressource: $resourceRecipeResult from getRecipesUseCase()"
                    )

                    when (resourceRecipeResult) {
                        is Resource.Loading<*> ->
                            _uiState.update { currentState ->
                                currentState.getStateSearchStarted()
                            }

                        is Resource.Success<*> ->
                            if (resourceRecipeResult.data != null) {
                                val recipeComplexResult =
                                    resourceRecipeResult.data as RecipeResult.RecipeComplexResult
                                _uiState.update { currentState ->
                                    currentState.copy(
                                        recipes = currentState.recipes + recipeComplexResult.recipeComplexList.map {
                                            Recipe.RecipeComplexExt(
                                                recipe = it,
                                                isFavorite = isFavoriteRecipeUseCase(it)
                                            )
                                        },
                                        isListShowing = true,
                                        totalResults = recipeComplexResult.totalResults,
                                        offset = recipeComplexResult.offset
                                    )
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
        } catch (cause: Throwable) {
            Log.e(TAG, "Cached throwable: $cause")
            _uiState.update { currentState ->
                currentState.getStateAfterSearchError(cause)
            }
        }
    }

    @Throws(RecipeListError.NotEnoughArgumentsError::class)
    private fun combineRecipeSearchByDataFromUi(): RecipeSearch {
        val recipeSearch = with(_uiState.value) {
            if (searchType == SearchType.SEARCH_BY_INGREDIENTS) {
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
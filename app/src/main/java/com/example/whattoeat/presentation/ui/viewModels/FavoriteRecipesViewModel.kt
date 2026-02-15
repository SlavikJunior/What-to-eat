package com.example.whattoeat.presentation.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whattoeat.di.DefaultDispatcher
import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.domain_entities.common.RecipeResult
import com.example.whattoeat.domain.domain_entities.common.Resource
import com.example.whattoeat.domain.search.RecipeSearch
import com.example.whattoeat.domain.useCases.AddFavoriteRecipeUseCase
import com.example.whattoeat.domain.useCases.GetFavoriteRecipesUseCase
import com.example.whattoeat.domain.useCases.GetRecipesUseCase
import com.example.whattoeat.domain.useCases.RemoveFavoriteRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class FavoriteRecipesPageStatus {
    DEFAULT,
    LOADING,
    SUCCESS,
    WARNING,
    ERROR
}

enum class FavoriteRecipesSortType {
    ORDER_BY_TITLE_ASC,
    ORDER_BY_TITLE_DESC,
//    ORDER_BY_READY_TIME_ASC,
//    ORDER_BY_READY_TIME_DESC
}

data class FavoriteRecipesFilter(
    val favoriteRecipesSortType: FavoriteRecipesSortType? = null
)

data class FavoriteRecipesModel(
    val status: FavoriteRecipesPageStatus = FavoriteRecipesPageStatus.DEFAULT,
    val isFilterBottomSheetVisible: Boolean = false,
    val recipes: List<Recipe.RecipeComplexExt> = listOf(),
    val isListShowing: Boolean = false,
    val filter: FavoriteRecipesFilter = FavoriteRecipesFilter(),
    val totalResults: Int = recipes.size
)

fun FavoriteRecipesModel.getStateLoadingStarted() =
    this.copy(
        status = FavoriteRecipesPageStatus.LOADING,
        isFilterBottomSheetVisible = false,
        recipes = emptyList(),
        isListShowing = false,
        filter = FavoriteRecipesFilter(),
        totalResults = 0
    )

fun FavoriteRecipesModel.getStateLoadingFinished(
    status: FavoriteRecipesPageStatus,
    recipes: List<Recipe.RecipeComplexExt> = emptyList(),
    totalResults: Int = recipes.size
) =
    this.copy(
        status = status,
        recipes = recipes,
        isListShowing = true,
        totalResults = totalResults
    )

sealed interface FavoriteRecipesPageEvent {
    data class SortTypeChange(val sortType: FavoriteRecipesSortType? = null) :
        FavoriteRecipesPageEvent

    data class FavoriteRecipeChange(val recipe: Recipe) : FavoriteRecipesPageEvent
    data object LoadRecipes : FavoriteRecipesPageEvent
}

@HiltViewModel
class FavoriteRecipesViewModel @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val getRecipes: GetRecipesUseCase,
    private val getFavoriteRecipes: GetFavoriteRecipesUseCase,
    private val addFavoriteRecipe: AddFavoriteRecipeUseCase,
    private val removeFavoriteRecipe: RemoveFavoriteRecipeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteRecipesModel())
    val uiState = _uiState.asStateFlow()

    private var favoritesJob: Job? = null

    fun reduce(event: FavoriteRecipesPageEvent) {
        when (event) {
            is FavoriteRecipesPageEvent.LoadRecipes -> subscribeToFavorites()
            is FavoriteRecipesPageEvent.SortTypeChange -> {}

            is FavoriteRecipesPageEvent.FavoriteRecipeChange -> {}
        }
    }

    private fun subscribeToFavorites() {
        favoritesJob?.cancel()
        favoritesJob = viewModelScope.launch(defaultDispatcher) {

            getFavoriteRecipes().collectLatest { favoriteRecipes ->

                _uiState.update {
                    it.getStateLoadingStarted()
                }

                if (favoriteRecipes.isEmpty()) {
                    _uiState.update {
                        it.getStateLoadingFinished(FavoriteRecipesPageStatus.SUCCESS, emptyList())
                    }
                } else {
                    try {
                        favoriteRecipes.forEach { favRecipe ->
                            launch {
                                fetchRecipeDetails(favRecipe.id)
                            }
                        }
                    } catch (cause: Throwable) {
                        Log.e(TAG, "Error loading fetching recipes from api with: $cause")

                        _uiState.update { currentState ->
                            currentState.getStateLoadingFinished(
                                status = FavoriteRecipesPageStatus.ERROR
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun fetchRecipeDetails(id: Int) {
        getRecipes(
            recipeSearch = RecipeSearch.RecipeFullInformationSearch(
                id = id,
                includeNutrition = true
            )
        ).collect { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // todo: обдумать можно ли как-то изменить состояние экрана при загрузке конкретного рецепта
                }

                is Resource.Success -> {
                    val result = resource.data
                    if (result is RecipeResult.RecipeFullInformationResult) {
                        val fullInfo = result.recipeFullInformationResult

                        val newRecipe = Recipe.RecipeComplexExt(
                            recipe = Recipe.RecipeComplex(
                                id = fullInfo.id,
                                title = fullInfo.title,
                                image = fullInfo.image,
                                imageType = fullInfo.imageType
                            ),
                            isFavorite = true
                        )

                        _uiState.update { currentState ->
                            val updatedList = currentState.recipes + newRecipe

                            currentState.copy(
                                status = FavoriteRecipesPageStatus.SUCCESS,
                                recipes = updatedList,
                                isListShowing = true,
                                totalResults = updatedList.size
                            )
                        }
                    }
                }

                is Resource.Error -> {
                    Log.e(TAG, "Error loading id: $id: ${resource.message}")
                    // todo: обдумать можно ли как-то изменить состояние экрана при загрузке с ошибкой конкретного рецепта
                }
            }
        }
    }

    companion object {
        private const val TAG = "TEST TAG"
    }
}
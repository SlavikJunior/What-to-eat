package com.example.whattoeat.presentation.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whattoeat.di.DefaultDispatcher
import com.example.whattoeat.domain.models.common.Recipe
import com.example.whattoeat.data.remoteSource.response.RecipeResponse
import com.example.whattoeat.domain.models.common.Resource
import com.example.whattoeat.data.remoteSource.request.RecipeRequest
import com.example.whattoeat.domain.useCases.GetFavoriteRecipesUseCase
import com.example.whattoeat.domain.useCases.GetRecipesUseCase
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
    LOADING,
    SUCCESS,
    ERROR
}

data class FavoriteRecipesModel(
    val status: FavoriteRecipesPageStatus = FavoriteRecipesPageStatus.SUCCESS,
    val isFilterBottomSheetVisible: Boolean = false,
    val recipes: List<Recipe.RecipeComplexExt> = listOf(),
    val isListShowing: Boolean = false,
    val totalResults: Int = recipes.size
)

fun FavoriteRecipesModel.getStateLoadingStarted() =
    this.copy(
        status = FavoriteRecipesPageStatus.LOADING,
        isFilterBottomSheetVisible = false,
        recipes = emptyList(),
        isListShowing = false,
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
    data class FavoriteRecipeChange(val recipe: Recipe) : FavoriteRecipesPageEvent
    data object LoadRecipes : FavoriteRecipesPageEvent
}

@HiltViewModel
class FavoriteRecipesViewModel @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val getRecipes: GetRecipesUseCase,
    private val getFavoriteRecipes: GetFavoriteRecipesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteRecipesModel())
    val uiState = _uiState.asStateFlow()

    private var favoritesJob: Job? = null

    fun reduce(event: FavoriteRecipesPageEvent) {
        when (event) {
            is FavoriteRecipesPageEvent.LoadRecipes -> subscribeToFavorites()
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
            recipeSearch = RecipeRequest.RecipeFullInformationRequest(
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
                    if (result is RecipeResponse.RecipeFullInformationResponse) {
                        val fullInfo = result.recipes

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
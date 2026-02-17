package com.example.whattoeat.presentation.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whattoeat.di.IoDispatcher
import com.example.whattoeat.domain.domainEntities.common.Recipe.*
import com.example.whattoeat.domain.domainEntities.common.RecipeResult
import com.example.whattoeat.domain.domainEntities.common.Resource
import com.example.whattoeat.domain.search.RecipeSearch
import com.example.whattoeat.domain.useCases.AddFavoriteRecipeUseCase
import com.example.whattoeat.domain.useCases.GetRecipesUseCase
import com.example.whattoeat.domain.useCases.IsFavoriteRecipeUseCase
import com.example.whattoeat.domain.useCases.RemoveFavoriteRecipeUseCase
import com.example.whattoeat.presentation.ui.viewModels.RecipeDetailError.*
import com.example.whattoeat.presentation.ui.viewModels.RecipeDetailModelState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RecipeDetailError(override val cause: Throwable?) : Throwable(cause) {
    data class LoadingError(override val cause: Throwable?) : RecipeDetailError(cause)
}

sealed interface RecipeDetailModelState {
    data object DefaultState : RecipeDetailModelState
    data object LoadingState : RecipeDetailModelState
    data class ErrorState(val error: RecipeDetailError) : RecipeDetailModelState
}

data class RecipeDetailModel(
    val modelState: RecipeDetailModelState = DefaultState,
    val recipe: RecipeFullInformationExt? = null,
    val similarRecipes: List<RecipeSimilarExt> = listOf(),
    val totalResults: Int = 0,
    val countOfSimilar: Int = 5
)

sealed interface RecipeDetailPageEvent {
    data class LoadRecipe(val recipeId: Int) : RecipeDetailPageEvent
    data class FavoriteSimilarRecipeChange(val recipe: RecipeSimilarExt) : RecipeDetailPageEvent
    data object FavoriteCurrentRecipeChange : RecipeDetailPageEvent
}

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val getRecipes: GetRecipesUseCase,
    private val isFavoriteRecipe: IsFavoriteRecipeUseCase,
    private val addFavoriteRecipe: AddFavoriteRecipeUseCase,
    private val removeFavoriteRecipe: RemoveFavoriteRecipeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetailModel())
    val uiState = _uiState.asStateFlow()

    fun reduce(event: RecipeDetailPageEvent) =
        when (event) {
            is RecipeDetailPageEvent.LoadRecipe -> onLoadRecipe(event)
            is RecipeDetailPageEvent.FavoriteCurrentRecipeChange -> onToggleCurrentFavorite()
            is RecipeDetailPageEvent.FavoriteSimilarRecipeChange -> onToggleSimilarFavorite(event)
        }

    private fun onLoadRecipe(event: RecipeDetailPageEvent.LoadRecipe) {
        Log.d(TAG, "Loading recipe with id: ${event.recipeId}")

        viewModelScope.launch {
            _uiState.update { it.copy(modelState = LoadingState) }

            getRecipes(RecipeSearch.RecipeFullInformationSearch(id = event.recipeId))
                .collectLatest { resource ->
                    Log.d(TAG, "Collected resource: $resource")

                    when (resource) {
                        is Resource.Success -> {
                            val result = resource.data as? RecipeResult.RecipeFullInformationResult
                            val recipe = result?.recipeFullInformationResult
                            if (recipe != null) {
                                val ext = RecipeFullInformationExt(
                                    recipe = recipe,
                                    isFavorite = isFavoriteRecipe(recipe)
                                )
                                _uiState.update {
                                    it.copy(
                                        recipe = ext,
                                        modelState = DefaultState
                                    )
                                }

                                Log.d(TAG, "Loading similar for recipe: ${ext.recipe.title}")
                                loadSimilarRecipes(event.recipeId)
                            }
                        }

                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    modelState = ErrorState(
                                        LoadingError(null)
                                    )
                                )
                            }
                        }

                        is Resource.Loading<*> -> {
                            _uiState.update {
                                it.copy(
                                    modelState = RecipeDetailModelState.LoadingState
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun loadSimilarRecipes(recipeId: Int) {
        Log.d(TAG, "Loading similar for recipe with id: $recipeId")

        viewModelScope.launch {
            getRecipes(RecipeSearch.RecipeSimilarSearch(id = recipeId, number = _uiState.value.countOfSimilar))
                .collectLatest { resource ->
                    Log.d(TAG, "Collected: $resource")

                    if (resource is Resource.Success) {
                        val similarResult = resource.data as? RecipeResult.RecipeSimilarResult
                        val similarList = similarResult?.recipeSimilarResult?.map {
                            RecipeSimilarExt(
                                recipe = it,
                                isFavorite = isFavoriteRecipe(it)
                            )
                        } ?: emptyList()

                        _uiState.update { it.copy(similarRecipes = similarList) }
                    }
                }
        }
    }

    private fun onToggleCurrentFavorite() {
        val current = _uiState.value.recipe ?: return
        viewModelScope.launch {
            if (current.isFavorite) removeFavoriteRecipe(current.recipe)
            else addFavoriteRecipe(current.recipe)

            _uiState.update {
                it.copy(recipe = it.recipe?.copy(isFavorite = !current.isFavorite))
            }
        }
    }

    private fun onToggleSimilarFavorite(event: RecipeDetailPageEvent.FavoriteSimilarRecipeChange) {
        val recipe = event.recipe
        viewModelScope.launch {
            if (recipe.isFavorite) removeFavoriteRecipe(recipe.recipe)
            else addFavoriteRecipe(recipe.recipe)

            _uiState.update { state ->
                state.copy(
                    similarRecipes = state.similarRecipes.map {
                        if (it.id == recipe.id) it.copy(isFavorite = !it.isFavorite) else it
                    }
                )
            }
        }
    }

    companion object {
        private const val TAG = "TEST TAG"
    }
}
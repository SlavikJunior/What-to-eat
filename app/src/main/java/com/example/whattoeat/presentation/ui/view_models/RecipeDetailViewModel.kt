package com.example.whattoeat.presentation.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whattoeat.domain.domain_entities.common.Recipe.*
import com.example.whattoeat.domain.domain_entities.common.RecipeResult
import com.example.whattoeat.domain.domain_entities.common.Resource
import com.example.whattoeat.domain.search.RecipeSearch
import com.example.whattoeat.domain.use_cases.AddFavoriteRecipeUseCase
import com.example.whattoeat.domain.use_cases.GetRecipesUseCase
import com.example.whattoeat.domain.use_cases.IsFavoriteRecipeUseCase
import com.example.whattoeat.domain.use_cases.RemoveFavoriteRecipeUseCase
import com.example.whattoeat.presentation.ui.view_models.RecipeDetailError.*
import com.example.whattoeat.presentation.ui.view_models.RecipeDetailModelState.*
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val offset: Int = 0,
    val totalResults: Int = 0,
    val countOfSimilar: Int = 5
)

fun RecipeDetailModel.numberOfCurrentPage() = (offset / countOfSimilar) + 1
fun RecipeDetailModel.isIncreaseOffsetButtonEnabled() = offset < totalResults

fun RecipeDetailModel.isDecreaseOffsetButtonEnabled() = offset >= countOfSimilar

sealed interface RecipeDetailPageEvent {
    data class LoadRecipe(val recipeId: Int) : RecipeDetailPageEvent
    data class FavoriteSimilarRecipeChange(val recipe: RecipeSimilarExt) :
        RecipeDetailPageEvent

    data object FavoriteCurrentRecipeChange : RecipeDetailPageEvent
    data object IncreaseOffsetChange : RecipeDetailPageEvent
    data object DecreaseOffsetChange : RecipeDetailPageEvent
}

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val isFavoriteRecipeUseCase: IsFavoriteRecipeUseCase,
    private val addFavoriteRecipeUseCase: AddFavoriteRecipeUseCase,
    private val removeFavoriteRecipeUseCase: RemoveFavoriteRecipeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetailModel())
    val uiState = _uiState.asStateFlow()

    fun reduce(event: RecipeDetailPageEvent) =
        when (event) {
            is RecipeDetailPageEvent.LoadRecipe -> onLoadRecipe(event)
            is RecipeDetailPageEvent.FavoriteCurrentRecipeChange -> onToggleCurrentFavorite()
            is RecipeDetailPageEvent.FavoriteSimilarRecipeChange -> onToggleSimilarFavorite(event)
            is RecipeDetailPageEvent.IncreaseOffsetChange -> onIncreaseSimilarOffset()
            is RecipeDetailPageEvent.DecreaseOffsetChange -> onDecreaseSimilarOffset()
        }

    private fun onLoadRecipe(event: RecipeDetailPageEvent.LoadRecipe) {
        viewModelScope.launch {
            _uiState.update { it.copy(modelState = LoadingState) }

            getRecipesUseCase(RecipeSearch.RecipeFullInformationSearch(id = event.recipeId))
                .collectLatest { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val result = resource.data as? RecipeResult.RecipeFullInformationResult
                            val recipe = result?.recipeFullInformationResult
                            if (recipe != null) {
                                val ext = RecipeFullInformationExt(
                                    recipe = recipe,
                                    isFavorite = isFavoriteRecipeUseCase(recipe)
                                )
                                _uiState.update {
                                    it.copy(
                                        recipe = ext,
                                        modelState = DefaultState
                                    )
                                }
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
        viewModelScope.launch {
            getRecipesUseCase(RecipeSearch.RecipeSimilarSearch(id = recipeId, number = _uiState.value.countOfSimilar))
                .collectLatest { resource ->
                    if (resource is Resource.Success) {
                        val similarResult = resource.data as? RecipeResult.RecipeSimilarResult
                        val similarList = similarResult?.recipeSimilarResult?.map {
                            RecipeSimilarExt(
                                recipe = it,
                                isFavorite = isFavoriteRecipeUseCase(it)
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
            if (current.isFavorite) removeFavoriteRecipeUseCase(current.recipe)
            else addFavoriteRecipeUseCase(current.recipe)

            _uiState.update {
                it.copy(recipe = it.recipe?.copy(isFavorite = !current.isFavorite))
            }
        }
    }

    private fun onToggleSimilarFavorite(event: RecipeDetailPageEvent.FavoriteSimilarRecipeChange) {
        val recipe = event.recipe
        viewModelScope.launch {
            if (recipe.isFavorite) removeFavoriteRecipeUseCase(recipe.recipe)
            else addFavoriteRecipeUseCase(recipe.recipe)

            _uiState.update { state ->
                state.copy(
                    similarRecipes = state.similarRecipes.map {
                        if (it.id == recipe.id) it.copy(isFavorite = !it.isFavorite) else it
                    }
                )
            }
        }
    }

    private fun onIncreaseSimilarOffset() { /* пока заглушка, если нужна пагинация похожих */
    }

    private fun onDecreaseSimilarOffset() { /* пока заглушка */
    }

    companion object {
        private const val TAG = "TEST TAG"
    }
}
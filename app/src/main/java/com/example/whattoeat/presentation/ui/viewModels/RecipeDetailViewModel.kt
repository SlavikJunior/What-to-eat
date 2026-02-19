package com.example.whattoeat.presentation.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whattoeat.data.yandex_translate.models.Languages
import com.example.whattoeat.di.IoDispatcher
import com.example.whattoeat.domain.domainEntities.common.Recipe.*
import com.example.whattoeat.domain.domainEntities.common.RecipeResult
import com.example.whattoeat.domain.domainEntities.common.Resource
import com.example.whattoeat.domain.search.RecipeSearch
import com.example.whattoeat.domain.useCases.AddFavoriteRecipeUseCase
import com.example.whattoeat.domain.useCases.GetRecipesUseCase
import com.example.whattoeat.domain.useCases.IsFavoriteRecipeUseCase
import com.example.whattoeat.domain.useCases.RemoveFavoriteRecipeUseCase
import com.example.whattoeat.domain.useCases.TranslateTextUseCase
import com.example.whattoeat.presentation.ui.viewModels.RecipeDetailError.*
import com.example.whattoeat.presentation.ui.viewModels.RecipeDetailModelState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RecipeDetailError(override val cause: Throwable?) : Throwable(cause) {
    data class LoadingError(override val cause: Throwable?) : RecipeDetailError(cause)
    data class TranslatingError(override val cause: Throwable?) : RecipeDetailError(cause)
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
    val countOfSimilar: Int = 3,
    val isTranslated: Boolean = false
)

sealed interface RecipeDetailPageEvent {
    data class LoadRecipe(val recipeId: Int) : RecipeDetailPageEvent
    data object TranslateRecipe : RecipeDetailPageEvent
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
    private val removeFavoriteRecipe: RemoveFavoriteRecipeUseCase,
    private val translateText: TranslateTextUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetailModel())
    val uiState = _uiState.asStateFlow()

    fun reduce(event: RecipeDetailPageEvent) =
        when (event) {
            is RecipeDetailPageEvent.LoadRecipe -> onLoadRecipe(event)
            is RecipeDetailPageEvent.FavoriteCurrentRecipeChange -> onToggleCurrentFavorite()
            is RecipeDetailPageEvent.FavoriteSimilarRecipeChange -> onToggleSimilarFavorite(event)
            is RecipeDetailPageEvent.TranslateRecipe -> onTranslateRecipe()
        }

    private fun onTranslateRecipe() {
        viewModelScope.launch {
            val currentRecipeExt = _uiState.value.recipe ?: return@launch
            val baseRecipe = currentRecipeExt.recipe

            val originalInstructions = baseRecipe.analyzedInstructions
            if (originalInstructions.isEmpty()) return@launch

            val allStepTexts = originalInstructions.flatMap { it.steps }.map { it.step }

            val allIngredientNames = originalInstructions
                .flatMap { it.steps }
                .flatMap { it.ingredients }
                .map { it.name }
                .distinct()

            try {
                val translatedSteps = async {
                    translateText(allStepTexts, Languages.RUSSIAN)
                }
                val translatedIngredients = async {
                    translateText(allIngredientNames, Languages.RUSSIAN)
                }

                val (translatedStepTexts, translatedIngredientsTexts) = awaitAll(
                    translatedSteps, translatedIngredients
                )

                val ingredientMap = allIngredientNames.zip(translatedIngredientsTexts).toMap()

                var stepGlobalIndex = 0
                val newInstructions = originalInstructions.map { instruction ->
                    instruction.copy(
                        steps = instruction.steps.map { step ->
                            val translatedStep = step.copy(
                                step = translatedStepTexts.getOrElse(stepGlobalIndex) { step.step },
                                ingredients = step.ingredients.map { ingr ->
                                    ingr.copy(name = ingredientMap[ingr.name] ?: ingr.name)
                                }
                            )
                            stepGlobalIndex++
                            translatedStep
                        }
                    )
                }

                _uiState.update { currentState ->
                    currentState.copy(
                        recipe = currentState.recipe!!.copy(
                            recipe = RecipeFullInformation(
                                id = currentState.recipe.recipe.id,
                                image = currentState.recipe.recipe.image,
                                imageType = currentState.recipe.recipe.imageType,
                                title = currentState.recipe.recipe.title,
                                readyInMinutes = currentState.recipe.recipe.readyInMinutes,
                                servings = currentState.recipe.recipe.servings,
                                sourceUrl = currentState.recipe.recipe.sourceUrl,
                                vegetarian = currentState.recipe.recipe.vegetarian,
                                vegan = currentState.recipe.recipe.vegan,
                                glutenFree = currentState.recipe.recipe.glutenFree,
                                dairyFree = currentState.recipe.recipe.dairyFree,
                                veryHealthy = currentState.recipe.recipe.veryHealthy,
                                cheap = currentState.recipe.recipe.cheap,
                                veryPopular = currentState.recipe.recipe.veryPopular,
                                cookingMinutes = currentState.recipe.recipe.cookingMinutes,
                                aggregateLikes = currentState.recipe.recipe.aggregateLikes,
                                healthScore = currentState.recipe.recipe.healthScore,
                                extendedIngredients = currentState.recipe.recipe.extendedIngredients,
                                summary = currentState.recipe.recipe.summary,
                                cuisines = currentState.recipe.recipe.cuisines,
                                dishTypes = currentState.recipe.recipe.dishTypes,
                                diets = currentState.recipe.recipe.diets,
                                occasions = currentState.recipe.recipe.occasions,
                                instructions = currentState.recipe.recipe.instructions,
                                analyzedInstructions = newInstructions,
                                spoonacularScore = currentState.recipe.recipe.spoonacularScore,
                                spoonacularSourceUrl = currentState.recipe.recipe.spoonacularSourceUrl
                            )
                        ),
                        isTranslated = true
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Translation failed", e)

                _uiState.update {
                    it.copy(
                        modelState = ErrorState(TranslatingError(e))
                    )
                }
            }
        }
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
                                        recipe = ext
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
                                    modelState = LoadingState
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun loadSimilarRecipes(recipeId: Int) {
        viewModelScope.launch(ioDispatcher) {

            getRecipes(
                RecipeSearch.RecipeSimilarSearch(
                    id = recipeId,
                    number = _uiState.value.countOfSimilar
                )
            ).collectLatest { resource ->

                if (resource !is Resource.Success) return@collectLatest

                val similarResult =
                    resource.data as? RecipeResult.RecipeSimilarResult
                        ?: return@collectLatest

                val initialList = similarResult.recipeSimilarResult.map {
                    RecipeSimilarExt(
                        recipe = it,
                        isFavorite = isFavoriteRecipe(it)
                    )
                }

                _uiState.update {
                    it.copy(similarRecipes = initialList)
                }

                initialList.forEach { similar ->
                    launch {
                        getRecipes(
                            RecipeSearch.RecipeFullInformationSearch(similar.id)
                        ).collectLatest { fullResource ->

                            if (fullResource !is Resource.Success) return@collectLatest

                            val fullInfo =
                                fullResource.data as? RecipeResult.RecipeFullInformationResult
                                    ?: return@collectLatest

                            val imageUrl =
                                fullInfo.recipeFullInformationResult.image

                            _uiState.update { state ->
                                state.copy(
                                    similarRecipes = state.similarRecipes.map { similarRecipeTemp ->
                                        if (similarRecipeTemp.id == similar.id) {
                                            similarRecipeTemp.copy(
                                                recipe = RecipeSimilar(
                                                    id = similarRecipeTemp.id,
                                                    image = imageUrl,
                                                    imageType = similarRecipeTemp.imageType,
                                                    title = similarRecipeTemp.title,
                                                    readyInMinutes = similarRecipeTemp.readyInMinutes,
                                                    servings = similarRecipeTemp.servings,
                                                    sourceUrl = similarRecipeTemp.sourceUrl
                                                )
                                            )
                                        } else similarRecipeTemp
                                    }
                                )
                            }
                        }
                    }
                }
                _uiState.update {
                    it.copy(
                        modelState = DefaultState
                    )
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
package com.example.whattoeat.presentation.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whattoeat.di.IoDispatcher
import com.example.whattoeat.domain.domainEntities.common.Recipe
import com.example.whattoeat.domain.useCases.DeleteUsersRecipeUseCase
import com.example.whattoeat.domain.useCases.GetAllUsersRecipesUseCase
import com.example.whattoeat.domain.useCases.UploadUsersRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface UsersRecipesModelState {
    data object DefaultState : UsersRecipesModelState
    data object LoadingState : UsersRecipesModelState
    data class ErrorState(val cause: Throwable?) : UsersRecipesModelState
}

data class UsersRecipesModel(
    val modelState: UsersRecipesModelState = UsersRecipesModelState.DefaultState,
    val recipes: List<Recipe.RecipeByUser> = emptyList(),
    val isAddSheetVisible: Boolean = false,
    val recipe: Recipe.RecipeByUser = Recipe.RecipeByUser
        (
        image = null,
        imageType = null,
        title = "",
        readyInMinutes = -1,
        servings = -1,
        sourceUrl = null,
        vegetarian = null,
        vegan = null,
        glutenFree = null,
        dairyFree = null,
        veryHealthy = null,
        cheap = null,
        cookingMinutes = null,
        healthScore = null,
        ingredients = null,
        summary = null,
        cuisines = null,
        dishTypes = null,
        diets = null,
        occasions = null,
        instructions = null,
        steps = null,
        notes = null,
    )
)

sealed interface UsersRecipesPageEvent {
    data object LoadRecipes : UsersRecipesPageEvent
    data object IsAddSheetVisibleChange : UsersRecipesPageEvent
    data object SaveRecipe : UsersRecipesPageEvent
    data class DeleteRecipe(val recipe: Recipe.RecipeByUser) : UsersRecipesPageEvent
    data class OnTitleChange(val title: String) : UsersRecipesPageEvent
    data class OnTimeChange(val readyInMinutes: Int) : UsersRecipesPageEvent
    data class OnInstructionsChange(val instructions: String) : UsersRecipesPageEvent
}

@HiltViewModel
class UsersRecipesViewModel @Inject constructor(
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val getAllUsersRecipes: GetAllUsersRecipesUseCase,
    private val uploadUsersRecipe: UploadUsersRecipeUseCase,
    private val deleteUsersRecipe: DeleteUsersRecipeUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsersRecipesModel())
    val uiState = _uiState.asStateFlow()

    init {
        reduce(UsersRecipesPageEvent.LoadRecipes)
    }

    fun reduce(event: UsersRecipesPageEvent) =
        when (event) {
            is UsersRecipesPageEvent.LoadRecipes -> loadRecipes()
            is UsersRecipesPageEvent.IsAddSheetVisibleChange -> _uiState.update {
                it.copy(
                    isAddSheetVisible = !it.isAddSheetVisible
                )
            }

            is UsersRecipesPageEvent.OnTitleChange -> _uiState.update {
                it.copy(
                    recipe = it.recipe.copy(
                        title = event.title
                    )
                )
            }

            is UsersRecipesPageEvent.OnTimeChange -> _uiState.update {
                it.copy(
                    recipe = it.recipe.copy(
                        readyInMinutes = event.readyInMinutes
                    )
                )
            }

            is UsersRecipesPageEvent.SaveRecipe -> saveNewRecipe()
            is UsersRecipesPageEvent.DeleteRecipe -> deleteRecipe(event.recipe)
            else -> {}
        }

    private fun loadRecipes() {
        viewModelScope.launch(ioDispatcher) {
            _uiState.update { it.copy(modelState = UsersRecipesModelState.LoadingState) }

            try {
                getAllUsersRecipes().collect { recipes ->
                    _uiState.update {
                        it.copy(
                            recipes = recipes,
                            modelState = UsersRecipesModelState.DefaultState
                        )
                    }
                }
            } catch (cause: Throwable) {
                Log.e(TAG, "Error: $cause")
                _uiState.update { it.copy(modelState = UsersRecipesModelState.ErrorState(cause = cause)) }
            }
        }
    }

    private fun saveNewRecipe() {
        val currentState = _uiState.value
        if (currentState.recipe.title.isBlank()) return

        viewModelScope.launch(ioDispatcher) {
            val newRecipe = Recipe.RecipeByUser(
                title = currentState.recipe.title,
                readyInMinutes = currentState.recipe.readyInMinutes,
                instructions = currentState.recipe.instructions,
                image = null,
                imageType = null,
                servings = 1,
                sourceUrl = null,
                vegetarian = false,
                vegan = false,
                glutenFree = false,
                dairyFree = false,
                veryHealthy = false,
                cheap = false,
                cookingMinutes = null,
                healthScore = null,
                ingredients = emptyList(),
                summary = currentState.recipe.summary,
                cuisines = emptyList(),
                dishTypes = emptyList(),
                diets = emptyList(),
                occasions = emptyList(),
                steps = emptyList(),
                notes = null
            )

            uploadUsersRecipe(newRecipe)

            reduce(UsersRecipesPageEvent.IsAddSheetVisibleChange)
            reduce(UsersRecipesPageEvent.LoadRecipes)
        }
    }

    private fun deleteRecipe(recipe: Recipe.RecipeByUser) {
        viewModelScope.launch {
            val deleted = deleteUsersRecipe(recipe)
            if (deleted > 0)
                reduce(UsersRecipesPageEvent.LoadRecipes)
        }
    }

    companion object {
        private const val TAG = "TEST TAG"
    }
}
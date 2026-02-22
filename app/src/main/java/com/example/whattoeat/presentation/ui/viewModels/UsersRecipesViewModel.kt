package com.example.whattoeat.presentation.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whattoeat.di.IoDispatcher
import com.example.whattoeat.domain.models.common.Recipe
import com.example.whattoeat.domain.useCases.DeleteUsersRecipeUseCase
import com.example.whattoeat.domain.useCases.GetAllUsersRecipesUseCase
import com.example.whattoeat.domain.useCases.UpdateUsersRecipeUseCase
import com.example.whattoeat.domain.useCases.UploadUsersRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class UsersRecipesError(override val cause: Throwable?) : Throwable(cause) {
    data class SaveError(override val cause: Throwable?) : UsersRecipesError(cause)
    data class CauseError(override val cause: Throwable?) : UsersRecipesError(cause)
}

sealed interface UsersRecipesModelState {
    data object DefaultState : UsersRecipesModelState
    data object LoadingState : UsersRecipesModelState
    data class ErrorState(val cause: UsersRecipesError?) : UsersRecipesModelState
}

data class RecipeByUserOnUi(
    val title: String = "",
    val readyInMinutes: String = "",
    val servings: String = "",
    val ingredients: String = "",
    val notes: String = ""
)

enum class ButtonActionType(val text: String) {
    SAVE_RECIPE("Save Recipe"),
    UPDATE_RECIPE("Update Recipe")
}

data class UsersRecipesModel(
    val modelState: UsersRecipesModelState = UsersRecipesModelState.DefaultState,
    val recipes: List<Recipe.RecipeByUser> = emptyList(),
    val isSheetVisible: Boolean = false,
    val buttonActionType: ButtonActionType = ButtonActionType.SAVE_RECIPE,
    val recipeByUserOnUi: RecipeByUserOnUi = RecipeByUserOnUi() // модель рецепта из интерфейса
)

sealed interface UsersRecipesPageEvent {
    data object LoadRecipes : UsersRecipesPageEvent
    data object IsAddSheetVisibleChange : UsersRecipesPageEvent
    data object SaveRecipe : UsersRecipesPageEvent
    data class DeleteRecipe(val recipe: Recipe.RecipeByUser) : UsersRecipesPageEvent
    data class UpdateRecipeStart(val recipe: Recipe.RecipeByUser) : UsersRecipesPageEvent
    data object UpdateRecipeEnd : UsersRecipesPageEvent
    data class TitleChange(val title: String) : UsersRecipesPageEvent
    data class ReadyInMinutesChange(val readyInMinutes: String) : UsersRecipesPageEvent
    data class ServingsChange(val servings: String) : UsersRecipesPageEvent
    data class IngredientsChange(val ingredients: String) : UsersRecipesPageEvent
    data class NotesChange(val notes: String) : UsersRecipesPageEvent
}

@HiltViewModel
class UsersRecipesViewModel @Inject constructor(
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val getAllUsersRecipes: GetAllUsersRecipesUseCase,
    private val uploadUsersRecipe: UploadUsersRecipeUseCase,
    private val deleteUsersRecipe: DeleteUsersRecipeUseCase,
    private val updateUsersRecipe: UpdateUsersRecipeUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsersRecipesModel())
    val uiState = _uiState.asStateFlow()

    private var recipe: Recipe.RecipeByUser? = null // внутренняя модель рецепта
    private var currentRecipeId: Int = 0

    init {
        reduce(UsersRecipesPageEvent.LoadRecipes)
    }

    fun reduce(event: UsersRecipesPageEvent) =
        when (event) {
            is UsersRecipesPageEvent.LoadRecipes -> onLoadRecipes()
            is UsersRecipesPageEvent.IsAddSheetVisibleChange -> {
                if (uiState.value.isSheetVisible) {
                    _uiState.update {
                        it.copy(
                            isSheetVisible = false,
                            buttonActionType = ButtonActionType.SAVE_RECIPE,
                            recipeByUserOnUi = RecipeByUserOnUi()
                        )
                    }
                    currentRecipeId = 0
                } else _uiState.update { it.copy(isSheetVisible = true) }
            }

            is UsersRecipesPageEvent.SaveRecipe -> onSaveRecipe()

            is UsersRecipesPageEvent.DeleteRecipe -> onDeleteRecipe(event)

            is UsersRecipesPageEvent.UpdateRecipeStart -> onUpdateRecipeStart(event)
            is UsersRecipesPageEvent.UpdateRecipeEnd -> onUpdateRecipeEnd()

            is UsersRecipesPageEvent.TitleChange -> _uiState.update {
                it.copy(
                    recipeByUserOnUi = it.recipeByUserOnUi.copy(
                        title = event.title
                    )
                )
            }

            is UsersRecipesPageEvent.ReadyInMinutesChange -> _uiState.update {
                it.copy(
                    recipeByUserOnUi = it.recipeByUserOnUi.copy(
                        readyInMinutes = event.readyInMinutes
                    )
                )
            }

            is UsersRecipesPageEvent.ServingsChange -> _uiState.update {
                it.copy(
                    recipeByUserOnUi = it.recipeByUserOnUi.copy(
                        servings = event.servings
                    )
                )
            }

            is UsersRecipesPageEvent.IngredientsChange -> _uiState.update {
                it.copy(
                    recipeByUserOnUi = it.recipeByUserOnUi.copy(
                        ingredients = event.ingredients
                    )
                )
            }

            is UsersRecipesPageEvent.NotesChange -> _uiState.update {
                it.copy(
                    recipeByUserOnUi = it.recipeByUserOnUi.copy(
                        notes = event.notes
                    )
                )
            }
        }

    private fun onLoadRecipes() {
        viewModelScope.launch {
            _uiState.update { it.copy(modelState = UsersRecipesModelState.LoadingState) }

            try {
                getAllUsersRecipes().collect { recipes ->
                    _uiState.update {
                        it.copy(
                            recipes = recipes,
                            modelState = UsersRecipesModelState.DefaultState
                        )
                    }

                    Log.d(TAG, "Collected: $recipes")
                }
            } catch (cause: Throwable) {
                Log.e(TAG, "Error: $cause")
                _uiState.update {
                    it.copy(
                        modelState = UsersRecipesModelState.ErrorState(
                            UsersRecipesError.CauseError(cause)
                        )
                    )
                }
            }
        }
    }

    private fun onSaveRecipe() {
        try {
            combineRecipeByUserFromRecipeBuUserOnUi()
        } catch (e: UsersRecipesError.SaveError) {
            _uiState.update { currentState ->
                currentState.copy(
                    modelState = UsersRecipesModelState.ErrorState(cause = e),
                    isSheetVisible = false,
                    recipeByUserOnUi = RecipeByUserOnUi()
                )
            }
            return
        }

        viewModelScope.launch {

            recipe?.let { recipeByUser ->
                try {
                    uploadUsersRecipe(recipeByUser)
                } catch (cause: Throwable) {
                    Log.e(TAG, "Error while uploading!")

                    _uiState.update {
                        it.copy(
                            modelState = UsersRecipesModelState.ErrorState(
                                UsersRecipesError.CauseError(
                                    cause
                                )
                            )
                        )
                    }
                    return@let
                }

                reduce(UsersRecipesPageEvent.IsAddSheetVisibleChange)
                reduce(UsersRecipesPageEvent.LoadRecipes)
            }
        }
    }

    @Throws(UsersRecipesError.SaveError::class)
    private fun combineRecipeByUserFromRecipeBuUserOnUi() {
        if (_uiState.value.recipeByUserOnUi.title.isNotBlank()) {
            recipe = with(_uiState.value.recipeByUserOnUi) {
                try {
                    val targetId = if (_uiState.value.buttonActionType == ButtonActionType.UPDATE_RECIPE) currentRecipeId else 0

                    val recipe = Recipe.RecipeByUser(
                        id = targetId,
                        title = title,
                        readyInMinutes = if(readyInMinutes.isBlank()) 0 else readyInMinutes.trim().toInt(),
                        servings = if(servings.isBlank()) 0 else servings.trim().toInt(),
                        ingredients = ingredients,
                        notes = notes
                    )

                    Log.d(TAG, "Combined: $recipe")
                    recipe
                } catch (e: NumberFormatException) {
                    Log.e(TAG, "NumberFormatException while combining")
                    throw UsersRecipesError.SaveError(cause = e)
                }
            }
        }
    }

    private fun onDeleteRecipe(event: UsersRecipesPageEvent.DeleteRecipe) {
        viewModelScope.launch {
            val deleted = deleteUsersRecipe(event.recipe)
            if (deleted > 0)
                reduce(UsersRecipesPageEvent.LoadRecipes)
        }
    }

    private fun onUpdateRecipeStart(event: UsersRecipesPageEvent.UpdateRecipeStart) {
        currentRecipeId = event.recipe.id

        _uiState.update { currentState ->
            currentState.copy(
                buttonActionType = ButtonActionType.UPDATE_RECIPE,
                isSheetVisible = true,
                recipeByUserOnUi = currentState.recipeByUserOnUi.copy(
                    title = event.recipe.title,
                    readyInMinutes = event.recipe.readyInMinutes?.toString().orEmpty(),
                    servings = event.recipe.servings?.toString().orEmpty(),
                    ingredients = event.recipe.ingredients.orEmpty(),
                    notes = event.recipe.notes.orEmpty()
                )
            )
        }
    }

    private fun onUpdateRecipeEnd() {
        try {
            combineRecipeByUserFromRecipeBuUserOnUi()
        } catch (e: UsersRecipesError.SaveError) {
            _uiState.update { currentState ->
                currentState.copy(
                    modelState = UsersRecipesModelState.ErrorState(cause = e),
                    isSheetVisible = false,
                    recipeByUserOnUi = RecipeByUserOnUi()
                )
            }
            return
        }

        viewModelScope.launch {

            recipe?.let { recipeByUser ->
                var updated: Int
                try {
                     updated = updateUsersRecipe(recipeByUser)
                } catch (cause: Throwable) {
                    Log.e(TAG, "Error while updating!")

                    _uiState.update {
                        it.copy(
                            modelState = UsersRecipesModelState.ErrorState(
                                UsersRecipesError.CauseError(
                                    cause
                                )
                            )
                        )
                    }
                    return@let
                }

                if (updated > 0) {
                    reduce(UsersRecipesPageEvent.IsAddSheetVisibleChange)
                    reduce(UsersRecipesPageEvent.LoadRecipes)
                } else {
                    _uiState.update {
                        it.copy(
                            modelState = UsersRecipesModelState.ErrorState(null)
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "TEST TAG"
    }
}
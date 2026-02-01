package com.example.whattoeat.domain.use_cases

import android.util.Log
import com.example.whattoeat.domain.domain_entities.support.RecipeSearch
import com.example.whattoeat.domain.repositories.RecipeSearchRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class GetRecipesUC(
    private val repository: RecipeSearchRepository
) {
    @Throws(Exception::class)
    operator fun invoke(recipeSearch: RecipeSearch) = flow {
        Log.d(TAG, "Invoking with: $recipeSearch")
        try {
            repository.getRecipesByRecipeSearch(recipeSearch)
                .onEach { recipe ->
                    Log.d(TAG, "Received recipe from repository: ${recipe.title}")
                }
                .collect { recipe ->
                    Log.d(TAG, "Emitting recipe: ${recipe.title}")
                    emit(recipe)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "GetRecipesUC"
    }
}
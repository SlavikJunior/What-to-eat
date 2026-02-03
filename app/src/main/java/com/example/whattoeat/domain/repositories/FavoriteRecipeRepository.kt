package com.example.whattoeat.domain.repositories

import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.search.RecipeSearch
import kotlinx.coroutines.flow.Flow

interface FavoriteRecipeRepository {

    suspend fun addRecipe(recipe: Recipe): Long

    suspend fun removeRecipe(recipe: Recipe): Int

    suspend fun getRecipes(recipeSearch: RecipeSearch): Flow<Recipe>
}
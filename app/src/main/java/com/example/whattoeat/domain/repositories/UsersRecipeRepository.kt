package com.example.whattoeat.domain.repositories

import com.example.whattoeat.domain.domainEntities.common.Recipe
import com.example.whattoeat.domain.search.RecipeSearch
import kotlinx.coroutines.flow.Flow

interface UsersRecipeRepository {

    suspend fun uploadRecipe(recipe: Recipe.RecipeByUser): Long

    suspend fun deleteRecipe(recipe: Recipe.RecipeByUser): Int
    suspend fun getAllRecipes(): Flow<List<Recipe.RecipeByUser>>
}
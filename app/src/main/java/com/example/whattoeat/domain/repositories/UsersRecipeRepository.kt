package com.example.whattoeat.domain.repositories

import com.example.whattoeat.domain.models.common.Recipe
import kotlinx.coroutines.flow.Flow

interface UsersRecipeRepository {

    suspend fun updateRecipe(recipe: Recipe.RecipeByUser): Int

    suspend fun uploadRecipe(recipe: Recipe.RecipeByUser): Long

    suspend fun deleteRecipe(recipe: Recipe.RecipeByUser): Int
    suspend fun getAllRecipes(): Flow<List<Recipe.RecipeByUser>>
    suspend fun getRecipeById(id: Int): Recipe.RecipeByUser?
}
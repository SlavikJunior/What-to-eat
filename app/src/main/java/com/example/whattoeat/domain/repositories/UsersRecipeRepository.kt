package com.example.whattoeat.domain.repositories

import com.example.whattoeat.domain.domain_entities.common.RecipeByUser
import com.example.whattoeat.domain.search.RecipeSearch
import kotlinx.coroutines.flow.Flow

interface UsersRecipeRepository {

    suspend fun uploadRecipe(recipe: RecipeByUser): Long

    suspend fun deleteRecipe(recipe: RecipeByUser): Int

    suspend fun getRecipes(recipeSearch: RecipeSearch): Flow<RecipeByUser>
}
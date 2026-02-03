package com.example.whattoeat.domain.repositories

import com.example.whattoeat.domain.domain_entities.common.RecipeByUser

interface UsersRecipeRepository {

    suspend fun uploadRecipe(recipe: RecipeByUser): Long

    suspend fun deleteRecipe(recipe: RecipeByUser): Int
}
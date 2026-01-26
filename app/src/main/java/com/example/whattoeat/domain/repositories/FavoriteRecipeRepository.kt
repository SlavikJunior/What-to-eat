package com.example.whattoeat.domain.repositories

import com.example.whattoeat.domain.domain_entities.common.Recipe

interface FavoriteRecipeRepository {

    suspend fun addRecipe(recipe: Recipe)

    suspend fun removeRecipe(recipe: Recipe)
}
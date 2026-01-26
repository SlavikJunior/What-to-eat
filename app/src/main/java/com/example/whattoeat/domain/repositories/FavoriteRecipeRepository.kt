package com.example.whattoeat.domain.repositories

import com.example.whattoeat.domain.domain_entities.common.Recipe

interface FavoriteRecipeRepository {

    suspend fun addRecipe(recipe: Recipe): Boolean

    suspend fun removeRecipe(recipe: Recipe): Boolean
}
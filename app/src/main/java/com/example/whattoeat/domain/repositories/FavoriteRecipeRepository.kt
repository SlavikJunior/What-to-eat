package com.example.whattoeat.domain.repositories

import com.example.whattoeat.data.localSource.entity.FavoriteRecipe
import com.example.whattoeat.domain.models.common.Recipe
import kotlinx.coroutines.flow.Flow

interface FavoriteRecipeRepository {

    suspend fun addRecipe(recipe: Recipe): Long

    suspend fun removeRecipe(recipe: Recipe): Int

    suspend fun getRecipes(): Flow<List<FavoriteRecipe>>

    suspend fun isFavorite(id: Int): Boolean
}
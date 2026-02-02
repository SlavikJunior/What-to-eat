package com.example.whattoeat.domain.repositories

import com.example.whattoeat.domain.domain_entities.common.*
import kotlinx.coroutines.flow.Flow

interface RecipeSearchRepository {

    suspend fun getRecipeComplex(): Flow<RecipeComplex>
    suspend fun getRecipeSimilar(): Flow<RecipeSimilar>
    suspend fun getRecipeSummary(): Flow<RecipeSummary>
    suspend fun getRecipeWithInformation(): Flow<RecipeWithInformation>
    suspend fun getRecipeWithIngredients(): Flow<RecipeWithIngredients>
}
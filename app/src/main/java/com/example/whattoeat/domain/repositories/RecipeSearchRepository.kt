package com.example.whattoeat.domain.repositories

import com.example.whattoeat.domain.domain_entities.common.*
import com.example.whattoeat.domain.search.RecipeSearch
import kotlinx.coroutines.flow.Flow

interface RecipeSearchRepository {

    suspend fun getRecipeComplex(recipeSearch: RecipeSearch.RecipeComplexSearch): Flow<RecipeComplex>
    suspend fun getRecipeSimilar(recipeSearch: RecipeSearch.RecipeSimilarSearch): Flow<RecipeSimilar>
    suspend fun getRecipeSummary(recipeSearch: RecipeSearch.RecipeSummarySearch): Flow<RecipeSummary>
    suspend fun getRecipeFullInformation(recipeSearch: RecipeSearch.RecipeFullInformationSearch): Flow<RecipeFullInformation>
    suspend fun getRecipeByIngredients(recipeSearch: RecipeSearch.RecipeByIngredientsSearch): Flow<RecipeByIngredients>
}
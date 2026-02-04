package com.example.whattoeat.domain.repositories

import com.example.whattoeat.domain.domain_entities.common.*
import com.example.whattoeat.domain.search.RecipeSearch
import kotlinx.coroutines.flow.Flow

interface RecipeSearchRepository {

    suspend fun getRecipeComplex(recipeSearch: RecipeSearch.RecipeComplexSearch): Flow<Resource<RecipeComplex>>
    suspend fun getRecipeSimilar(recipeSearch: RecipeSearch.RecipeSimilarSearch): Flow<Resource<RecipeSimilar>>
    suspend fun getRecipeSummary(recipeSearch: RecipeSearch.RecipeSummarySearch): Flow<Resource<RecipeSummary>>
    suspend fun getRecipeFullInformation(recipeSearch: RecipeSearch.RecipeFullInformationSearch): Flow<Resource<RecipeFullInformation>>
    suspend fun getRecipeByIngredients(recipeSearch: RecipeSearch.RecipeByIngredientsSearch): Flow<Resource<RecipeByIngredients>>
}
package com.example.whattoeat.domain.repositories

import com.example.whattoeat.domain.domain_entities.common.*
import com.example.whattoeat.domain.search.RecipeSearch
import kotlinx.coroutines.flow.Flow

interface RecipeSearchRepository {

    suspend fun getRecipeComplex(recipeSearch: RecipeSearch.RecipeComplexSearch): Flow<Resource<Recipe.RecipeComplex>>
    suspend fun getRecipeSimilar(recipeSearch: RecipeSearch.RecipeSimilarSearch): Flow<Resource<Recipe.RecipeSimilar>>
    suspend fun getRecipeSummary(recipeSearch: RecipeSearch.RecipeSummarySearch): Flow<Resource<Recipe.RecipeSummary>>
    suspend fun getRecipeFullInformation(recipeSearch: RecipeSearch.RecipeFullInformationSearch): Flow<Resource<Recipe.RecipeFullInformation>>
    suspend fun getRecipeByIngredients(recipeSearch: RecipeSearch.RecipeByIngredientsSearch): Flow<Resource<Recipe.RecipeByIngredients>>
}
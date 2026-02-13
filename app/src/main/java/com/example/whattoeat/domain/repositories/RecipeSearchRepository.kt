package com.example.whattoeat.domain.repositories

import com.example.whattoeat.domain.domain_entities.common.*
import com.example.whattoeat.domain.search.RecipeSearch
import kotlinx.coroutines.flow.Flow

interface RecipeSearchRepository {

    fun getRecipeComplex(recipeSearch: RecipeSearch.RecipeComplexSearch): Flow<Resource<RecipeResult.RecipeComplexResult>>
    fun getRecipeSimilar(recipeSearch: RecipeSearch.RecipeSimilarSearch): Flow<Resource<RecipeResult.RecipeSimilarResult>>
    fun getRecipeSummary(recipeSearch: RecipeSearch.RecipeSummarySearch): Flow<Resource<Recipe.RecipeSummary>>
    fun getRecipeFullInformation(recipeSearch: RecipeSearch.RecipeFullInformationSearch): Flow<Resource<RecipeResult.RecipeFullInformationResult>>
    fun getRecipeFullInformationBulk(recipeSearch: RecipeSearch.RecipeFullInformationBulkSearch): Flow<Resource<RecipeResult.RecipeFullInformationBulkResult>>
    fun getRecipeByIngredients(recipeSearch: RecipeSearch.RecipeByIngredientsSearch): Flow<Resource<Recipe.RecipeByIngredients>>
}
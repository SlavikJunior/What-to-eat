package com.example.whattoeat.domain.repositories

import com.example.whattoeat.data.remoteSource.response.RecipeResponse
import com.example.whattoeat.domain.models.common.*
import com.example.whattoeat.data.remoteSource.request.RecipeRequest
import kotlinx.coroutines.flow.Flow

interface RecipeSearchRepository {

    fun getRecipeComplex(recipeSearch: RecipeRequest.RecipeComplexRequest): Flow<Resource<RecipeResponse.RecipeComplexResponse>>
    fun getRecipeSimilar(recipeSearch: RecipeRequest.RecipeSimilarRequest): Flow<Resource<RecipeResponse.RecipeSimilarResponse>>
    fun getRecipeSummary(recipeSearch: RecipeRequest.RecipeSummaryRequest): Flow<Resource<Recipe.RecipeSummary>>
    fun getRecipeFullInformation(recipeSearch: RecipeRequest.RecipeFullInformationRequest): Flow<Resource<RecipeResponse.RecipeFullInformationResponse>>
    fun getRecipeFullInformationBulk(recipeSearch: RecipeRequest.RecipeFullInformationBulkRequest): Flow<Resource<RecipeResponse.RecipeFullInformationBulkResponse>>
    fun getRecipeByIngredients(recipeSearch: RecipeRequest.RecipeByIngredientsRequest): Flow<Resource<RecipeResponse.RecipeByIngredientsResponse>>
}
package com.example.whattoeat.data.net.service

import com.example.whattoeat.domain.domain_entities.common.RecipeResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface SpoonacularApiService {

    @GET(value = "recipes/complexSearch")
    suspend fun recipeComplexSearch(
        @QueryMap(encoded = true) query: Map<String, String>,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Result<RecipeResult.RecipeComplexResult>

    @GET(value = "recipes/findByIngredients")
    suspend fun recipeByIngredients(
        @QueryMap(encoded = true) query: Map<String, String>,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Result<RecipeResult.RecipeByIngredientsResult>

    @GET(value = "recipes/{id}/information")
    suspend fun recipeFullInformation(
        @Path(value = "id") id: Int,
        @QueryMap(encoded = true) query: Map<String, String>,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Result<RecipeResult.RecipeFullInformationResult>

    @GET(value = "recipes/{id}/similar")
    suspend fun recipeSimilar(
        @Path(value = "id") id: Int,
        @QueryMap(encoded = true) query: Map<String, String>,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Result<RecipeResult.RecipeSimilarResult>

    @GET(value = "recipes/{id}/summary")
    suspend fun recipeSummary(
        @Path(value = "id") id: Int,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Result<RecipeResult.RecipeSummaryResult>
}
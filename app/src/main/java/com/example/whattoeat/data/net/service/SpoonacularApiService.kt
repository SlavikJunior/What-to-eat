package com.example.whattoeat.data.net.service

import com.example.whattoeat.domain.domain_entities.common.RecipeResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface SpoonacularApiService {

    @GET(value = "recipes/complexSearch")
    fun recipeComplexSearch(
        @QueryMap(encoded = true) query: Map<String, String>,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Response<RecipeResult.RecipeComplexResult>

    @GET(value = "recipes/findByIngredients")
    fun recipeByIngredients(
        @QueryMap(encoded = true) query: Map<String, String>,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Response<RecipeResult.RecipeByIngredientsResult>

    @GET(value = "recipes/{id}/information")
    fun recipeFullInformation(
        @Path(value = "id") id: Int,
        @QueryMap(encoded = true) query: Map<String, String>,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Response<RecipeResult.RecipeFullInformationResult>

    @GET(value = "recipes/{id}/similar")
    fun recipeSimilar(
        @Path(value = "id") id: Int,
        @QueryMap(encoded = true) query: Map<String, String>,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Response<RecipeResult.RecipeSimilarResult>

    @GET(value = "recipes/{id}/summary")
    fun recipeSummary(
        @Path(value = "id") id: Int,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Response<RecipeResult.RecipeSummaryResult>
}
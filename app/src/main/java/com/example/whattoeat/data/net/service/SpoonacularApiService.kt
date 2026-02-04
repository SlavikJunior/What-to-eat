package com.example.whattoeat.data.net.service

import com.example.whattoeat.domain.domain_entities.common.*
import retrofit2.http.*
import retrofit2.Response

interface SpoonacularApiService {

    @GET(value = "recipes/complexSearch")
    fun recipeComplexSearch(
        @QueryMap(encoded = true) query: Map<String, String>,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Response<RecipeComplexResult>

    @GET(value = "recipes/findByIngredients")
    fun recipeByIngredients(
        @QueryMap(encoded = true) query: Map<String, String>,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Response<RecipeByIngredientsResult>

    @GET(value = "recipes/{id}/information")
    fun recipeFullInformation(
        @Path(value = "id") id: Int,
        @QueryMap(encoded = true) query: Map<String, String>,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Response<RecipeFullInformationResult>

    @GET(value = "recipes/{id}/similar")
    fun recipeSimilar(
        @Path(value = "id") id: Int,
        @QueryMap(encoded = true) query: Map<String, String>,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Response<RecipeSimilarResult>

    @GET(value = "recipes/{id}/summary")
    fun recipeSummary(
        @Path(value = "id") id: Int,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Response<RecipeSummaryResult>
}
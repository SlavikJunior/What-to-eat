package com.example.whattoeat.data.net.service

import com.example.whattoeat.domain.domain_entities.common.RecipeByIngredients
import com.example.whattoeat.domain.domain_entities.common.RecipeComplex
import com.example.whattoeat.domain.domain_entities.common.RecipeFullInformation
import com.example.whattoeat.domain.domain_entities.common.RecipeSimilar
import com.example.whattoeat.domain.domain_entities.common.RecipeSummary
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface SpoonacularApiService {

    @GET(value = "recipes/complexSearch")
    fun recipeComplexSearch(
        @QueryMap(encoded = true) query: Map<String, String>,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Call<RecipeComplex>

    @GET(value = "recipes/findByIngredients")
    fun recipeByIngredients(
        @QueryMap(encoded = true) query: Map<String, String>,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Call<RecipeByIngredients>

    @GET(value = "recipes/{id}/information")
    fun recipeFullInformation(
        @Path(value = "id") id: Int,
        @QueryMap(encoded = true) query: Map<String, String>,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Call<RecipeFullInformation>

    @GET(value = "recipes/{id}/similar")
    fun recipeSimilar(
        @Path(value = "id") id: Int,
        @QueryMap(encoded = true) query: Map<String, String>,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Call<RecipeSimilar>

    @GET(value = "recipes/{id}/summary")
    fun recipeSummary(
        @Path(value = "id") id: Int,
        @Query(value = "apiKey", encoded = true) apiKey: String
    ): Call<RecipeSummary>
}
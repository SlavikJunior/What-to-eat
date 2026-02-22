package com.example.whattoeat.data.remoteSource.response

import com.example.whattoeat.domain.models.common.Recipe
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface RecipeResponse {

    @JvmInline
    @Serializable
    value class RecipeByIngredientsResponse(val recipes: List<Recipe.RecipeByIngredients>): RecipeResponse

    @Serializable
    data class RecipeComplexResponse(
        @SerialName("results") val recipes: List<Recipe.RecipeComplex>,
        @SerialName("offset") val offset: Int,
        @SerialName("number") val number: Int,
        @SerialName("totalResults") val totalResults: Int
    ): RecipeResponse

    @JvmInline
    @Serializable
    value class RecipeFullInformationResponse(val recipes: Recipe.RecipeFullInformation): RecipeResponse

    @JvmInline
    @Serializable
    value class RecipeFullInformationBulkResponse(val recipes: List<Recipe.RecipeFullInformation>): RecipeResponse

    @JvmInline
    @Serializable
    value class RecipeSimilarResponse(val recipes: List<Recipe.RecipeSimilar>): RecipeResponse

    @JvmInline
    @Serializable
    value class RecipeSummaryResponse(val recipes: Recipe.RecipeSummary): RecipeResponse
}
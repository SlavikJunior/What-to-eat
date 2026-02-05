package com.example.whattoeat.domain.domain_entities.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface RecipeResult {

    @JvmInline
    @Serializable
    value class RecipeByIngredientsResult(val recipeByIngredientsResult: List<Recipe.RecipeByIngredients>): RecipeResult

    @Serializable
    data class RecipeComplexResult(
        @SerialName("results") val recipeComplexList: List<Recipe.RecipeComplex>,
        @SerialName("offset") val offset: Int,
        @SerialName("number") val number: Int,
        @SerialName("totalResults") val totalResults: Int
    ): RecipeResult

    @JvmInline
    @Serializable
    value class RecipeFullInformationResult(val recipeFullInformationResult: Recipe.RecipeFullInformation): RecipeResult

    @JvmInline
    @Serializable
    value class RecipeSimilarResult(val recipeSimilarResult: List<Recipe.RecipeSimilar>): RecipeResult

    @JvmInline
    @Serializable
    value class RecipeSummaryResult(val recipeSummary: Recipe.RecipeSummary): RecipeResult
}
package com.example.whattoeat.domain.domain_entities.common

import com.example.whattoeat.domain.domain_entities.support.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Recipe {

    @Serializable
    data class RecipeByUser(
        val image: String,
        val imageType: String,
        val title: String,
        val readyInMinutes: Int, // общее время готовки
        val servings: Int, // порции
        val sourceUrl: String,
        val vegetarian: Boolean,
        val vegan: Boolean,
        val glutenFree: Boolean,
        val dairyFree: Boolean,
        val veryHealthy: Boolean,
        val cheap: Boolean,
        val cookingMinutes: String?, // время готовки
        val healthScore: Double,
        val extendedIngredients: List<Ingredient>,
        val summary: String,
        val cuisines: List<Cuisines>,
        val dishTypes: List<DishTypes>,
        val diets: List<Diets>,
        val occasions: List<String>,
        val instructions: String,
        val steps: List<StepByUser>,
        val notes: String
    ): Recipe

    @Serializable
    data class RecipeByIngredients(
        val id: Int,
        val title: String,
        val image: String,
        val usedIngredientCount: Int,
        val missedIngredientCount: Int,
        val missedIngredients: List<Ingredient>,
        val usedIngredients: List<Ingredient>,
        val unusedIngredients: List<Ingredient>,
        val likes: Int
    ): Recipe

    @Serializable
    data class RecipeComplex(
        @SerialName("id") val id: Int,
        @SerialName("title") val title: String,
        @SerialName("image") val image: String,
        @SerialName("imageType") val imageType: String
    ): Recipe

    @Serializable
    data class RecipeFullInformation(
        val id: Int,
        val image: String,
        val imageType: String,
        val title: String,
        val readyInMinutes: Int, // общее время готовки
        val servings: Int, // порции
        val sourceUrl: String,
        val vegetarian: Boolean,
        val vegan: Boolean,
        val glutenFree: Boolean,
        val dairyFree: Boolean,
        val veryHealthy: Boolean,
        val cheap: Boolean,
        val veryPopular: Boolean,
        val cookingMinutes: String? = null, // время готовки
        val aggregateLikes: Int,
        val healthScore: Double,
        val extendedIngredients: List<Ingredient>,
        val summary: String,
        val cuisines: List<Cuisines> = listOf(),
        val dishTypes: List<DishTypes> = listOf(),
        val diets: List<Diets> = listOf(),
        val occasions: List<String> = emptyList(),
        val instructions: String,
        val analyzedInstructions: List<AnalyzedInstruction>,
        val spoonacularScore: Double,
        val spoonacularSourceUrl: String
    ): Recipe

    @Serializable
    data class RecipeSimilar(
        val id: Int,
        val image: String,
        val imageType: String,
        val title: String,
        val readyInMinutes: Int,
        val servings: Int,
        val sourceUrl: String
    ): Recipe

    @Serializable
    data class RecipeSummary(
        val id: Int,
        val title: String,
        val summary: String
    ): Recipe
}
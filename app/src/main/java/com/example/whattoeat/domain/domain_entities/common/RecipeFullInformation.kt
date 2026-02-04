package com.example.whattoeat.domain.domain_entities.common

import com.example.whattoeat.domain.domain_entities.support.*
import kotlinx.serialization.Serializable

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
    val cuisines: List<Cuisines> = listOf(Cuisines.UNKNOWN),
    val dishTypes: List<DishTypes> = listOf(DishTypes.UNKNOWN),
    val diets: List<Diets> = listOf(Diets.UNKNOWN),
    val occasions: List<String> = emptyList(),
    val instructions: String,
    val analyzedInstructions: List<AnalyzedInstruction>,
    val spoonacularScore: Double,
    val spoonacularSourceUrl: String
): Recipe

@JvmInline
@Serializable
value class RecipeFullInformationResult(val recipeFullInformationResult: RecipeFullInformation): RecipeResult
package com.example.whattoeat.domain.domain_entities.common

import com.example.whattoeat.domain.domain_entities.support.*
import kotlinx.serialization.Serializable

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
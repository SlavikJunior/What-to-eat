package com.example.whattoeat.domain.domain_entities.common

import com.example.whattoeat.domain.domain_entities.support.AnalyzedInstruction
import com.example.whattoeat.domain.domain_entities.support.Cuisines
import com.example.whattoeat.domain.domain_entities.support.Diets
import com.example.whattoeat.domain.domain_entities.support.DishTypes
import com.example.whattoeat.domain.domain_entities.support.Ingredient

data class RecipeInformation(
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
    val cookingMinutes: String?, // время готовки
    val aggregateLikes: Int,
    val healthScore: Double,
    val extendedIngredients: List<Ingredient>,
    val summary: String,
    val cuisines: List<Cuisines>,
    val dishTypes: List<DishTypes>,
    val diets: List<Diets>,
    val occasions: List<String>,
    val instructions: String,
    val analyzedInstructions: List<AnalyzedInstruction>,
    val spoonacularScore: Double,
    val spoonacularSourceUrl: String
): Recipe
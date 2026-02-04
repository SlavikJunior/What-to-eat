package com.example.whattoeat.domain.domain_entities.common

import com.example.whattoeat.domain.domain_entities.support.Ingredient
import kotlinx.serialization.Serializable

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

@JvmInline
@Serializable
value class RecipeByIngredientsResult(val recipeByIngredientsResult: List<RecipeByIngredients>): RecipeResult
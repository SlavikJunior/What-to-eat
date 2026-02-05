package com.example.whattoeat.data.database.entity

import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.domain_entities.support.Cuisines
import com.example.whattoeat.domain.domain_entities.support.Diets
import com.example.whattoeat.domain.domain_entities.support.DishTypes
import com.example.whattoeat.domain.domain_entities.support.Ingredient

abstract class BaseRecipe {
    abstract val id: Int
    abstract val image: String
    abstract val imageType: String
    abstract val title: String
    abstract val readyInMinutes: Int
    abstract val servings: Int?
    abstract val sourceUrl: String?
    abstract val vegetarian: Boolean?
    abstract val vegan: Boolean?
    abstract val glutenFree: Boolean?
    abstract val dairyFree: Boolean?
    abstract val veryHealthy: Boolean?
    abstract val cheap: Boolean?
    abstract val cookingMinutes: String?
    abstract val extendedIngredients: List<Ingredient>
    abstract val summary: String?
    abstract val cuisines: List<Cuisines>?
    abstract val dishTypes: List<DishTypes>?
    abstract val diets: List<Diets>?
    abstract val occasions: List<String>?
    abstract val instructions: String?

    open fun toRecipeFullInformation() =
        Recipe.RecipeFullInformation(
            id = id,
            image = image,
            imageType = imageType,
            title = title,
            readyInMinutes = readyInMinutes,
            servings = servings ?: -1,
            sourceUrl = sourceUrl ?: "",
            vegetarian = vegetarian ?: false,
            vegan = vegan ?: false,
            glutenFree = glutenFree ?: false,
            dairyFree = dairyFree ?: false,
            veryHealthy = false,
            cheap = cheap ?: false,
            veryPopular = false,
            cookingMinutes = cookingMinutes,
            aggregateLikes = -1,
            healthScore = -1.0,
            extendedIngredients = extendedIngredients,
            summary = summary ?: "",
            cuisines = cuisines ?: emptyList(),
            dishTypes = dishTypes ?: emptyList(),
            diets = diets ?: emptyList(),
            occasions = occasions ?: emptyList(),
            instructions = instructions ?: "",
            analyzedInstructions = emptyList(),
            spoonacularScore = -1.0,
            spoonacularSourceUrl = ""
        )
}
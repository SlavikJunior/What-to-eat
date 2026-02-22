package com.example.whattoeat.domain.domainEntities.common

import com.example.whattoeat.domain.domainEntities.support.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Recipe {

    @Serializable
    data class RecipeByUser(
        val id: Int,
        val title: String,
        val readyInMinutes: Int?, // общее время готовки
        val servings: Int?, // порции
        val ingredients: String?,
        val notes: String?
    ) : Recipe

    @Serializable
    open class RecipeByIngredients(
        val id: Int,
        val title: String,
        val image: String,
        val usedIngredientCount: Int,
        val missedIngredientCount: Int,
        val missedIngredients: List<Ingredient>,
        val usedIngredients: List<Ingredient>,
        val unusedIngredients: List<Ingredient>,
        val likes: Int
    ) : Recipe

    @Serializable
    data class RecipeByIngredientsExt(
        val recipe: RecipeByIngredients,
        val isFavorite: Boolean
    ) : RecipeByIngredients(
        id = recipe.id,
        title = recipe.title,
        image = recipe.image,
        usedIngredientCount = recipe.usedIngredientCount,
        missedIngredientCount = recipe.missedIngredientCount,
        missedIngredients = recipe.missedIngredients,
        usedIngredients = recipe.usedIngredients,
        unusedIngredients = recipe.unusedIngredients,
        likes = recipe.likes
    )

    @Serializable
    open class RecipeComplex(
        @SerialName("id") val id: Int,
        @SerialName("title") val title: String,
        @SerialName("image") val image: String,
        @SerialName("imageType") val imageType: String
    ) : Recipe

    @Serializable
    data class RecipeComplexExt(
        val recipe: RecipeComplex,
        val isFavorite: Boolean
    ) : RecipeComplex(
        id = recipe.id,
        title = recipe.title,
        image = recipe.image,
        imageType = recipe.imageType
    )

    @Serializable
    open class RecipeFullInformation(
        /* it's used */ val id: Int,
        /* it's used */ val image: String,
        /* it's used */ val imageType: String,
        /* it's used */ val title: String,
        /* it's used */ val readyInMinutes: Int, // общее время готовки
        /* it's used */ val servings: Int, // порции
        val sourceUrl: String,
        /* it's used */ val vegetarian: Boolean,
        /* it's used */ val vegan: Boolean,
        /* it's used */ val glutenFree: Boolean,
        /* it's used */ val dairyFree: Boolean,
        /* it's used */ val veryHealthy: Boolean,
        val cheap: Boolean,
        val veryPopular: Boolean,
        /* it's used */ val cookingMinutes: String? = null, // время готовки
        /* it's used */ val aggregateLikes: Int,
        /* it's used */ val healthScore: Double,
        /* it's used */ val extendedIngredients: List<Ingredient>,
        /* it's used */ val summary: String,
        val cuisines: List<Cuisines> = listOf(),
        val dishTypes: List<DishTypes> = listOf(),
        val diets: List<Diets> = listOf(),
        val occasions: List<String> = emptyList(),
        /* it's used */ val instructions: String,
        /* it's used */ val analyzedInstructions: List<AnalyzedInstruction>,
        /* it's used */ val spoonacularScore: Double,
        /* it's used */ val spoonacularSourceUrl: String
    ) : Recipe

    @Serializable
    data class RecipeFullInformationExt(
        val recipe: RecipeFullInformation,
        val isFavorite: Boolean
    ) : RecipeFullInformation(
        id = recipe.id,
        image = recipe.image,
        imageType = recipe.imageType,
        title = recipe.title,
        readyInMinutes = recipe.readyInMinutes,
        servings = recipe.servings,
        sourceUrl = recipe.sourceUrl,
        vegetarian = recipe.vegetarian,
        vegan = recipe.vegan,
        glutenFree = recipe.glutenFree,
        dairyFree = recipe.dairyFree,
        veryHealthy = recipe.veryHealthy,
        cheap = recipe.cheap,
        veryPopular = recipe.veryPopular,
        cookingMinutes = recipe.cookingMinutes,
        aggregateLikes = recipe.aggregateLikes,
        healthScore = recipe.healthScore,
        extendedIngredients = recipe.extendedIngredients,
        summary = recipe.summary,
        cuisines = recipe.cuisines,
        dishTypes = recipe.dishTypes,
        diets = recipe.diets,
        occasions = recipe.occasions,
        instructions = recipe.instructions,
        analyzedInstructions = recipe.analyzedInstructions,
        spoonacularScore = recipe.spoonacularScore,
        spoonacularSourceUrl = recipe.spoonacularSourceUrl
    )

    @Serializable
    open class RecipeSimilar(
        val id: Int,
        val image: String,
        val imageType: String,
        val title: String,
        val readyInMinutes: Int,
        val servings: Int,
        val sourceUrl: String
    ) : Recipe

    @Serializable
    data class RecipeSimilarExt(
        val recipe: RecipeSimilar,
        val isFavorite: Boolean
    ) : RecipeSimilar(
        id = recipe.id,
        image = recipe.image,
        imageType = recipe.imageType,
        title = recipe.title,
        readyInMinutes = recipe.readyInMinutes,
        servings = recipe.servings,
        sourceUrl = recipe.sourceUrl
    )

    @Serializable
    data class RecipeSummary(
        val id: Int,
        val title: String,
        val summary: String
    ) : Recipe
}
package com.example.whattoeat.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.whattoeat.data.database.entity.UsersRecipe.Companion.TABLE_NAME
import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.domain_entities.support.Cuisines
import com.example.whattoeat.domain.domain_entities.support.Diets
import com.example.whattoeat.domain.domain_entities.support.DishTypes
import com.example.whattoeat.domain.domain_entities.support.Ingredient

@Entity(
    tableName = TABLE_NAME,
    indices = [
        Index("title"),
        Index("image")
    ]
)

data class UsersRecipe(
    @PrimaryKey(autoGenerate = true)
    override val id: Int = 0,
    @ColumnInfo(name = "image")
    override val image: String? = null,
    @ColumnInfo(name = "image_type")
    override val imageType: String? = null,
    @ColumnInfo(name = "title")
    override val title: String,
    @ColumnInfo(name = "ready_in_minutes")
    override val readyInMinutes: Int,
    @ColumnInfo(name = "servings")
    override val servings: Int? = null,
    @ColumnInfo(name = "source_url")
    override val sourceUrl: String? = null,
    @ColumnInfo(name = "vegetarian")
    override val vegetarian: Boolean? = null,
    @ColumnInfo(name = "vegan")
    override val vegan: Boolean? = null,
    @ColumnInfo(name = "gluten_free")
    override val glutenFree: Boolean? = null,
    @ColumnInfo(name = "dairy_free")
    override val dairyFree: Boolean? = null,
    @ColumnInfo(name = "very_healthy")
    override val veryHealthy: Boolean? = null,
    @ColumnInfo(name = "cheap")
    override val cheap: Boolean? = null,
    @ColumnInfo(name = "cooking_minutes")
    override val cookingMinutes: String? = null,
    @ColumnInfo(name = "extended_ingredients")
    override val extendedIngredients: List<Ingredient>? = null,
    @ColumnInfo(name = "summary")
    override val summary: String? = null,
    @ColumnInfo(name = "cuisines")
    override val cuisines: List<Cuisines>? = null,
    @ColumnInfo(name = "dish_types")
    override val dishTypes: List<DishTypes>? = null,
    @ColumnInfo(name = "diets")
    override val diets: List<Diets>? = null,
    @ColumnInfo(name = "occasions")
    override val occasions: List<String>? = null,
    @ColumnInfo(name = "instructions")
    override val instructions: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) : BaseRecipe() {

    @Ignore
    val extendedIngredientsNames: List<String> = extendedIngredients?.map { it.name } ?: emptyList()

    fun toRecipeByUser(): Recipe.RecipeByUser {
        return Recipe.RecipeByUser(
            vegetarian = vegetarian ?: false,
            vegan = vegan ?: false,
            glutenFree = glutenFree ?: false,
            dairyFree = dairyFree ?: false,
            veryHealthy = veryHealthy ?: false,
            cheap = cheap ?: false,
            healthScore = -1.0,
            summary = summary.orEmpty(),
            cuisines = cuisines ?: emptyList(),
            dishTypes = dishTypes ?: emptyList(),
            diets = diets ?: emptyList(),
            occasions = occasions ?: emptyList(),
            instructions = instructions.orEmpty(),
            image = image,
            imageType = imageType,
            title = title,
            readyInMinutes = readyInMinutes,
            servings = servings ?: -1,
            sourceUrl = sourceUrl.orEmpty(),
            cookingMinutes = cookingMinutes.orEmpty(),
            extendedIngredients = extendedIngredients,
            steps = emptyList(),
            notes = "",
        )
    }

    fun toRecipeByIngredients() =
        Recipe.RecipeByIngredients(
            id = id,
            title = title,
            image = image.orEmpty(),
            usedIngredientCount = -1,
            missedIngredientCount = -1,
            missedIngredients = emptyList(),
            usedIngredients = emptyList(),
            unusedIngredients = emptyList(),
            likes = -1
        )

    companion object {
        const val TABLE_NAME = "users_recipes"

        fun fromRecipe(recipe: Recipe.RecipeByUser): UsersRecipe {
            return UsersRecipe(
                id = 0,
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
                cookingMinutes = recipe.cookingMinutes,
                extendedIngredients = recipe.extendedIngredients,
                summary = recipe.summary,
                cuisines = recipe.cuisines,
                dishTypes = recipe.dishTypes,
                diets = recipe.diets,
                occasions = recipe.occasions,
                instructions = recipe.instructions
            )
        }
    }
}
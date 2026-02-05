package com.example.whattoeat.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
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
    override val image: String,
    @ColumnInfo(name = "image_type")
    override val imageType: String,
    @ColumnInfo(name = "title")
    override val title: String,
    @ColumnInfo(name = "ready_in_minutes")
    override val readyInMinutes: Int,
    @ColumnInfo(name = "servings")
    override val servings: Int?,
    @ColumnInfo(name = "source_url")
    override val sourceUrl: String?,
    @ColumnInfo(name = "vegetarian")
    override val vegetarian: Boolean?,
    @ColumnInfo(name = "vegan")
    override val vegan: Boolean?,
    @ColumnInfo(name = "gluten_free")
    override val glutenFree: Boolean?,
    @ColumnInfo(name = "dairy_free")
    override val dairyFree: Boolean?,
    @ColumnInfo(name = "very_healthy")
    override val veryHealthy: Boolean?,
    @ColumnInfo(name = "cheap")
    override val cheap: Boolean?,
    @ColumnInfo(name = "cooking_minutes")
    override val cookingMinutes: String?,
    @ColumnInfo(name = "extended_ingredients")
    override val extendedIngredients: List<Ingredient>,
    @ColumnInfo(name = "summary")
    override val summary: String?,
    @ColumnInfo(name = "cuisines")
    override val cuisines: List<Cuisines>?,
    @ColumnInfo(name = "dish_types")
    override val dishTypes: List<DishTypes>?,
    @ColumnInfo(name = "diets")
    override val diets: List<Diets>?,
    @ColumnInfo(name = "occasions")
    override val occasions: List<String>?,
    @ColumnInfo(name = "instructions")
    override val instructions: String?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) : BaseRecipe() {

    val extendedIngredientsNames: List<String>
        get() = extendedIngredients.map { it.name }

    fun toRecipeByUser(): Recipe.RecipeByUser {
        return Recipe.RecipeByUser(
            vegetarian = vegetarian ?: false,
            vegan = vegan ?: false,
            glutenFree = glutenFree ?: false,
            dairyFree = dairyFree ?: false,
            veryHealthy = veryHealthy ?: false,
            cheap = cheap ?: false,
            healthScore = -1.0,
            summary = summary ?: "",
            cuisines = cuisines ?: emptyList(),
            dishTypes = dishTypes ?: emptyList(),
            diets = diets ?: emptyList(),
            occasions = occasions ?: emptyList(),
            instructions = instructions ?: "",
            image = image,
            imageType = imageType,
            title = title,
            readyInMinutes = readyInMinutes,
            servings = servings ?: -1,
            sourceUrl = sourceUrl ?: "",
            cookingMinutes = cookingMinutes ?: "",
            extendedIngredients = extendedIngredients,
            steps = emptyList(),
            notes = "",
        )
    }

    fun toRecipeByIngredients() =
        Recipe.RecipeByIngredients(
            id = id,
            title = title,
            image = image,
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
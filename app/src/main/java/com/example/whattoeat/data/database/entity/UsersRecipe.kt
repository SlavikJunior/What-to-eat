package com.example.whattoeat.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.example.whattoeat.data.database.entity.UsersRecipe.Companion.TABLE_NAME
import com.example.whattoeat.domain.domain_entities.common.RecipeByUser
import com.example.whattoeat.domain.domain_entities.common.RecipeFullInformation
import com.example.whattoeat.domain.domain_entities.support.Cuisines
import com.example.whattoeat.domain.domain_entities.support.Diets
import com.example.whattoeat.domain.domain_entities.support.DishTypes
import com.example.whattoeat.domain.domain_entities.support.Ingredient
import java.util.concurrent.atomic.AtomicInteger

@ConsistentCopyVisibility
@Entity(
    tableName = TABLE_NAME,
    primaryKeys = ["id"],
    indices = [
        Index("title"),
        Index("image"),
        Index("extended_ingredients"),
    ]
)
data class UsersRecipe private constructor(
    @Ignore
    val recipe: RecipeByUser,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) : BaseRecipe() {

    constructor(recipeByUser: RecipeByUser) : this(recipe = recipeByUser)

    @ColumnInfo(name = "id")
    override val id: Int = getId()
    @ColumnInfo(name = "image")
    override val image: String = recipe.image
    @ColumnInfo(name = "image_type")
    override val imageType: String = recipe.imageType
    @ColumnInfo(name = "title")
    override val title: String = recipe.title
    @ColumnInfo(name = "ready_in_minutes")
    override val readyInMinutes: Int = recipe.readyInMinutes
    @ColumnInfo(name = "servings")
    override val servings: Int? = recipe.servings
    @ColumnInfo(name = "source_url")
    override val sourceUrl: String? = recipe.sourceUrl
    @ColumnInfo(name = "vegetarian")
    override val vegetarian: Boolean? = recipe.vegetarian
    @ColumnInfo(name = "vegan")
    override val vegan: Boolean? = recipe.vegan
    @ColumnInfo(name = "gluten_free")
    override val glutenFree: Boolean? = recipe.glutenFree
    @ColumnInfo(name = "dairy_free")
    override val dairyFree: Boolean? = recipe.dairyFree
    @ColumnInfo(name = "very_healthy")
    override val veryHealthy: Boolean? = recipe.veryHealthy
    @ColumnInfo(name = "cheap")
    override val cheap: Boolean? = recipe.cheap
    @ColumnInfo(name = "cooking_minutes")
    override val cookingMinutes: String? = recipe.cookingMinutes
    @Ignore
    override val extendedIngredients: List<Ingredient> = recipe.extendedIngredients
    @ColumnInfo(name = "extended_ingredients")
    val extendedIngredientsNames = extendedIngredients.map { ingredient ->
        ingredient.name
    }
    @ColumnInfo(name = "summary")
    override val summary: String? = recipe.summary
    @ColumnInfo(name = "cuisines")
    override val cuisines: List<Cuisines>? = recipe.cuisines
    @ColumnInfo(name = "dish_types")
    override val dishTypes: List<DishTypes>? = recipe.dishTypes
    @ColumnInfo(name = "diets")
    override val diets: List<Diets>? = recipe.diets
    @ColumnInfo(name = "occasions")
    override val occasions: List<String>? = recipe.occasions
    @ColumnInfo(name = "instructions")
    override val instructions: String? = recipe.instructions

    override fun toRecipeFullInformation(): RecipeFullInformation {
        val temp = super.toRecipeFullInformation()
        return temp.copy(
            vegetarian = vegetarian ?: false,
            vegan = vegan ?: false,
            glutenFree = glutenFree ?: false,
            dairyFree = dairyFree ?: false,
            veryHealthy = veryHealthy ?: false,
            cheap = cheap ?: false,
            veryPopular = false,
            healthScore = -1.0,
            summary = summary ?: "",
            cuisines = cuisines ?: emptyList(),
            dishTypes = dishTypes ?: emptyList(),
            diets = diets ?: emptyList(),
            occasions = occasions ?: emptyList(),
            instructions = instructions ?: "",
            analyzedInstructions = emptyList()
        )
    }

    fun toRecipeByUser(): RecipeByUser {
        return RecipeByUser(
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

    companion object {
        const val TABLE_NAME = "users_recipes"

        private val id = AtomicInteger(1)
        fun getId() = id.andIncrement
    }
}
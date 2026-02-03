package com.example.whattoeat.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
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
    primaryKeys = ["id"]
)
data class UsersRecipe private constructor(
    val recipe: RecipeByUser,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) : BaseRecipe() {

    constructor(recipeByUser: RecipeByUser) : this(recipe = recipeByUser)

    override val id: Int = getId()
    override val image: String = recipe.image
    override val imageType: String = recipe.imageType
    override val title: String = recipe.title
    override val readyInMinutes: Int = recipe.readyInMinutes
    override val servings: Int? = recipe.servings
    override val sourceUrl: String? = recipe.sourceUrl
    override val vegetarian: Boolean? = recipe.vegetarian
    override val vegan: Boolean? = recipe.vegan
    override val glutenFree: Boolean? = recipe.glutenFree
    override val dairyFree: Boolean? = recipe.dairyFree
    override val veryHealthy: Boolean? = recipe.veryHealthy
    override val cheap: Boolean? = recipe.cheap
    override val cookingMinutes: String? = recipe.cookingMinutes
    override val extendedIngredients: List<Ingredient> = recipe.extendedIngredients
    override val summary: String? = recipe.summary
    override val cuisines: List<Cuisines>? = recipe.cuisines
    override val dishTypes: List<DishTypes>? = recipe.dishTypes
    override val diets: List<Diets>? = recipe.diets
    override val occasions: List<String>? = recipe.occasions
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

    companion object {
        const val TABLE_NAME = "users_recipes"

        private val id = AtomicInteger(1)
        fun getId() = id.andIncrement
    }
}
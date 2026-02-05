package com.example.whattoeat.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.example.whattoeat.data.database.entity.CachedRecipe.Companion.TABLE_NAME
import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.domain_entities.support.AnalyzedInstruction
import com.example.whattoeat.domain.domain_entities.support.Cuisines
import com.example.whattoeat.domain.domain_entities.support.Diets
import com.example.whattoeat.domain.domain_entities.support.DishTypes
import com.example.whattoeat.domain.domain_entities.support.Ingredient

@Entity(
    tableName = TABLE_NAME,
    primaryKeys = ["id"],
    indices = [Index("title")]
)
data class CachedRecipe(
    override val id: Int,
    override val image: String,
    @ColumnInfo(name = "image_type")
    override val imageType: String,
    override val title: String,
    @ColumnInfo("ready_in_minutes")
    override val readyInMinutes: Int, // общее время готовки
    override val servings: Int, // порции
    @ColumnInfo(name = "source_url")
    override val sourceUrl: String,
    override val vegetarian: Boolean,
    override val vegan: Boolean,
    @ColumnInfo(name = "gluten_free")
    override val glutenFree: Boolean,
    @ColumnInfo(name = "dairy_free")
    override val dairyFree: Boolean,
    @ColumnInfo(name = "very_healthy")
    override val veryHealthy: Boolean,
    override val cheap: Boolean,
    @ColumnInfo(name = "very_popular")
    val veryPopular: Boolean,
    @ColumnInfo(name = "cooking_minutes")
    override val cookingMinutes: String? = null, // время готовки
    @ColumnInfo(name = "health_score")
    val healthScore: Double,
    @ColumnInfo(name = "extended_ingredients")
    override val extendedIngredients: List<Ingredient>,
    override val summary: String,
    override val cuisines: List<Cuisines> = listOf(Cuisines.UNKNOWN),
    @ColumnInfo(name = "dish_types")
    override val dishTypes: List<DishTypes> = listOf(DishTypes.UNKNOWN),
    override val diets: List<Diets> = listOf(Diets.UNKNOWN),
    override val occasions: List<String> = emptyList(),
    override val instructions: String,
    @ColumnInfo(name = "analyzed_instructions")
    val analyzedInstructions: List<AnalyzedInstruction>,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "delete_to")
    val deleteTo: Long
) : BaseRecipe() {

    override fun toRecipeFullInformation(): Recipe.RecipeFullInformation {
        val temp = super.toRecipeFullInformation()
        return temp.copy(
            vegetarian = vegetarian,
            vegan = vegan,
            glutenFree = glutenFree,
            dairyFree = dairyFree,
            veryHealthy = veryHealthy,
            cheap = cheap,
            veryPopular = veryPopular,
            healthScore = healthScore,
            summary = summary,
            cuisines = cuisines,
            dishTypes = dishTypes,
            diets = diets,
            occasions = occasions,
            instructions = instructions,
            analyzedInstructions = analyzedInstructions
        )
    }

    fun fromRecipeFullInformation(recipe: Recipe.RecipeFullInformation) = with(recipe) {
        CachedRecipe(
            id = id,
            image = image,
            imageType = imageType,
            title = title,
            readyInMinutes = readyInMinutes,
            servings = servings,
            sourceUrl = sourceUrl,
            vegetarian = vegetarian,
            vegan = vegan,
            glutenFree = glutenFree,
            dairyFree = dairyFree,
            veryHealthy = veryHealthy,
            cheap = cheap,
            veryPopular = veryPopular,
            cookingMinutes = cookingMinutes,
            healthScore = healthScore,
            extendedIngredients = extendedIngredients,
            summary = summary,
            cuisines = cuisines,
            dishTypes = dishTypes,
            diets = diets,
            occasions = occasions,
            instructions = instructions,
            analyzedInstructions = analyzedInstructions,
            createdAt = createdAt,
            updatedAt = updatedAt,
            deleteTo = deleteTo
        )
    }

    companion object {
        const val TABLE_NAME = "cached_recipes"
    }
}
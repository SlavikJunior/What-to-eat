package com.example.whattoeat.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.whattoeat.data.database.entity.UsersRecipe.Companion.TABLE_NAME
import com.example.whattoeat.domain.domainEntities.common.Recipe
import com.example.whattoeat.domain.domainEntities.support.Cuisines
import com.example.whattoeat.domain.domainEntities.support.Diets
import com.example.whattoeat.domain.domainEntities.support.DishTypes
import com.example.whattoeat.domain.domainEntities.support.Ingredient

@Entity(
    tableName = TABLE_NAME,
    indices = [
        Index("title")
    ]
)

data class UsersRecipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "ready_in_minutes")
    val readyInMinutes: Int?,
    @ColumnInfo(name = "servings")
    val servings: Int? = null,
    @ColumnInfo(name = "extended_ingredients")
    val ingredients: String?,
    @ColumnInfo(name = "notes")
    val notes: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) {

    fun toRecipeByUser(): Recipe.RecipeByUser {
        return Recipe.RecipeByUser(
            title = title,
            readyInMinutes = readyInMinutes,
            servings = servings ?: -1,
            ingredients = ingredients.orEmpty(),
            notes = notes.orEmpty()
        )
    }

    companion object {
        const val TABLE_NAME = "users_recipes"

        fun fromRecipe(recipe: Recipe.RecipeByUser): UsersRecipe {
            return UsersRecipe(
                id = 0,
                title = recipe.title,
                readyInMinutes = recipe.readyInMinutes,
                servings = recipe.servings,
                ingredients = recipe.ingredients
            )
        }
    }
}
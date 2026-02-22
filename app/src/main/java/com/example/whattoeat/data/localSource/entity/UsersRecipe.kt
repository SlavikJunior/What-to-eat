package com.example.whattoeat.data.localSource.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.whattoeat.data.localSource.entity.UsersRecipe.Companion.TABLE_NAME
import com.example.whattoeat.domain.models.common.Recipe

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
            id = id,
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
                id = recipe.id,
                title = recipe.title,
                readyInMinutes = recipe.readyInMinutes,
                servings = recipe.servings,
                ingredients = recipe.ingredients,
                notes = recipe.notes
            )
        }
    }
}
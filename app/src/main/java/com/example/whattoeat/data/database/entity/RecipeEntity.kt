package com.example.whattoeat.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.whattoeat.data.database.entity.RecipeEntity.Companion.TABLE_NAME
import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.domain_entities.support.CookingTime
import com.example.whattoeat.domain.domain_entities.support.Portions
import com.example.whattoeat.domain.domain_entities.support.Product
import com.example.whattoeat.domain.domain_entities.support.Step

@Entity(tableName = TABLE_NAME)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(index = true)
    val title: String,
    val description: String,
    val image: String? = null,
    @ColumnInfo(name = "full_description")
    val fullDescription: String? = null,
    @ColumnInfo(name = "cooking_time")
    val cookingTime: CookingTime? = null,
    val portions: Portions? = null,
    @ColumnInfo(index = true)
    val products: List<Product>? = null,
    val video: String? = null,
    val steps: List<Step>? = null
) {
    companion object {
        private const val TABLE_NAME = "recipes"

        fun fromRecipe(recipe: Recipe) =
            RecipeEntity(
                title = recipe.title,
                description = recipe.description,
                image = recipe.image,
                fullDescription = recipe.fullDescription,
                cookingTime = recipe.cookingTime,
                portions = recipe.portions,
                products = recipe.products,
                video = recipe.video,
                steps = recipe.steps
            )
    }
}
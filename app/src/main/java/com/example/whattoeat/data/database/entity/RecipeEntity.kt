package com.example.whattoeat.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.whattoeat.data.database.entity.RecipeEntity.Companion.TABLE_NAME
import com.example.whattoeat.data.net.toListWithSeparator
import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.domain_entities.support.CookingTime
import com.example.whattoeat.domain.domain_entities.support.Portions

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
    val cookingTime: String? = null,
    val portions: String? = null,
    val products: String? = null,
    val video: String? = null,
    val steps: String? = null
) {

    fun toRecipe() =
        Recipe(
            title = this.title,
            description = this.description,
            image = this.image,
            fullDescription = this.fullDescription,
            cookingTime = this.cookingTime?.let { CookingTime(it) },
            portions = this.portions?.let { Portions(it) },
            products = this.products?.toListWithSeparator(";;"),
            video = this.video,
            steps = this.steps?.toListWithSeparator(";;"),
        )
    companion object {
        private const val TABLE_NAME = "recipes"

        fun fromRecipe(recipe: Recipe) =
            RecipeEntity(
                title = recipe.title,
                description = recipe.description,
                image = recipe.image,
                fullDescription = recipe.fullDescription,
                cookingTime = recipe.cookingTime.toString(),
                portions = recipe.portions.toString(),
                products = recipe.products?.joinToString(";;"),
                video = recipe.video,
                steps = recipe.steps?.joinToString(";;")
            )
    }
}
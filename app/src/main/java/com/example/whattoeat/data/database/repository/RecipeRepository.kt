package com.example.whattoeat.data.database.repository

import com.example.whattoeat.data.database.dao.RecipeDAO
import com.example.whattoeat.data.database.entity.RecipeEntity
import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.repositories.FavoriteRecipeRepository

class RecipeRepository(
    private val recipeDAO: RecipeDAO
): FavoriteRecipeRepository {
    override suspend fun addRecipe(recipe: Recipe) =
        recipeDAO.insert(RecipeEntity.fromRecipe(recipe))

    override suspend fun removeRecipe(recipe: Recipe) =
        recipeDAO.delete(RecipeEntity.fromRecipe(recipe))

    suspend fun isStored(recipe: Recipe) =
        recipeDAO.isStored(recipe.title, recipe.description)
}
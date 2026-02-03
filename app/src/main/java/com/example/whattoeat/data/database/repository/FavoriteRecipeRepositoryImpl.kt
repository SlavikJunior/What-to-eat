package com.example.whattoeat.data.database.repository

import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.repositories.FavoriteRecipeRepository
import com.example.whattoeat.domain.search.RecipeSearch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRecipeRepositoryImpl @Inject constructor(

): FavoriteRecipeRepository {
    override suspend fun addRecipe(recipe: Recipe): Long {
        TODO("Not yet implemented")
    }

    override suspend fun removeRecipe(recipe: Recipe): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipes(recipeSearch: RecipeSearch): Flow<Recipe> {
        TODO("Not yet implemented")
    }
}
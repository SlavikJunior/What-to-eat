package com.example.whattoeat.data.net.repository

import com.example.whattoeat.data.net.client.RussianFoodComClient
import com.example.whattoeat.domain.domain_entities.support.RecipeSearch
import com.example.whattoeat.domain.repositories.RecipeSearchRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeSearchRepositoryImpl @Inject constructor(
    private val client: RussianFoodComClient
): RecipeSearchRepository {

    override fun getRecipesByRecipeSearch(recipeSearch: RecipeSearch) = flow {
        try {
            client.searchRecipes(recipeSearch).collect { recipe ->
                emit(recipe)
            }
        } catch (_: Exception) {}
    }
}
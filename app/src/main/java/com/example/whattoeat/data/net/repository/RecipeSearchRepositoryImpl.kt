package com.example.whattoeat.data.net.repository

import com.example.whattoeat.data.net.client.RussianFoodComClient
import com.example.whattoeat.domain.domain_entities.support.RecipeSearch
import com.example.whattoeat.domain.repositories.RecipeSearchRepository

class RecipeSearchRepositoryImpl(
    private val client: RussianFoodComClient
): RecipeSearchRepository {

    override fun getRecipesByRecipeSearch(recipeSearch: RecipeSearch) =
        client.searchRecipes(recipeSearch)
}
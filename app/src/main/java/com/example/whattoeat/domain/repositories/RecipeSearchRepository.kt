package com.example.whattoeat.domain.repositories

import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.domain_entities.support.RecipeSearch
import kotlinx.coroutines.flow.Flow

@FunctionalInterface
interface RecipeSearchRepository {

    fun getRecipesByRecipeSearch(recipeSearch: RecipeSearch): Flow<List<Recipe>>
}
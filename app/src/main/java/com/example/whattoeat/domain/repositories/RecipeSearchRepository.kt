package com.example.whattoeat.domain.repositories

import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.domain_entities.support.RecipeSearch
import kotlinx.coroutines.flow.Flow

@FunctionalInterface
interface RecipeSearchRepository {
    // Возвращаем Flow отдельных рецептов, а не списка
    fun getRecipesByRecipeSearch(recipeSearch: RecipeSearch): Flow<Recipe>
}
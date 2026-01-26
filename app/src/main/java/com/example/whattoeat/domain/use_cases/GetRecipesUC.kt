package com.example.whattoeat.domain.use_cases

import com.example.whattoeat.domain.domain_entities.support.RecipeSearch
import com.example.whattoeat.domain.repositories.RecipeSearchRepository

class GetRecipesUC(
    private val repository: RecipeSearchRepository
) {

    operator fun invoke(recipeSearch: RecipeSearch) =
        repository.getRecipesByRecipeSearch(recipeSearch)
}
package com.example.whattoeat.domain.use_cases

import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.repositories.FavoriteRecipeRepository

class AddFavoriteRecipeUC(
    private val repository: FavoriteRecipeRepository
) {
    suspend operator fun invoke(recipe: Recipe) =
        repository.addRecipe(recipe)
}
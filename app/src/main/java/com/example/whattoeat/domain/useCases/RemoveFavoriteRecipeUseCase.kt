package com.example.whattoeat.domain.useCases

import com.example.whattoeat.domain.models.common.Recipe
import com.example.whattoeat.domain.repositories.FavoriteRecipeRepository
import javax.inject.Inject

class RemoveFavoriteRecipeUseCase @Inject constructor(
    private val repository: FavoriteRecipeRepository
) {

    suspend operator fun invoke(recipe: Recipe) =
        repository.removeRecipe(recipe)
}
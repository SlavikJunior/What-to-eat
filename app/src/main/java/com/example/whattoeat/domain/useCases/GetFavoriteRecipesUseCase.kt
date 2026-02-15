package com.example.whattoeat.domain.useCases

import com.example.whattoeat.domain.repositories.FavoriteRecipeRepository
import javax.inject.Inject

class GetFavoriteRecipesUseCase @Inject constructor(
    private val repository: FavoriteRecipeRepository
) {

    suspend operator fun invoke() =
        repository.getRecipes()
}
package com.example.whattoeat.domain.use_cases

import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.repositories.FavoriteRecipeRepository
import javax.inject.Inject

class IsFavoriteRecipeUseCase @Inject constructor(
    private val repository: FavoriteRecipeRepository
) {

    suspend operator fun invoke(recipe: Recipe) = when (recipe) {
        is Recipe.RecipeComplex -> repository.isFavorite(recipe.id)
        is Recipe.RecipeFullInformation -> repository.isFavorite(recipe.id)
        is Recipe.RecipeSimilar -> repository.isFavorite(recipe.id)
        else -> false
    }
}
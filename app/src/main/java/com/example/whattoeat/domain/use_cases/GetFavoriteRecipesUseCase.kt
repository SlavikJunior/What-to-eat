package com.example.whattoeat.domain.use_cases

import com.example.whattoeat.domain.repositories.FavoriteRecipeRepository
import com.example.whattoeat.domain.search.RecipeSearch
import javax.inject.Inject

class GetFavoriteRecipesUseCase @Inject constructor(
    private val repository: FavoriteRecipeRepository
) {

    suspend operator fun invoke(recipeSearch: RecipeSearch) =
        repository.getRecipes(recipeSearch)
}
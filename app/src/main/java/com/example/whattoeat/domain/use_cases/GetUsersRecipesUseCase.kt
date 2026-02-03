package com.example.whattoeat.domain.use_cases

import com.example.whattoeat.domain.repositories.UsersRecipeRepository
import com.example.whattoeat.domain.search.RecipeSearch
import javax.inject.Inject

class GetUsersRecipesUseCase @Inject constructor(
    private val repository: UsersRecipeRepository
) {

    suspend operator fun invoke(recipeSearch: RecipeSearch) =
        repository.getRecipes(recipeSearch)
}
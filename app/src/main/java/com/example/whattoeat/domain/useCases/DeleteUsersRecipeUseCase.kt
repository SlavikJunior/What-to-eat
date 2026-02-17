package com.example.whattoeat.domain.useCases

import com.example.whattoeat.domain.domainEntities.common.Recipe
import com.example.whattoeat.domain.repositories.UsersRecipeRepository
import javax.inject.Inject

class DeleteUsersRecipeUseCase @Inject constructor(
    private val repository: UsersRecipeRepository
) {

    suspend operator fun invoke(recipe: Recipe.RecipeByUser) =
        repository.deleteRecipe(recipe)
}
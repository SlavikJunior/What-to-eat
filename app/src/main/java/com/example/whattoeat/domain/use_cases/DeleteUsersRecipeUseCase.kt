package com.example.whattoeat.domain.use_cases

import com.example.whattoeat.domain.domain_entities.common.RecipeByUser
import com.example.whattoeat.domain.repositories.UsersRecipeRepository
import javax.inject.Inject

class DeleteUsersRecipeUseCase @Inject constructor(
    private val repository: UsersRecipeRepository
) {

    suspend operator fun invoke(recipe: RecipeByUser) =
        repository.deleteRecipe(recipe)
}
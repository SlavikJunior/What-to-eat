package com.example.whattoeat.domain.useCases

import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.repositories.UsersRecipeRepository
import javax.inject.Inject

class UploadUsersRecipeUseCase @Inject constructor(
    private val repository: UsersRecipeRepository
) {

    suspend operator fun invoke(recipe: Recipe.RecipeByUser) =
        repository.uploadRecipe(recipe)
}
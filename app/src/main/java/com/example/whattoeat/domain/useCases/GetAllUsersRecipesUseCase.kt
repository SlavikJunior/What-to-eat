package com.example.whattoeat.domain.useCases

import com.example.whattoeat.domain.repositories.UsersRecipeRepository
import javax.inject.Inject

class GetAllUsersRecipesUseCase @Inject constructor(
    private val repository: UsersRecipeRepository
) {

    suspend operator fun invoke() =
        repository.getAllRecipes()
}
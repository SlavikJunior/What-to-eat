package com.example.whattoeat.domain.use_cases

import com.example.whattoeat.domain.repositories.RecipeSearchRepository
import com.example.whattoeat.domain.search.RecipeSearch
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val repository: RecipeSearchRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(recipeSearch: RecipeSearch) =
        when(recipeSearch) {
            is RecipeSearch.RecipeByIngredientsSearch -> repository.getRecipeByIngredients(recipeSearch)
            is RecipeSearch.RecipeComplexSearch -> repository.getRecipeComplex(recipeSearch)
            is RecipeSearch.RecipeFullInformationSearch -> repository.getRecipeFullInformation(recipeSearch)
            is RecipeSearch.RecipeSimilarSearch -> repository.getRecipeSimilar(recipeSearch)
            is RecipeSearch.RecipeSummarySearch -> repository.getRecipeSummary(recipeSearch)
        }

    companion object {
        private const val TAG = "GetRecipesUseCase"
    }
}
package com.example.whattoeat.domain.useCases

import com.example.whattoeat.domain.repositories.RecipeSearchRepository
import com.example.whattoeat.data.remoteSource.request.RecipeRequest
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val repository: RecipeSearchRepository
) {
    operator fun invoke(recipeSearch: RecipeRequest) =
        when(recipeSearch) {
            is RecipeRequest.RecipeByIngredientsRequest -> repository.getRecipeByIngredients(recipeSearch)
            is RecipeRequest.RecipeComplexRequest -> repository.getRecipeComplex(recipeSearch)
            is RecipeRequest.RecipeFullInformationRequest -> repository.getRecipeFullInformation(recipeSearch)
            is RecipeRequest.RecipeFullInformationBulkRequest -> repository.getRecipeFullInformationBulk(recipeSearch)
            is RecipeRequest.RecipeSimilarRequest -> repository.getRecipeSimilar(recipeSearch)
            is RecipeRequest.RecipeSummaryRequest -> repository.getRecipeSummary(recipeSearch)
        }

    companion object {
        private const val TAG = "TEST TAG"
    }
}
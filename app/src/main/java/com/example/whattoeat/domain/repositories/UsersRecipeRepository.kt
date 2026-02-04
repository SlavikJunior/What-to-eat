package com.example.whattoeat.domain.repositories

import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.search.RecipeSearch
import kotlinx.coroutines.flow.Flow

interface UsersRecipeRepository {

    suspend fun uploadRecipe(recipe: Recipe.RecipeByUser): Long

    suspend fun deleteRecipe(recipe: Recipe.RecipeByUser): Int

    suspend fun getRecipes(recipeSearch: RecipeSearch): Flow<Recipe.RecipeByUser>

    suspend fun getRecipesAsRecipeComplex(recipeSearch: RecipeSearch.RecipeComplexSearch): Flow<Recipe.RecipeComplex>

    suspend fun getRecipesAsRecipeFullInformation(recipeSearch: RecipeSearch.RecipeFullInformationSearch): Flow<Recipe.RecipeFullInformation>

    suspend fun getRecipesAsRecipeByIngredients(recipeSearch: RecipeSearch.RecipeByIngredientsSearch): Flow<Recipe.RecipeByIngredients>
}
package com.example.whattoeat.domain.repositories

import com.example.whattoeat.domain.domain_entities.common.RecipeByIngredients
import com.example.whattoeat.domain.domain_entities.common.RecipeByUser
import com.example.whattoeat.domain.domain_entities.common.RecipeComplex
import com.example.whattoeat.domain.domain_entities.common.RecipeFullInformation
import com.example.whattoeat.domain.search.RecipeSearch
import kotlinx.coroutines.flow.Flow

interface UsersRecipeRepository {

    suspend fun uploadRecipe(recipe: RecipeByUser): Long

    suspend fun deleteRecipe(recipe: RecipeByUser): Int

    suspend fun getRecipes(recipeSearch: RecipeSearch): Flow<RecipeByUser>

    suspend fun getRecipesAsRecipeComplex(recipeSearch: RecipeSearch.RecipeComplexSearch): Flow<RecipeComplex>

    suspend fun getRecipesAsRecipeFullInformation(recipeSearch: RecipeSearch.RecipeFullInformationSearch): Flow<RecipeFullInformation>

    suspend fun getRecipesAsRecipeByIngredients(recipeSearch: RecipeSearch.RecipeByIngredientsSearch): Flow<RecipeByIngredients>
}
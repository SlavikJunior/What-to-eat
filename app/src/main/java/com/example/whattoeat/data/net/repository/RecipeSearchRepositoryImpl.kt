package com.example.whattoeat.data.net.repository

import com.example.whattoeat.data.database.dao.CachedRecipeDao
import com.example.whattoeat.data.database.dao.UsersRecipeDao
import com.example.whattoeat.data.net.service.SpoonacularApiService
import com.example.whattoeat.domain.domain_entities.common.RecipeByIngredients
import com.example.whattoeat.domain.domain_entities.common.RecipeComplex
import com.example.whattoeat.domain.domain_entities.common.RecipeFullInformation
import com.example.whattoeat.domain.domain_entities.common.RecipeSimilar
import com.example.whattoeat.domain.domain_entities.common.RecipeSummary
import com.example.whattoeat.domain.repositories.RecipeSearchRepository
import com.example.whattoeat.domain.search.RecipeSearch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipeSearchRepositoryImpl @Inject constructor(
    val service: SpoonacularApiService,
    val usersRecipeDao: UsersRecipeDao,
    val cachedRecipeDao: CachedRecipeDao
): RecipeSearchRepository {
    override suspend fun getRecipeComplex(recipeSearch: RecipeSearch.RecipeComplexSearch): Flow<RecipeComplex> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeSimilar(recipeSearch: RecipeSearch.RecipeSimilarSearch): Flow<RecipeSimilar> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeSummary(recipeSearch: RecipeSearch.RecipeSummarySearch): Flow<RecipeSummary> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeFullInformation(recipeSearch: RecipeSearch.RecipeFullInformationSearch): Flow<RecipeFullInformation> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeByIngredients(recipeSearch: RecipeSearch.RecipeByIngredientsSearch): Flow<RecipeByIngredients> {
        TODO("Not yet implemented")
    }
}
package com.example.whattoeat.data.net.repository

import android.util.Log
import com.example.whattoeat.data.database.dao.CachedRecipeComplexDao
import com.example.whattoeat.data.database.entity.CachedRecipeComplex
import com.example.whattoeat.data.net.service.SpoonacularApiService
import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.domain_entities.common.Resource
import com.example.whattoeat.domain.repositories.RecipeSearchRepository
import com.example.whattoeat.domain.search.RecipeSearch
import com.example.whattoeat.domain.search.toQueryMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeSearchRepositoryImpl @Inject constructor(
    val apiKey: String,
    val service: SpoonacularApiService,
    val cachedRecipeDao: CachedRecipeComplexDao
) : RecipeSearchRepository {

    override suspend fun getRecipeComplex(recipeSearch: RecipeSearch.RecipeComplexSearch) =
        flow {
            emit(Resource.Loading())

            service.recipeComplexSearch(
                query = recipeSearch.toQueryMap(),
                apiKey = apiKey
            ).let { result ->

                result.onSuccess { recipeComplexResult ->
                    recipeComplexResult.recipeComplexList.forEach {recipeComplex ->
                        Log.d(TAG, "Emitting... : $recipeComplex")
                        emit(Resource.Success(recipeComplex))
                    }
                }

                result.onFailure {
                    emit(Resource.Error("Not found :/"))
                }
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getRecipeSimilar(recipeSearch: RecipeSearch.RecipeSimilarSearch): Flow<Resource<Recipe.RecipeSimilar>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeSummary(recipeSearch: RecipeSearch.RecipeSummarySearch): Flow<Resource<Recipe.RecipeSummary>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeFullInformation(recipeSearch: RecipeSearch.RecipeFullInformationSearch) =
        flow {
            emit(Resource.Loading())

            service.recipeFullInformation(
                id = recipeSearch.id,
                query = recipeSearch.toQueryMap(),
                apiKey = apiKey
            ).let { result ->

                result.onSuccess {
                    emit(Resource.Success(it.recipeFullInformationResult))
                }

                result.onFailure {
                    emit(tryToGetFromCache(recipeSearch))
                }
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getRecipeByIngredients(recipeSearch: RecipeSearch.RecipeByIngredientsSearch) =
        flow {
            emit(Resource.Loading())

            service.recipeByIngredients(
                query = recipeSearch.toQueryMap(),
                apiKey = apiKey
            ).let { result ->

                result.onSuccess {
                    it.recipeByIngredientsResult.forEach { recipeByIngredients ->
                        emit(Resource.Success(recipeByIngredients))
                    }
                }

                result.onFailure {
                    emit(Resource.Error("Not found :/"))
                }
            }
        }.flowOn(Dispatchers.IO)

    @Suppress("UNCHECKED_CAST")
    private suspend fun tryToGetFromCache(recipeSearch: RecipeSearch.RecipeFullInformationSearch): Resource<Recipe.RecipeFullInformation> {
        var fromCachedRecipe: Recipe.RecipeFullInformation? = null
        var cachedRecipe: CachedRecipeComplex? = null
        withContext(Dispatchers.IO) {
            cachedRecipe = async {
                cachedRecipeDao.selectById(recipeSearch.id)
            }.await()
        }

        if (cachedRecipe != null)
            fromCachedRecipe = cachedRecipe.toRecipeFullInformation()

        return if (fromCachedRecipe != null)
            Resource.Success(cachedRecipe) as Resource<Recipe.RecipeFullInformation>
        else Resource.Error("Not found :/")
    }

    companion object {
        private const val TAG = "TEST TAG"
    }
}
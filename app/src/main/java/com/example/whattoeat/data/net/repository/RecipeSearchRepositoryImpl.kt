package com.example.whattoeat.data.net.repository

import android.util.Log
import com.example.whattoeat.data.database.dao.CachedRecipeComplexDao
import com.example.whattoeat.data.database.entity.CachedRecipeComplex
import com.example.whattoeat.data.net.service.SpoonacularApiService
import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.domain_entities.common.RecipeResult
import com.example.whattoeat.domain.domain_entities.common.Resource
import com.example.whattoeat.domain.repositories.RecipeSearchRepository
import com.example.whattoeat.domain.search.RecipeSearch
import com.example.whattoeat.domain.search.toQueryMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class RecipeSearchRepositoryImpl @Inject constructor(
    val json: Json,
    val apiKey: String,
    val service: SpoonacularApiService,
    val cachedRecipeDao: CachedRecipeComplexDao
) : RecipeSearchRepository {

    override suspend fun getRecipeComplex(recipeSearch: RecipeSearch.RecipeComplexSearch): Flow<Resource<RecipeResult.RecipeComplexResult>> {
        var listFromCache: List<Recipe.RecipeComplex> = emptyList()
        val flow = flow {
            emit(Resource.Loading())

//            listFromCache = tryToGetFromCache(recipeSearch)
//            if(listFromCache.isNotEmpty()) {
//                return listFromCache.map { Resource.Success(data = it) }.asFlow()
//            }

            service.recipeComplexSearch(
                query = recipeSearch.toQueryMap(),
                apiKey = apiKey
            ).let { result ->

                result.onSuccess { recipeComplexResult ->
                    Log.d(TAG, "Emitting... : $recipeComplexResult")
                    emit(Resource.Success(recipeComplexResult))

                }

                result.onFailure {
                    emit(Resource.Error("Not found :/"))
                }
            }
        }.flowOn(Dispatchers.IO)
        return flow
    }

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
                    emit(Resource.Error("Not found :/"))
//                    emit(tryToGetFromCache(recipeSearch))
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


    private suspend fun tryToGetFromCache(recipeSearch: RecipeSearch.RecipeComplexSearch): List<Recipe.RecipeComplex> {
        val hash = getRecipeComplexSearchHash(recipeSearch)
        try {
            val list = cachedRecipeDao.selectByHash(hash)
            return list.map { cachedRecipeComplex ->
                decodeCachedRecipe(cachedRecipeComplex)
            }
        } catch (_: Throwable) {
            return emptyList()
        }
    }

    private fun decodeCachedRecipe(cachedRecipeComplex: CachedRecipeComplex) =
        json.decodeFromString<Recipe.RecipeComplex>(cachedRecipeComplex.recipeComplexBody)

    private fun getRecipeComplexSearchHash(recipeSearch: RecipeSearch.RecipeComplexSearch) =
        recipeSearch.copy(
            offset = null,
            number = null
        ).hashCode().toString()

    companion object {
        private const val TAG = "TEST TAG"
    }
}
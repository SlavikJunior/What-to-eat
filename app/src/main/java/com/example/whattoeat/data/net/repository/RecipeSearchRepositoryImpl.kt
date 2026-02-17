package com.example.whattoeat.data.net.repository

import android.util.Log
import com.example.whattoeat.data.net.service.SpoonacularApiService
import com.example.whattoeat.di.IoDispatcher
import com.example.whattoeat.di.SpoonacularJson
import com.example.whattoeat.domain.domainEntities.common.Recipe
import com.example.whattoeat.domain.domainEntities.common.RecipeResult
import com.example.whattoeat.domain.domainEntities.common.Resource
import com.example.whattoeat.domain.repositories.RecipeSearchRepository
import com.example.whattoeat.domain.search.RecipeSearch
import com.example.whattoeat.domain.search.toQueryMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import javax.inject.Inject

class RecipeSearchRepositoryImpl @Inject constructor(
    @SpoonacularJson val json: Json,
    val apiKey: String,
    val service: SpoonacularApiService,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RecipeSearchRepository {

    override fun getRecipeComplex(recipeSearch: RecipeSearch.RecipeComplexSearch): Flow<Resource<RecipeResult.RecipeComplexResult>> {
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
        }.flowOn(ioDispatcher)
        return flow
    }

    override fun getRecipeSimilar(recipeSearch: RecipeSearch.RecipeSimilarSearch): Flow<Resource<RecipeResult.RecipeSimilarResult>> =
        flow {
            Log.d(TAG, "RecipeSearch: $recipeSearch")

            emit(Resource.Loading())

            service.recipeSimilar(
                id = recipeSearch.id,
                query = recipeSearch.toQueryMap(),
                apiKey = apiKey
            ).let { result ->

                result.onSuccess {
                    Log.d(TAG, "Success emiting: ${it.recipeSimilarResult}")

                    emit(Resource.Success(RecipeResult.RecipeSimilarResult(it.recipeSimilarResult)))
                }

                result.onFailure {
                    Log.d(TAG, "Error emiting! Throwable: $it")

                    emit(Resource.Error("Not found :/"))
//                    emit(tryToGetFromCache(recipeSearch))
                }
            }
        }

    override fun getRecipeSummary(recipeSearch: RecipeSearch.RecipeSummarySearch): Flow<Resource<Recipe.RecipeSummary>> {
        TODO("Not yet implemented")
    }

    override fun getRecipeFullInformation(recipeSearch: RecipeSearch.RecipeFullInformationSearch): Flow<Resource<RecipeResult.RecipeFullInformationResult>> =
        flow {
            Log.d(TAG, "RecipeSearch: $recipeSearch")

            emit(Resource.Loading())

            service.recipeFullInformation(
                id = recipeSearch.id,
                query = recipeSearch.toQueryMap(),
                apiKey = apiKey
            ).let { result ->

                result.onSuccess {
                    Log.d(TAG, "Success emiting: ${it.recipeFullInformationResult}")

                    emit(Resource.Success(RecipeResult.RecipeFullInformationResult(it.recipeFullInformationResult)))
                }

                result.onFailure {
                    Log.d(TAG, "Error emiting! Throwable: $it")

                    emit(Resource.Error("Not found :/"))
//                    emit(tryToGetFromCache(recipeSearch))
                }
            }
        }.flowOn(ioDispatcher)

    override fun getRecipeFullInformationBulk(recipeSearch: RecipeSearch.RecipeFullInformationBulkSearch): Flow<Resource<RecipeResult.RecipeFullInformationBulkResult>> {
        return flow {
            Log.d(TAG, "RecipeSearch: $recipeSearch")

            emit(Resource.Loading())

            service.recipeFullInformationBulk(
                query = recipeSearch.toQueryMap(),
                apiKey = apiKey
            ).let { result ->

                result.onSuccess {
                    Log.d(TAG, "Success emiting: ${it.recipeFullInformationBulkResult}")

                    emit(Resource.Success(it))
                }

                result.onFailure {
                    Log.d(TAG, "Error emiting! Throwable: $it")

                    emit(Resource.Error("Not found :/"))
                }
            }
        }.flowOn(ioDispatcher)
    }

    override fun getRecipeByIngredients(recipeSearch: RecipeSearch.RecipeByIngredientsSearch) =
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
        }.flowOn(ioDispatcher)

    companion object {
        private const val TAG = "TEST TAG"
    }
}
package com.example.whattoeat.data.remoteSource.repository

import android.util.Log
import com.example.whattoeat.data.remoteSource.service.SpoonacularApiService
import com.example.whattoeat.di.IoDispatcher
import com.example.whattoeat.domain.models.common.Recipe
import com.example.whattoeat.data.remoteSource.response.RecipeResponse
import com.example.whattoeat.domain.models.common.Resource
import com.example.whattoeat.domain.repositories.RecipeSearchRepository
import com.example.whattoeat.data.remoteSource.request.RecipeRequest
import com.example.whattoeat.data.remoteSource.request.toQueryMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RecipeSearchRepositoryImpl @Inject constructor(
    val apiKey: String,
    val service: SpoonacularApiService,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RecipeSearchRepository {

    override fun getRecipeComplex(recipeSearch: RecipeRequest.RecipeComplexRequest): Flow<Resource<RecipeResponse.RecipeComplexResponse>> {
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

    override fun getRecipeSimilar(recipeSearch: RecipeRequest.RecipeSimilarRequest): Flow<Resource<RecipeResponse.RecipeSimilarResponse>> =
        flow {
            Log.d(TAG, "RecipeSearch: $recipeSearch")

            emit(Resource.Loading())

            service.recipeSimilar(
                id = recipeSearch.id,
                query = recipeSearch.toQueryMap(),
                apiKey = apiKey
            ).let { result ->

                result.onSuccess {
                    Log.d(TAG, "Success emiting: ${it.recipes}")

                    emit(Resource.Success(RecipeResponse.RecipeSimilarResponse(it.recipes)))
                }

                result.onFailure {
                    Log.d(TAG, "Error emiting! Throwable: $it")

                    emit(Resource.Error("Not found :/"))
//                    emit(tryToGetFromCache(recipeSearch))
                }
            }
        }

    override fun getRecipeSummary(recipeSearch: RecipeRequest.RecipeSummaryRequest): Flow<Resource<Recipe.RecipeSummary>> {
        TODO("Not yet implemented")
    }

    override fun getRecipeFullInformation(recipeSearch: RecipeRequest.RecipeFullInformationRequest): Flow<Resource<RecipeResponse.RecipeFullInformationResponse>> =
        flow {
            Log.d(TAG, "RecipeSearch: $recipeSearch")

            emit(Resource.Loading())

            service.recipeFullInformation(
                id = recipeSearch.id,
                query = recipeSearch.toQueryMap(),
                apiKey = apiKey
            ).let { result ->

                result.onSuccess {
                    Log.d(TAG, "Success emiting: ${it.recipes}")

                    emit(Resource.Success(RecipeResponse.RecipeFullInformationResponse(it.recipes)))
                }

                result.onFailure {
                    Log.d(TAG, "Error emiting! Throwable: $it")

                    emit(Resource.Error("Not found :/"))
//                    emit(tryToGetFromCache(recipeSearch))
                }
            }
        }.flowOn(ioDispatcher)

    override fun getRecipeFullInformationBulk(recipeSearch: RecipeRequest.RecipeFullInformationBulkRequest): Flow<Resource<RecipeResponse.RecipeFullInformationBulkResponse>> {
        return flow {
            Log.d(TAG, "RecipeSearch: $recipeSearch")

            emit(Resource.Loading())

            service.recipeFullInformationBulk(
                query = recipeSearch.toQueryMap(),
                apiKey = apiKey
            ).let { result ->

                result.onSuccess {
                    Log.d(TAG, "Success emiting: ${it.recipes}")

                    emit(Resource.Success(it))
                }

                result.onFailure {
                    Log.d(TAG, "Error emiting! Throwable: $it")

                    emit(Resource.Error("Not found :/"))
                }
            }
        }.flowOn(ioDispatcher)
    }

    override fun getRecipeByIngredients(recipeSearch: RecipeRequest.RecipeByIngredientsRequest) =
        flow {
            emit(Resource.Loading())

            service.recipeByIngredients(
                query = recipeSearch.toQueryMap(),
                apiKey = apiKey
            ).let { result ->

                result.onSuccess {
                    emit(Resource.Success(it))
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